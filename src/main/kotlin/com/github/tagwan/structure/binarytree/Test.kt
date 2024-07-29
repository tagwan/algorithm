package com.github.tagwan.structure.binarytree

fun create() {
    // 创建一棵空树
    val binaryTree = BinaryTree()
    // 创建一个根节点
    val root = TreeNode(1)
    // 把根节点赋给树
    binaryTree.root = root
    // 创建2个节点
    val rootLeft = TreeNode(2)
    val rootRight = TreeNode(3)
    // 把跟节点赋给树
    binaryTree.root?.left = rootLeft
    binaryTree.root?.right = rootRight
}

fun ergodic() {
    // 创建一棵树
    val binaryTree = BinaryTree()

    // 第一层
    // 创建一个根节点
    val root = TreeNode(1)
    // 把根节点赋给树
    binaryTree.root = root

    // 第二层
    // 创建2个节点
    val rootLeft = TreeNode(2)
    val rootRight = TreeNode(3)
    // 把跟节点赋给树
    binaryTree.root?.left = rootLeft
    binaryTree.root?.right = rootRight

    // 第三层
    // 为第二层的左节点创建2个节点
    rootLeft.left = TreeNode(4)
    rootLeft.right = TreeNode(5)
    // 为第二层的右节点创建2个节点
    rootRight.left = TreeNode(6)
    rootRight.right = TreeNode(7)

    print("前序遍历: ")
    binaryTree.frontShow()
    println(" ")
    print("中序遍历: ")
    binaryTree.midShow()
    println(" ")
    print("后序遍历: ")
    binaryTree.afterShow()
}

fun search() {
    // 创建一棵空树
    val binaryTree = BinaryTree()

    // 第一层
    // 创建一个根节点
    val root = TreeNode(1)
    // 把根节点赋给树
    binaryTree.root = root

    // 第二层
    // 创建2个节点
    val rootLeft = TreeNode(2)
    val rootRight = TreeNode(3)
    // 把跟节点赋给树
    binaryTree.root?.left = rootLeft
    binaryTree.root?.right = rootRight

    // 第三层
    // 为第二层的左节点创建2个节点
    rootLeft.left = TreeNode(4)
    rootLeft.right = TreeNode(5)
    // 为第二层的右节点创建2个节点
    rootRight.left = TreeNode(6)
    rootRight.right = TreeNode(7)

    // 前序查找
    val result: TreeNode? = binaryTree.frontSearch(3)
    println(result == rootRight)
    println(binaryTree.frontSearch(1)?.value)
    println(binaryTree.frontSearch(8))
}

fun delete() {
    // 创建一棵树
    val binaryTree = BinaryTree()

    // 第一层
    // 创建一个根节点
    val root = TreeNode(1)
    // 把根节点赋给树
    binaryTree.root = root

    // 第二层
    // 创建2个节点
    val rootLeft = TreeNode(2)
    val rootRight = TreeNode(3)
    // 把跟节点赋给树
    binaryTree.root?.left = rootLeft
    binaryTree.root?.right = rootRight

    // 第三层
    // 为第二层的左节点创建2个节点
    rootLeft.left = TreeNode(4)
    rootLeft.right = TreeNode(5)
    // 为第二层的右节点创建2个节点
    rootRight.left = TreeNode(6)
    rootRight.right = TreeNode(7)

    // 删除一个子树
    print("前序遍历: ")
    binaryTree.frontShow()
    binaryTree.delete(5)
    print("\n删除5后的前序遍历: ")
    binaryTree.frontShow()
    binaryTree.delete(3)
    print("\n删除5和3后的前序遍历: ")
    binaryTree.frontShow()
}