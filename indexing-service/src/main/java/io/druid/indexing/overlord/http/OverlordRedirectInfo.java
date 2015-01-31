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

package io.druid.indexing.overlord.http;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import io.druid.indexing.overlord.TaskMaster;
import io.druid.server.http.RedirectInfo;

import java.net.URL;

/**
*/
public class OverlordRedirectInfo implements RedirectInfo
{
  private final TaskMaster taskMaster;

  @Inject
  public OverlordRedirectInfo(TaskMaster taskMaster)
  {
    this.taskMaster = taskMaster;
  }

  @Override
  public boolean doLocal()
  {
    return taskMaster.isLeading();
  }

  @Override
  public URL getRedirectURL(String queryString, String requestURI)
  {
    try {
      return new URL(String.format("http://%s%s", taskMaster.getLeader(), requestURI));
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
}
