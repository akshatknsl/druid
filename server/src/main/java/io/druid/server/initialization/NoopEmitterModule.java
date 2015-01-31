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

package io.druid.server.initialization;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.metamx.emitter.core.Emitter;
import com.metamx.emitter.core.NoopEmitter;
import io.druid.guice.ManageLifecycle;

/**
 */
public class NoopEmitterModule implements Module
{
  public static final String EMITTER_TYPE = "noop";

  @Override
  public void configure(Binder binder)
  {
  }

  @Provides
  @ManageLifecycle
  @Named(EMITTER_TYPE)
  public Emitter makeEmitter()
  {
    return new NoopEmitter();
  }
}
