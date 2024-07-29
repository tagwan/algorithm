package com.github.tagwan.algorithm

import java.util.*

/**
 * 跳跃表
 *
 * @param headNode      头节点，入口 [SkipNode]
 * @param highLevel     层数
 * @param random        用于投掷硬币 [Random]
 *
 * @author jdg
 */
class SkipList<T> private constructor(
    private var headNode: SkipNode<T>,
    private var highLevel: Int,
    private val random: Random
) {

    constructor(): this(
        SkipNode(Int.MIN_VALUE, null),
        0,
        Random()
    )

    /**
     * Search
     *
     * @param key
     * @return
     */
    fun search(key: Int): SkipNode<T>? {
        var team: SkipNode<T>? = headNode
        while (team != null) {

            // 找到了
            if (team.key == key) {
                return team
            }

            val right = team.right
            team = when {
                (right == null) -> {

                    // 右侧没有了，只能下降
                    team.down
                }
                (right.key > key) -> {

                    // 需要下降去寻找
                    team.down
                }
                else -> {

                    // 右侧比较小向右
                    team.right
                }
            }
        }
        return null
    }

    /**
     * delete
     * <p>
     *     删除不需要考虑层数
     *
     * @param key
     */
    fun delete(key: Int) {
        var team: SkipNode<T>? = headNode

        while (team != null) {
            val right = team.right
            team = when {
                (right == null) -> {

                    //右侧没有了，说明这一层找到，没有只能下降
                    team.down
                }
                (right.key == key) -> {

                    //找到节点，右侧即为待删除节点
                    team.right = right.right //删除右侧节点
                    team.down //向下继续查找删除
                }
                (right.key > key) -> {

                    //右侧已经不可能了，向下
                    team.down
                }
                else -> {

                    // 节点还在右侧
                    team.right
                }
            }
        }
    }

    /**
     * Add
     *
     * @param node
     */
    fun add(node: SkipNode<T>) {
        val key = node.key
        val findNode = search(key)

        // 如果存在这个key的节点
        if (findNode != null) {
            findNode.value = node.value
            return
        }

        // 存储向下的节点, 这些节点可能在右侧插入节点
        val stack = Stack<SkipNode<T>>()

        // 查找待插入的节点, 找到最底层的哪个节点。
        var team: SkipNode<T>? = headNode

        // 进行查找操作
        while (team != null) {
            val right = team.right
            team = when {
                right == null -> {
                    // 右侧没有了，只能下降
                    stack.add(team) // 将曾经向下的节点记录一下
                    team.down
                }
                right.key > key -> {
                    //需要下降去寻找
                    stack.add(team) // 将曾经向下的节点记录一下
                    team.down
                }
                else -> {
                    //向右
                    right
                }
            }
        }


        var level = 1 // 当前层数，从第一层添加(第一层必须添加，先添加再判断)
        var downNode: SkipNode<T>? = null // 保持前驱节点(即down的指向，初始为null)

        while (!stack.isEmpty()) {

            // 在该层插入node
            team = stack.pop() // 抛出待插入的左侧节点
            val nodeTeam = SkipNode(node.key, node.value) // 节点需要重新创建
            nodeTeam.down = downNode // 处理竖方向
            downNode = nodeTeam // 标记新的节点下次使用
            if (team.right == null) {
                // 右侧为null 说明插入在末尾
                team.right = nodeTeam
            } else {
                // 右侧还有节点，插入在两者之间
                nodeTeam.right = team.right
                team.right = nodeTeam
            }

            //考虑是否需要向上
            if (level > MAX_LEVEL)
                break // 已经到达最高级的节点啦

            val num = random.nextDouble() // [0-1]随机数
            if (num > 0.5) // 运气不好结束
                break

            level++
            if (level > highLevel) // 比当前最大高度要高但是依然在允许范围内 需要改变head节点
            {
                highLevel = level

                // 需要创建一个新的节点
                val highHeadNode = SkipNode<T>(Int.MIN_VALUE, null)
                highHeadNode.down = headNode
                headNode = highHeadNode // 改变head
                stack.add(headNode) // 下次抛出head
            }
        }
    }

    /**
     * Print list
     *
     */
    fun printList() {
        var teamNode: SkipNode<*>? = headNode
        var index = 1
        var last = teamNode
        while (last!!.down != null) {
            last = last.down
        }
        while (teamNode != null) {
            var enumNode = teamNode.right
            var enumLast = last.right
            System.out.printf("%-8s", "head->")
            while (enumLast != null && enumNode != null) {
                if (enumLast.key == enumNode.key) {
                    System.out.printf("%-5s", enumLast.key.toString() + "->")
                    enumLast = enumLast.right
                    enumNode = enumNode.right
                } else {
                    enumLast = enumLast.right
                    System.out.printf("%-5s", "")
                }
            }
            teamNode = teamNode.down
            index++
            println()
        }
    }

    companion object {
        private const val MAX_LEVEL = 32 // 最大的层
    }

}

class SkipNode<T>(
    var key: Int,
    var value: T?
) {
    var right: SkipNode<T>? = null
    var down: SkipNode<T>? = null //左右上下四个方向的指针
}

fun main() {
    val list = SkipList<Int>()
    for (i in 1..19) {
        list.add(SkipNode(i, 666))
    }
    list.printList()
    list.delete(4)
    list.delete(8)
    list.printList()
}
