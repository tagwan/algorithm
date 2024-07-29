package com.github.tagwan.structure


import java.util.LinkedList


//////////////////////////////////////////////////////////////////
//Uniform Grid:
//- 它将地图划分为固定大小的网格单元,每个网格单元存储该区域内的游戏对象。
//- 查询某个区域时,只需遍历相关的网格单元即可,效率较高。实现也相对简单。
//Quadtree:
//- Quadtree是一种空间划分树,它将二维平面递归地划分为4个子区域。
//- 对于稀疏的游戏地图,Quadtree可以更好地适应不同密度的区域,减少空间浪费。
//- 查询时可以通过遍历相关的子树节点来高效地获取目标区域内的游戏对象。
//R-Tree:
//- R-Tree是一种空间索引结构,它将游戏对象包围在最小矩形框(MBR)中,并组织成树形结构。
//- R-Tree擅长处理不规则形状的查询区域,相比Uniform Grid更加灵活。
//- 查询时可以通过遍历相关的树节点来高效地获取目标区域内的游戏对象。
//Bounding Volume Hierarchy (BVH):
//- BVH是一种基于包围体的空间划分结构,它将游戏对象层层包围在越来越小的包围体中。
//- BVH在碰撞检测等应用中表现优秀,同样适用于 AOI 查询场景。
//- 构建和查询的效率都很高,但实现相比前几种稍微复杂一些。
// so, 基于网格(九宫格)的AOI，查询效率高、实现简单,易于理解和维护。代码简洁易懂,可扩展性好
//////////////////////////////////////////////////////////////////

// 场景的宽度、高度和单元格大小
data class UniformGrid(
    val mapWidth: Int,
    val mapHeight: Int,
    val gridSize: Int
) {
    val gridWidth = mapWidth / gridSize
    val gridHeight = mapHeight / gridSize
}

interface IMapObject {
    val id: Long
    var x: Int
    var y: Int
    val tileSize: Int
    val area: Area
}

data class GameObject(
    override val id: Long,
    override var x: Int,
    override var y: Int,
    override val tileSize: Int
): IMapObject {

    constructor(x: Int, y: Int): this(0, x, y, 0)

    override val area: Area
        get() = Area(x, y, x + tileSize - 1, y + tileSize - 1)
}

data class Area(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    fun intersects(other: Area): Boolean {
        return !(other.left > this.right ||
                other.right < this.left ||
                other.top > this.bottom ||
                other.bottom < this.top)
    }
}

class AOIManager(
    private val scenario: UniformGrid
) {
    private val gameObjects = HashMap<Long, IMapObject>()
    private val grid = Array(scenario.gridWidth * scenario.gridHeight) { LinkedList<IMapObject>() }

    fun addObject(obj: IMapObject) {
        this.gameObjects[obj.id] = obj
        this.updateObjectCell(obj)
    }

    fun removeObject(obj: IMapObject) {
        this.gameObjects.remove(obj.id)
        this.updateObjectCell(obj, remove = true)
    }

    fun moveObject(obj: IMapObject, newX: Int, newY: Int) {
        obj.x = newX
        obj.y = newY
        this.updateObjectCell(obj)
    }

    fun getObjectsInRange(obj: IMapObject): List<IMapObject> {
        val area = obj.area
        val cellIndices = this.getCellIndicesInRange(area)
        return cellIndices.flatMap { grid[it] }
            .filter { it.id != obj.id && it.area.intersects(area) }
    }

    private fun updateObjectCell(obj: IMapObject, remove: Boolean = false) {
        val oldCellIndices = this.getCellIndicesForObject(obj)
        oldCellIndices.forEach { cellIndex ->
            if (remove) {
                grid[cellIndex].remove(obj)
            } else {
                grid[cellIndex].add(obj)
            }
        }
    }

    private fun getCellIndicesForObject(obj: IMapObject): List<Int> {
        val area = obj.area
        return getCellIndicesInRange(area)
    }

    private fun getCellIndicesInRange(area: Area): List<Int> {
        val startX = (area.left / scenario.gridSize).coerceIn(0, scenario.gridWidth - 1)
        val startY = (area.top / scenario.gridSize).coerceIn(0, scenario.gridHeight - 1)
        val endX = (area.right / scenario.gridSize).coerceIn(0, scenario.gridWidth - 1)
        val endY = (area.bottom / scenario.gridSize).coerceIn(0, scenario.gridHeight - 1)

        return (startX..endX).flatMap { x ->
            (startY..endY).map { y ->
                x + y * scenario.gridWidth
            }
        }
    }
}

//fun main() {
//    val scenario = UniformGrid(1000, 1000, 100)
//    val aoiManager = AOIManager(scenario)
//
//    val gameObjects = (1..100).map {
//        val x = (Math.random() * scenario.mapWidth).toInt()
//        val y = (Math.random() * scenario.mapHeight).toInt()
//        GameObject(it.toLong(), x, y, 10)
//    }
//
//    //val focusArea = Area(x - focusRange, y - focusRange, x + focusRange, y + focusRange)
//    //val cellIndicesInRange = aOIManager.getCellIndicesInRange(focusArea)
//    gameObjects.forEach { aoiManager.addObject(it) }
//
//    gameObjects.forEach { gameObject ->
//        val objectsInRange = aoiManager.getObjectsInRange(gameObject)
//        println("Object ${gameObject.id} has ${objectsInRange.size} objects in range")
//    }
//}