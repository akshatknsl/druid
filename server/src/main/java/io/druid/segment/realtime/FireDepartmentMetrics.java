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

package io.druid.segment.realtime;

import com.google.common.base.Preconditions;

import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class FireDepartmentMetrics
{
  private final AtomicLong processedCount = new AtomicLong(0);
  private final AtomicLong thrownAwayCount = new AtomicLong(0);
  private final AtomicLong unparseableCount = new AtomicLong(0);
  private final AtomicLong rowOutputCount = new AtomicLong(0);
  private final AtomicLong numPersists = new AtomicLong(0);
  private final AtomicLong persistTimeMillis = new AtomicLong(0);
  private final AtomicLong persistBackPressureMillis = new AtomicLong(0);

  public void incrementProcessed()
  {
    processedCount.incrementAndGet();
  }

  public void incrementThrownAway()
  {
    thrownAwayCount.incrementAndGet();
  }

  public void incrementUnparseable()
  {
    unparseableCount.incrementAndGet();
  }

  public void incrementRowOutputCount(long numRows)
  {
    rowOutputCount.addAndGet(numRows);
  }

  public void incrementNumPersists()
  {
    numPersists.incrementAndGet();
  }

  public void incrementPersistTimeMillis(long millis)
  {
    persistTimeMillis.addAndGet(millis);
  }

  public void incrementPersistBackPressureMillis(long millis)
  {
    persistBackPressureMillis.addAndGet(millis);
  }

  public long processed()
  {
    return processedCount.get();
  }

  public long thrownAway()
  {
    return thrownAwayCount.get();
  }

  public long unparseable()
  {
    return unparseableCount.get();
  }

  public long rowOutput()
  {
    return rowOutputCount.get();
  }

  public long numPersists()
  {
    return numPersists.get();
  }

  public long persistTimeMillis()
  {
    return persistTimeMillis.get();
  }

  public long persistBackPressureMillis()
  {
    return persistBackPressureMillis.get();
  }

  public FireDepartmentMetrics snapshot()
  {
    final FireDepartmentMetrics retVal = new FireDepartmentMetrics();
    retVal.processedCount.set(processedCount.get());
    retVal.thrownAwayCount.set(thrownAwayCount.get());
    retVal.unparseableCount.set(unparseableCount.get());
    retVal.rowOutputCount.set(rowOutputCount.get());
    retVal.numPersists.set(numPersists.get());
    retVal.persistTimeMillis.set(persistTimeMillis.get());
    retVal.persistBackPressureMillis.set(persistBackPressureMillis.get());
    return retVal;
  }

  /**
   * merge other FireDepartmentMetrics, will modify this object's data
   *
   * @return this object
   */
  public FireDepartmentMetrics merge(FireDepartmentMetrics other)
  {
    Preconditions.checkNotNull(other, "Cannot merge a null FireDepartmentMetrics");
    FireDepartmentMetrics otherSnapshot = other.snapshot();
    processedCount.addAndGet(otherSnapshot.processed());
    thrownAwayCount.addAndGet(otherSnapshot.thrownAway());
    rowOutputCount.addAndGet(otherSnapshot.rowOutput());
    unparseableCount.addAndGet(otherSnapshot.unparseable());
    numPersists.addAndGet(otherSnapshot.numPersists());
    persistTimeMillis.addAndGet(otherSnapshot.persistTimeMillis());
    persistBackPressureMillis.addAndGet(otherSnapshot.persistBackPressureMillis());
    return this;
  }
}
