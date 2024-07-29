package com.github.tagwanj.ai.btree;


import com.github.tagwanj.ai.Entity;
import com.github.tagwanj.ai.IScript;

/**
 * 行为树脚本
 */
public interface IBehaviorTreeScript extends IScript {
	
	/**
	 * 为对象添加行为树
	 * @param entity
	 */
	default void addBehaviorTree(Entity entity) {
		
	}
}
