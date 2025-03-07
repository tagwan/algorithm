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

import com.github.tagwanj.ai.btree.Task;
import com.github.tagwanj.ai.btree.Decorator;

/**
 * 直接失败
 * An {@code AlwaysFail} decorator will fail no matter the wrapped task fails or succeeds.
 * 
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 * 
 * @author implicit-invocation
 */
public class AlwaysFail<E> extends Decorator<E> {
	private static final long serialVersionUID = 1L;

	/** Creates an {@code AlwaysFail} decorator with no child. */
	public AlwaysFail () {
	}

	/** Creates an {@code AlwaysFail} decorator with the given child.
	 * 
	 * @param task the child task to wrap */
	public AlwaysFail (Task<E> task) {
		super(task);
	}

	@Override
	public void childSuccess (Task<E> runningTask) {
		childFail(runningTask);
	}

}
