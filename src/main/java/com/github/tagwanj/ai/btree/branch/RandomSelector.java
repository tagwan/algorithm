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

package com.github.tagwanj.ai.btree.branch;

import com.github.tagwanj.ai.btree.Task;

import java.util.Arrays;
import java.util.List;

/**随机选择器<br>
 *  A {@code RandomSelector} is a selector task's variant that runs its children in a random order.
 * 
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 * 
 * @author implicit-invocation */
public class RandomSelector<E> extends Selector<E> {
	private static final long serialVersionUID = 1L;

	/** Creates a {@code RandomSelector} branch with no children. */
	public RandomSelector () {
		super();
	}

	/** Creates a {@code RandomSelector} branch with the given children.
	 * 
	 * @param tasks the children of this task */
	@SuppressWarnings("unchecked")
	public RandomSelector (Task<E>... tasks) {
		super(Arrays.asList(tasks));
	}

	/** Creates a {@code RandomSelector} branch with the given children.
	 * 
	 * @param tasks the children of this task */
	public RandomSelector (List<Task<E>> tasks) {
		super(tasks);
	}

	@Override
	public void start () {
		super.start();
		if (randomChildren == null) randomChildren = createRandomChildren();
	}
}
