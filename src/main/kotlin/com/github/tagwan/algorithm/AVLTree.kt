//package com.github.tagwan.algorithm
//
//
///**
// * AVL平衡二叉树
// */
//class AVLTree<K : Comparable<K>?, V> {
//    private var root: Node? = null
//    var size = 0
//        private set
//
//    /**
//     * 获取某个节点的高度
//     *
//     * @param node
//     * @return int
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private fun getHeight(node: Node?): Int {
//        return node?.height ?: 0
//    }
//
//    /**
//     * 获取某个节点的平衡因子
//     *
//     * @param node
//     * @return int
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private fun getBalanceFactor(node: Node?): Int {
//        return if (node == null) {
//            0
//        } else getHeight(node.left) - getHeight(node.right)
//    }
//
//    /**
//     * 查看AVL平衡二叉树是否是二分搜索树
//     *
//     * @param
//     * @return boolean
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private val isBinarySearchTree: Boolean
//        private get() {
//            val keys = ArrayList<K>()
//            inOrder(root, keys)
//            for (i in 1 until keys.size) {
//                if (keys[i - 1]!!.compareTo(keys[i]) > 0) {
//                    return false
//                }
//            }
//            return true
//        }
//
//    /**
//     * 查看AVL平衡二叉树是否是平衡二叉树
//     *
//     * @return boolean
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private val isBalanced: Boolean
//        private get() = isBalanced(root)
//
//    /**
//     * 递归查看以node为根节点的AVL平衡二叉树是否是平衡二叉树
//     *
//     * @param node
//     * @return boolean
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private fun isBalanced(node: Node?): Boolean {
//        if (node == null) {
//            return true
//        }
//        val balanceFactor = getBalanceFactor(node)
//        return if (Math.abs(balanceFactor) > 1) {
//            false
//        } else isBalanced(node.left) && isBalanced(node.right)
//    }
//    // 对节点y进行向右旋转操作，返回旋转后新的根节点x
//    //        y                              x
//    //       / \                           /   \
//    //      x   T4     向右旋转 (y)        z     y
//    //     / \       - - - - - - - ->    / \   / \
//    //    z   T3                       T1  T2 T3 T4
//    //   / \
//    // T1   T2
//    /**
//     * 右旋转操作
//     * @param y
//     * @return AVLTree<K></K>,V>.Node
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private fun rightRotate(y: Node): Node {
//        val x = y.left
//            ?: return y
//        val T3 = x.right
//
//        //右旋转操作
//        x.right = y
//        y.left = T3
//
//        //更新height
//        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1
//        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1
//        return x
//    }
//
//    // 对节点y进行向左旋转操作，返回旋转后新的根节点x
//    //    y                             x
//    //  /  \                          /   \
//    // T1   x      向左旋转 (y)       y     z
//    //     / \   - - - - - - - ->   / \   / \
//    //   T2  z                     T1 T2 T3 T4
//    //      / \
//    //     T3 T4
//    /**
//     * 左旋转操作
//     *
//     * @param y
//     * @return AVLTree<K></K>,V>.Node
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    private fun leftRotate(y: Node): Node {
//        val x: Node = y.right
//        val T2: Node = x.left
//
//        //左旋转操作
//        x.left = y
//        y.right = T2
//
//        //更新height
//        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1
//        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1
//        return x
//    }
//
//    /**
//     * 中序遍历以node为根节点的AVL平衡二叉树
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @return void
//     * @author ronglexie
//     * @version 2018/8/18
//     */
//    private fun inOrder(node: Node?, keys: ArrayList<K>) {
//        if (node == null) {
//            return
//        }
//        inOrder(node.left, keys)
//        keys.add(node.key)
//        inOrder(node.right, keys)
//    }
//
//    /**
//     * 向AVL平衡二叉树中插入元素
//     *
//     * @param key
//     * @param value
//     * @return void
//     * @author ronglexie
//     * @version 2018/9/1
//     */
//    fun add(key: K, value: V) {
//        root = add(root, key, value)
//    }
//
//    /**
//     * 向node为根元素的AVL平衡二叉树中插入元素
//     * 递归算法
//     *
//     * @param node
//     * @param key
//     * @param value
//     * @return void
//     * @author ronglexie
//     * @version 2018/8/19
//     */
//    private fun add(node: Node?, key: K, value: V): Node {
//        //递归终止条件，返回结果为null
//        if (node == null) {
//            size++
//            return Node(key, value)
//        }
//        if (key!!.compareTo(node.key) < 0) {
//            node.left = add(node.left, key, value)
//        } else if (key.compareTo(node.key) > 0) {
//            node.right = add(node.right, key, value)
//        } else {
//            node.value = value
//        }
//        /**========== 维护平衡 Start ========== */
//        //更新Height
//        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right))
//        //计算平衡因子
//        val balanceFactor = getBalanceFactor(node)
//        //LL左孩子节点的左侧产生不平衡
//        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
//            //右旋转操作
//            return rightRotate(node)
//        }
//        //RR右孩子节点的右侧产生不平衡
//        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
//            //左旋转操作
//            return leftRotate(node)
//        }
//        //LR左孩子节点的右侧产生不平衡
//        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
//            node.left = leftRotate(node.left)
//            //右旋转操作
//            return rightRotate(node)
//        }
//        //RL右孩子节点的左侧产生不平衡
//        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
//            node.right = rightRotate(node.right)
//            //右旋转操作
//            return leftRotate(node)
//        }
//        /**========== 维护平衡 End ========== */
//        return node
//    }
//
//    /**
//     * 查找AVL平衡二叉树的最小值
//     *
//     * @param
//     * @return V
//     * @author ronglexie
//     * @version 2018/8/18
//     */
//    fun minimum(): V {
//        require(!isEmpty) { "BinarySearchTree is empty !" }
//        return minimum(root).value
//    }
//
//    /**
//     * 查找以node为根节点AVL平衡二叉树的最小节点
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    private fun minimum(node: Node?): Node? {
//        require(!isEmpty) { "BinarySearchTree is empty !" }
//        return if (node.left == null) {
//            node
//        } else minimum(node.left)
//    }
//
//    /**
//     * 查找AVL平衡二叉树的最大值
//     *
//     * @param
//     * @return V
//     * @author ronglexie
//     * @version 2018/8/18
//     */
//    fun maximize(): V {
//        require(!isEmpty) { "BinarySearchTree is empty !" }
//        return maximize(root).value
//    }
//
//    /**
//     * 查找以node为根节点AVL平衡二叉树的最大节点
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    private fun maximize(node: Node?): Node? {
//        require(!isEmpty) { "BinarySearchTree is empty !" }
//        return if (node.right == null) {
//            node
//        } else minimum(node.right)
//    }
//
//    /**
//     * 删除AVL平衡二叉树的最大值
//     *
//     * @param
//     * @return V
//     * @author ronglexie
//     * @version 2018/8/18
//     */
//    fun removeMax(): V {
//        val maximize = maximize()
//        removeMax(root)
//        return maximize
//    }
//
//    /**
//     * 删除以node为根的AVL平衡二叉树的最大节点
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    private fun removeMax(node: Node?): Node? {
//        if (node.right == null) {
//            val leftNode: Node = node.left
//            node.left = null
//            size--
//            return leftNode
//        }
//        node.right = removeMin(node.right)
//        return node
//    }
//
//    /**
//     * 删除AVL平衡二叉树的最小值
//     *
//     * @param
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    fun removeMin(): V {
//        val minimum = minimum()
//        removeMin(root)
//        return minimum
//    }
//
//    /**
//     * 删除以node为根的AVL平衡二叉树的最小节点
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    private fun removeMin(node: Node): Node {
//        if (node.left == null) {
//            val rightNode: Node = node.right
//            node.right = null
//            size--
//            return rightNode
//        }
//        node.left = removeMin(node.left)
//        return node
//    }
//
//    fun remove(key: K): V? {
//        val node: Node? = getNode(root, key)
//        if (node != null) {
//            root = remove(root, key)
//            return node.value
//        }
//        return null
//    }
//
//    /**
//     * 删除以node为根的AVL平衡二叉树中的指定元素
//     * 深度优先遍历，递归实现
//     *
//     * @param node
//     * @param key
//     * @return BinarySearchTree<E>.Node
//     * @author ronglexie
//     * @version 2018/8/18
//    </E> */
//    private fun remove(node: Node, key: K): Node? {
//        if (node == null) {
//            return null
//        }
//        val resultNode: Node
//        if (key!!.compareTo(node.key) < 0) {
//            node.left = remove(node.left, key)
//            resultNode = node
//        } else if (key.compareTo(node.key) > 0) {
//            node.right = remove(node.right, key)
//            resultNode = node
//        } else  /*if(key.compareTo(node.key) == 0)*/ {
//            // 删除右子树为空的情况
//            if (node.right == null) {
//                val leftNode: Node = node.left
//                node.left = null
//                size--
//                resultNode = leftNode
//            } else if (node.left == null) {
//                val rightNode: Node = node.right
//                node.right = null
//                size--
//                resultNode = rightNode
//            } else {
//                // 1、删除后用后继节点替代该位置(后继节点即待删除节点右子树中的最小节点)
//                // 获得后继节点
//                val successor: Node = minimum(node.right)
//                // 删除后继节点，并让待删除节点的右子树成为后继节点的右子树
//                successor.right = remove(node.right, successor.key)
//                // 让待删除节点的左子树成为后继节点的左子树
//                successor.left = node.left
//                // 将待删除节点的左、右子节点置为空
//                node.right = null
//                node.left = node.right
//                resultNode = successor
//                /**
//                 * // 2、删除后用前驱节点替代该位置(前驱节点即待删除节点左子树中的最大节点)
//                 * // 获得前驱节点
//                 * Node predecessor = maximize(node.left);
//                 * // 删除前驱节点，并让待删除节点的左子树成为前驱节点的左子树
//                 * predecessor.left = removeMax(node);
//                 * // 让待删除节点的右子树成为前驱节点的右子树
//                 * predecessor.right = node.right;
//                 * // 将待删除节点的左、右子节点置为空
//                 * node.left = node.right = null;
//                 * return predecessor;
//                 */
//            }
//        }
//        /**========== 维护平衡 Start ========== */
//        if (resultNode == null) {
//            return null
//        }
//
//        //更新Height
//        resultNode.height = 1 + Math.max(getHeight(resultNode.left), getHeight(resultNode.right))
//        //计算平衡因子
//        val balanceFactor = getBalanceFactor(resultNode)
//        //LL左孩子节点的左侧产生不平衡
//        if (balanceFactor > 1 && getBalanceFactor(resultNode.left) >= 0) {
//            //右旋转操作
//            return rightRotate(resultNode)
//        }
//        //RR右孩子节点的右侧产生不平衡
//        if (balanceFactor < -1 && getBalanceFactor(resultNode.right) <= 0) {
//            //左旋转操作
//            return leftRotate(resultNode)
//        }
//        //LR左孩子节点的右侧产生不平衡
//        if (balanceFactor > 1 && getBalanceFactor(resultNode.left) < 0) {
//            resultNode.left = leftRotate(resultNode.left)
//            //右旋转操作
//            return rightRotate(resultNode)
//        }
//        //RL右孩子节点的左侧产生不平衡
//        if (balanceFactor < -1 && getBalanceFactor(resultNode.right) > 0) {
//            resultNode.right = rightRotate(resultNode.right)
//            //右旋转操作
//            return leftRotate(resultNode)
//        }
//        /**========== 维护平衡 End ========== */
//        return resultNode
//    }
//
//    operator fun contains(key: K): Boolean {
//        return getNode(root, key) != null
//    }
//
//    operator fun get(key: K): V? {
//        val node: Node? = getNode(root, key)
//        return if (node != null) node.value else null
//    }
//
//    operator fun set(key: K, value: V) {
//        val node: Node = getNode(root, key) ?: throw IllegalArgumentException("Set failed. key is not exists!")
//        node.value = value
//    }
//
//    val isEmpty: Boolean
//        get() = size == 0
//
//    /**
//     * 根据key获取Node
//     *
//     * @param node
//     * @param key
//     * @return map.LinkedListMap<K></K>,V>.Node
//     * @author ronglexie
//     * @version 2018/8/19
//     */
//    fun getNode(node: Node?, key: K): Node? {
//        if (node == null) {
//            return null
//        }
//        return if (key!!.compareTo(node.key) == 0) {
//            node
//        } else if (key.compareTo(node.key) < 0) {
//            getNode(node.left, key)
//        } else {
//            getNode(node.right, key)
//        }
//    }
//
//    /**
//     * 节点类
//     *
//     * @author ronglexie
//     * @version 2018/8/18
//     */
//    private inner class Node(var key: K, var value: V) {
//        var left: Node? = null
//        var right: Node? = null
//        var height = 1
//    }
//}