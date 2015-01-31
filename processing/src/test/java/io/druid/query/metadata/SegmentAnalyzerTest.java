/*
 * Druid - a distributed column store.
 * Copyright 2012 - 2015 Metamarkets Group Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.druid.query.metadata;

import com.google.common.collect.Lists;
import com.metamx.common.guava.Sequences;
import io.druid.query.LegacyDataSource;
import io.druid.query.QueryConfig;
import io.druid.query.QueryRunner;
import io.druid.query.QueryRunnerFactory;
import io.druid.query.QueryRunnerTestHelper;
import io.druid.query.metadata.metadata.ColumnAnalysis;
import io.druid.query.metadata.metadata.SegmentAnalysis;
import io.druid.query.metadata.metadata.SegmentMetadataQuery;
import io.druid.query.spec.QuerySegmentSpecs;
import io.druid.segment.IncrementalIndexSegment;
import io.druid.segment.QueryableIndexSegment;
import io.druid.segment.Segment;
import io.druid.segment.TestIndex;
import io.druid.segment.column.ValueType;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class SegmentAnalyzerTest
{
  @Test
  public void testIncrementalDoesNotWork() throws Exception
  {
    final List<SegmentAnalysis> results = getSegmentAnalysises(
        new IncrementalIndexSegment(TestIndex.getIncrementalTestIndex(false), null)
    );

    Assert.assertEquals(0, results.size());
  }

  @Test
  public void testMappedWorks() throws Exception
  {
    final List<SegmentAnalysis> results = getSegmentAnalysises(
        new QueryableIndexSegment("test_1", TestIndex.getMMappedTestIndex())
    );

    Assert.assertEquals(1, results.size());

    final SegmentAnalysis analysis = results.get(0);
    Assert.assertEquals("test_1", analysis.getId());

    final Map<String, ColumnAnalysis> columns = analysis.getColumns();
    Assert.assertEquals(TestIndex.COLUMNS.length, columns.size()); // All columns including time

    for (String dimension : TestIndex.DIMENSIONS) {
      final ColumnAnalysis columnAnalysis = columns.get(dimension);

      Assert.assertEquals(dimension, ValueType.STRING.name(), columnAnalysis.getType());
      Assert.assertTrue(dimension, columnAnalysis.getSize() > 0);
      Assert.assertTrue(dimension, columnAnalysis.getCardinality() > 0);
    }

    for (String metric : TestIndex.METRICS) {
      final ColumnAnalysis columnAnalysis = columns.get(metric);

      Assert.assertEquals(metric, ValueType.FLOAT.name(), columnAnalysis.getType());
      Assert.assertTrue(metric, columnAnalysis.getSize() > 0);
      Assert.assertNull(metric, columnAnalysis.getCardinality());
    }
  }

  /**
   * *Awesome* method name auto-generated by IntelliJ!  I love IntelliJ!
   *
   * @param index
   *
   * @return
   */
  private List<SegmentAnalysis> getSegmentAnalysises(Segment index)
  {
    final QueryRunner runner = QueryRunnerTestHelper.makeQueryRunner(
        (QueryRunnerFactory) new SegmentMetadataQueryRunnerFactory(
            new SegmentMetadataQueryQueryToolChest(),
            QueryRunnerTestHelper.NOOP_QUERYWATCHER
        ), index
    );

    final SegmentMetadataQuery query = new SegmentMetadataQuery(
        new LegacyDataSource("test"), QuerySegmentSpecs.create("2011/2012"), null, null, null
    );
    HashMap<String,Object> context = new HashMap<String, Object>();
    return Sequences.toList(query.run(runner, context), Lists.<SegmentAnalysis>newArrayList());
  }
}
