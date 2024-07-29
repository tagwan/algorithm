package com.github.tagwan.structure.aoi.link

import com.github.tagwan.other.Fix64
import java.util.*

/**
 * Aoi linked list
 * <p>
 *     跳跃表
 *
 * @property maxLayer
 * @property limit
 * @constructor Create empty Aoi linked list
 */
class AoiLinkedList(
    private val maxLayer: Int = 9,
    val limit: Fix64 = Fix64.zero
) {
    private var header: AoiNode? = null
    private val random = Random()
    private var count: Int = 0

    /**
     * Add AoiEntity
     *
     * @param target
     * @param entity
     * @return
     */
    fun add(target: Fix64, entity: AoiEntity? = null): AoiNode? {
        var rLayer = 1
        if (header == null) {
            rLayer = maxLayer
            header = AoiNode(rLayer, target, entity)
            val tempHeader = header
            for (layer in (maxLayer - 1) downTo 1) {
                header?.down = AoiNode(layer, target, top = header)
                header = header?.down
            }

            header = tempHeader
            return null
        }

        while (rLayer < maxLayer && random.nextInt(2) == 0) {
            ++rLayer
        }

        var cur = header
        var insertNode: AoiNode? = null
        var lastLayerNode: AoiNode? = null

        for (layer in (maxLayer - 1) downTo 1) {
            while (cur?.right != null && cur.right?.value ?: Fix64.zero < target) {
                cur = cur.right
            }

            val right = cur?.right
            if (layer <= rLayer) {
                insertNode = AoiNode(layer, target, entity = entity, left = cur, right = right)

                if (right != null)
                    right.left = insertNode

                cur?.right = insertNode

                if (lastLayerNode != null) {
                    lastLayerNode.down = insertNode
                    insertNode.top = lastLayerNode
                }

                lastLayerNode = insertNode
            }

            cur = cur?.down
        }

        count++
        return insertNode
    }

    operator fun get(target: Fix64): AoiNode? {
        var node: AoiNode? = null
        var cur = header
        while (cur != null) {
            var right = cur.right
            while (right != null && right.value < target)
                cur = right
            right = cur?.right
            if (right != null && Fix64.abs(right.value - target) < limit) {
                node = right
                val down = node.down
                while (down != null)
                    node = down
                return node
            }

            cur = cur?.down
        }
        return null
    }

    /**
     * Remove AoiNode
     *
     * @param node
     */
    fun remove(node: AoiNode?) {
        var cur = node
        while (cur != null) {
            val tmp = cur
            tmp.circuitBreaker()
            cur = cur.top
        }

        count--
    }

    /**
     * Remove
     *
     * @param key
     * @param target
     */
    fun remove(key: Long, target: Fix64) {
        var cur = header
        for (layer in (maxLayer - 1) downTo 1) {
            var right = cur?.right
            while (right != null && right.value < target) cur = right
            right = cur?.right
            if (right != null && right.value - target <= limit && right.entity?.key == key) {
                right.circuitBreaker()
            }

            cur = cur?.down
        }
        count--
    }

    /**
     * Move
     *
     * @param node
     * @param target
     */
    fun move(node: AoiNode?, target: Fix64) {
        var cur: AoiNode? = node

        // region Left
        if (target > cur?.value ?: Fix64.zero) {
            while (cur != null) {
                if (cur.right != null && target > cur.right?.value?: Fix64.zero) {
                    var findNode = cur

                    // Find the target node to be moved to.
                    while (findNode?.right != null && findNode.right?.value?: Fix64.zero < target)
                        findNode = findNode.right

                    // Fuse the current node.
                    cur.circuitBreaker()

                    // Move to the target node location
                    cur.left = findNode
                    cur.right = findNode?.right
                    findNode?.right = findNode?.right
                    if (findNode?.right != null)
                        findNode.right?.left = cur
                    findNode?.right = cur
                }

                cur.value = target
                cur = cur.top
            }

            return
        }

        // endregion

        // region Right
        while (cur != null) {
            if (cur.left != null && target < cur.left?.value?: Fix64.zero) {
                // Find the target node to be moved to.
                var findNode = cur
                var findLeft = findNode.left
                while (findLeft != null && findLeft.value > target)
                    findNode = findNode?.left

                // Fuse the current node.
                cur.circuitBreaker()

                // Move to the target node location
                cur.right = findNode
                cur.left = findNode?.left
                findLeft = findNode?.left
                if (findLeft != null) findLeft.right = cur
                findNode?.left = cur
            }

            cur.value = target
            cur = cur.top
        }
        // endregion
    }
}