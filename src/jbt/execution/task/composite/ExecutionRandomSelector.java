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
package jbt.execution.task.composite;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.core.ITaskState;
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.composite.ModelRandomSelector;

/**
 * ExecutionRandomSelector is the ExecutionTask that knows how to run a
 * ModelRandomSelector.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionRandomSelector extends ExecutionComposite {
	/**
	 * Currently active child.
	 */
	private ExecutionTask activeChild;
	/**
	 * The currently active child of the selector. This integer is an index over
	 * the elements of {@link #order}.
	 */
	private int activeChildIndex;
	/**
	 * The list of children of this task.
	 */
	private List<ModelTask> children;
	/**
	 * List storing a sequence of integers with the order in which the children
	 * of this task must be evaluated. This list is computed when the task is
	 * spawned.
	 */
	private List<Integer> order;

	/**
	 * Constructs an ExecutionRandomSelector to run a specific
	 * ModelRandomSelector.
	 * 
	 * @param modelTask
	 *            the ModelRandomSelector to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionRandomSelector.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionRandomSelector(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelRandomSelector)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelRandomSelector.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Spawns the first task (randomly selected).
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.children = this.getModelTask().getChildren();
		/*
		 * First we initialize the list with the order in which the list of
		 * children will be evaluated.
		 */
		this.order = new Vector<Integer>();
		for (int i = 0; i < this.children.size(); i++) {
			this.order.add(i);
		}
		Collections.shuffle(this.order);

		/*
		 * Then we spawn the first child.
		 */
		this.activeChildIndex = 0;
		this.activeChild = this.children.get(this.order.get(this.activeChildIndex)).createExecutor(
				this.getExecutor(), this);
		this.activeChild.addTaskListener(this);
		this.activeChild.spawn(this.getContext());
	}

	/**
	 * Just terminates the currently active child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		this.activeChild.terminate();
	}

	/**
	 * Checks the status of the currently active child. If it is running,
	 * {@link Status#RUNNING} is returned. If it has successfully finished, it
	 * returns {@link Status#SUCCESS}. If it has failed, it tries to spawn the
	 * next child (returning {@link Status#RUNNING}). If it was the last child,
	 * returns {@link Status#FAILURE}.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		Status childStatus = this.activeChild.getStatus();

		if (childStatus == Status.RUNNING) {
			return Status.RUNNING;
		}
		else if (childStatus == Status.SUCCESS) {
			return Status.SUCCESS;
		}
		else {
			/* If it was the last child of the list, return failure. */
			if (this.activeChildIndex == this.children.size() - 1) {
				return Status.FAILURE;
			}

			this.activeChildIndex++;
			this.activeChild = this.children.get(this.order.get(this.activeChildIndex))
					.createExecutor(this.getExecutor(), this);
			this.activeChild.addTaskListener(this);
			this.activeChild.spawn(this.getContext());
			return Status.RUNNING;
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Just calls {@link #tick()} to make the task evolve.
	 * 
	 * @see jbt.execution.core.ExecutionTask#statusChanged(jbt.execution.core.event.TaskEvent)
	 */
	public void statusChanged(TaskEvent e) {
		this.tick();
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeState()
	 */
	protected ITaskState storeState() {
		return null;
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeTerminationState()
	 */
	protected ITaskState storeTerminationState() {
		return null;
	}
}
