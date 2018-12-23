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
package jbt.model.task.composite;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.composite.ExecutionRandomSelector;
import jbt.model.core.ModelTask;

/**
 * A ModelRandomSelector is a task that behaves just like a ModelSelector, but
 * which walk through its children in a random order. Instead of evaluating its
 * children from left to right, this task evaluate them in a random order.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelRandomSelector extends ModelComposite {
	/**
	 * Creates a ModelRandomSelector with a guard and several children. The list
	 * of children cannot be empty.
	 * 
	 * @param guard
	 *            the guard of the ModelRandomSelector, which may be null.
	 * @param children
	 *            the list of children, which cannot be empty.
	 */
	public ModelRandomSelector(ModelTask guard, ModelTask... children) {
		super(guard, children);
	}

	/**
	 * Returns an ExecutionRandomSelector that knows how to run this
	 * ModelRandomSelector.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionRandomSelector(this, executor, parent);
	}
}
