package com.github.tagwan.structure.aoi.link

import com.github.tagwan.other.Fix64


data class AoiNode(
    val layer: Int,
    var value: Fix64,
    val entity: AoiEntity? = null,
    var left: AoiNode? = null,
    var right: AoiNode? = null,
    var top: AoiNode? = null,
    var down: AoiNode? = null
) {
    fun circuitBreaker() {
        if (this.left != null)
            this.left?.right = this.right
        if (this.right != null)
            this.right?.left = this.left
        this.left = null
        this.right = null
    }
}
