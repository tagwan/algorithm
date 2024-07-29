package com.github.tagwan.structure.aoi.grid

class AOIBlock {
    // 当前块有哪些玩家
    val map = HashMap<Long, PlayerEntity>()
}

data class PlayerEntity(
    val id: Long,
    val transformInfo: TransformInfo,
    val aoiComponent: AoiComponent
)

data class TransformInfo(
    var x: Int,
    var y: Int
)

data class AoiComponent(
    var blockX: Int,
    var blockY: Int
)