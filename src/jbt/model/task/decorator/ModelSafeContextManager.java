/*
 * Copyright (C) 2012 Ricardo Juan Palma Durán
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jbt.model.task.decorator;

import jbt.execution.context.SafeContext;
import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionSafeContextManager;
import jbt.model.core.ModelTask;

/**
 * A ModelSafeContextManager is a decorator that creates a new context for its
 * child task. The context that it creates is a {@link SafeContext}, and the
 * input context that the SafeContext receives is the context of the
 * ModelSafeContextManager.
 * <p>
 * The spawning and updating of the child task are carried out as usual.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSafeContextManager extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelSafeContextManager, which may be null.
	 * @param child
	 *            the child of the ModelSafeContextManager.
	 */
	public ModelSafeContextManager(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionSafeContextManager that knows how to run this
	 * ModelSafeContextManager.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSafeContextManager(this, executor, parent);
	}
}
