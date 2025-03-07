/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.github.tagwanj.ai.btree.decorator;

import com.github.tagwanj.ai.btree.LoopDecorator;
import com.github.tagwanj.ai.btree.Task;

/**
 * 直到成功<br>
 * The {@code UntilSuccess} decorator will repeat the wrapped task until that
 * task succeeds, which makes the decorator succeed.
 * <p>
 * Notice that a wrapped task that always fails without entering the running
 * status will cause an infinite loop in the current frame.
 * 
 * @param <E>
 *            type of the blackboard object that tasks use to read or modify
 *            game state
 * 
 * @author implicit-invocation
 * @author davebaol
 */
public class UntilSuccess<E> extends LoopDecorator<E> {
	private static final long serialVersionUID = 1L;

	/** Creates an {@code UntilSuccess} decorator with no child. */
	public UntilSuccess() {
	}

	/**
	 * Creates an {@code UntilSuccess} decorator with the given child.
	 * 
	 * @param task
	 *            the child task to wrap
	 */
	public UntilSuccess(Task<E> task) {
		super(task);
	}

	@Override
	public void childSuccess(Task<E> runningTask) {
		success();
		loop = false;
	}

	@Override
	public void childFail(Task<E> runningTask) {
		loop = true;
	}
}
