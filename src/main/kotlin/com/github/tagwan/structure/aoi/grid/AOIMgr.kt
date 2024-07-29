package com.github.tagwan.structure.aoi.grid

import com.github.tagwan.structure.aoi.grid.AOIBlock
import com.github.tagwan.structure.aoi.grid.PlayerEntity

class AOIMgr(
    val AOISize: Int = 96 // 视野大小 96 * 96
) {

    var blockSize: Int = 0
    var mapSize: Int = 0 // 暂时支持正方形，表示我们当前的地图
    var AOIBlockNum: Int = 0
    var AOIBlocksData: Array<Array<AOIBlock>> = emptyArray()

    fun init(mapSize: Int, blockSize: Int) {
        this.blockSize = AOISize / 3 // 由视野大小，算出AOI每个小块大小
        this.mapSize = mapSize * blockSize // SGYD --> 64 * 4 = 256

        this.AOIBlockNum = this.mapSize / this.blockSize
        this.AOIBlocksData = Array(this.AOIBlockNum) { Array(this.AOIBlockNum) { AOIBlock() } }

    }

    fun sendMsgInMap(map: Map<Long, PlayerEntity>, msg: Any, toPlayers: ArrayList<PlayerEntity>, ignoreId: Long) {
        for ((id, entity) in map) {
            if (id == ignoreId)
                continue

            toPlayers.add(entity)

            // push
        }
    }

    fun sendMsg2AOIPlayer(blockX: Int, blockY: Int, msg: Any, toPlayers: ArrayList<PlayerEntity>, ignoreId: Long) {
        if (blockX < 0 || blockY < 0 || blockX >= this.AOIBlockNum|| blockY >= this.AOIBlockNum)
            return

        // 当前中心块
        this.sendMsgInMap(this.AOIBlocksData[blockX][blockY].map, msg, toPlayers, ignoreId)

        // 左
        if (blockX - 1 > 0) {
            this.sendMsgInMap(this.AOIBlocksData[blockX - 1][blockY].map, msg, toPlayers, ignoreId)
        }

        // 右
        if (blockX + 1 < this.AOIBlockNum) {
            this.sendMsgInMap(this.AOIBlocksData[blockX + 1][blockY].map, msg, toPlayers, ignoreId)
        }

        // 上
        if (blockY + 1 < this.AOIBlockNum) {
            this.sendMsgInMap(this.AOIBlocksData[blockX][blockY + 1].map, msg, toPlayers, ignoreId)
        }

        // 下
        if (blockY - 1 > 0) {
            this.sendMsgInMap(this.AOIBlocksData[blockX][blockY - 1].map, msg, toPlayers, ignoreId)
        }

        // 左上
        if (blockX - 1 > 0 && blockY + 1 < this.AOIBlockNum) {
            this.sendMsgInMap(this.AOIBlocksData[blockX - 1][blockY + 1].map, msg, toPlayers, ignoreId)
        }

        // 左下
        if (blockX - 1 > 0 && blockY - 1 > 0) {
            this.sendMsgInMap(this.AOIBlocksData[blockX - 1][blockY - 1].map, msg, toPlayers, ignoreId)
        }

        // 右上
        if (blockX + 1 < this.AOIBlockNum && blockY + 1 < this.AOIBlockNum) {
            this.sendMsgInMap(this.AOIBlocksData[blockX + 1][blockY + 1].map, msg, toPlayers, ignoreId)
        }

        // 右下
        if (blockX + 1 < this.AOIBlockNum && blockY - 1 > 0) {
            this.sendMsgInMap(this.AOIBlocksData[blockX + 1][blockY - 1].map, msg, toPlayers, ignoreId)
        }
    }

    fun addToAOIMgr(entity: PlayerEntity) {
        val blockX = entity.transformInfo.x / this.blockSize
        val blockY = entity.transformInfo.y / this.blockSize
        entity.aoiComponent.blockX = blockX
        entity.aoiComponent.blockY = blockY


        // 通知区域内其他玩家


        // 周围玩家消息推送给我


        // 加入
        this.AOIBlocksData[blockX][blockY].map[entity.id] = entity

        // 旧的移除

    }

    fun leaveAOIMgr(entity: PlayerEntity) {
        val blockX = entity.transformInfo.x / this.blockSize
        val blockY = entity.transformInfo.y / this.blockSize
        if (blockX < 0 || blockY < 0 || blockX >= this.AOIBlockNum|| blockY >= this.AOIBlockNum)
            return

        // 通知别人
    }
}