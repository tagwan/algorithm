package com.github.tagwan.structure


/**
 * data structure: doubly linked list
 *
 * description: in a doubly linked list, each element stores a link to the previous and next elements
 *
 * time to insert an element at the beginning and end of the list: O(1)
 * insertion time in the middle by index: O(n)
 * delete: O(n)
 */

class DoubleLinkedList<T>(
    /**
     * stores a reference to the first element of the list
     *
     * if the list is empty, then the reference is null
     */
    private var first: Node<T>? = null,
    /**
     * stores a reference to the last element of the list
     *
     * if the list is empty, then the reference is null
     */
    private var last: Node<T>? = null
) {

    /**
     *  stores the number of elements in the list
     *
     */
    private var count: Int = 0

    /**
     * doubly linked list node
     *
     * @value - node value
     * @prev - link to the previous element (assuming the element is not the first one)
     * @next - link to the next element (assuming the element is not the last one)
     */
    class Node<T>(
        private val value: T,
        private var prev: Node<T>? = null,
        private var next: Node<T>? = null
    ) {

        fun changeNext(next: Node<T>? = null) {
            this.next = next
        }

        fun changePrev(prev: Node<T>? = null) {
            this.prev = prev
        }

        fun next() = next
        fun prev() = prev
        fun value() = value

        fun isOne() = prev == null && next == null
        fun isFirst() = prev == null
        fun isLast() = next == null
    }

    /**
     *
     * @return returns the number of elements in the list
     */
    fun size() = count

    /**
     * a simple function that converts a list into a normal Kotlin list for visual representation
     *
     * @return returns Kotlin a list of elements
     */
    fun toList() : List<T> {
        if (first == null) return listOf()

        val list = mutableListOf<T>()
        var node = first
        while (node != null) {
            list.add(node.value())
            node = node.next()
        }
        return list
    }

    /**
     * checks if an element is in the list
     *
     * @value - element value
     *
     * @return returns true if the value exists in the list
     */
    fun contains(value: T) : Boolean {
        if (first == null) return false

        var node = first
        while (node != null) {
            if (node.value() == value) {
                return true
            }
            node = node.next()
        }
        return false
    }

    /**
     * checking if the list is empty
     *
     * @return returns true if the list is empty
     */
    fun isEmpty() = first == null

    /**
     * removes an element from the list
     *
     * @value - element value
     *
     * @return returns true if the element was successfully removed
     */
    fun remove(value: T) : Boolean {
        if (first == null) return false

        var node = first

        while (node != null) {
            if (node.value() == value) {
                if (node.isOne()) {
                    first = null
                    last = null
                } else if (node.isFirst()) {
                    val next = node.next()
                    next?.changePrev(null)
                    first = next
                } else if (node.isLast()) {
                    val prev = node.prev()
                    prev?.changeNext(null)
                    last = prev
                } else {
                    node.prev()?.changeNext(node.next())
                    node.next()?.changePrev(node.prev())
                }
                count--
                return true
            }
            node = node.next()
        }
        return false
    }

    /**
     * add element by index
     *
     * @index - the index where the new element should be added
     * @value - the value of the new element
     *
     * @return returns true if the element was successfully added at the specified index
     */
    fun add(index: Int, value: T) : Boolean {

        if (first == null) return false

        var i = 0
        var node = first
        while (node != null) {
            if (i == index) {
                val newNode = Node(value)

                newNode.changePrev(node.prev())
                newNode.changeNext(node)

                node.prev()?.changeNext(newNode)
                node.changePrev(newNode)

                count++
                return true
            }
            i++
            node = node.next()
        }

        return false
    }

    /**
     * similar addLast method
     *
     */
    fun add(value: T) = addLast(value)

    /**
     * adds an element to the beginning of the list
     *
     * @value - element value
     */
    fun addFirst(value: T) {
        val firstNode = first
        first = if (firstNode == null) {
            Node(value)
        } else {
            val newNode = Node(value)
            newNode.changeNext(firstNode)
            firstNode.changePrev(newNode)
            newNode
        }
        if (last == null) {
            last = first
        }
        count++
    }

    /**
     * adds an element to the end of the list
     *
     * @value - element value
     */
    fun addLast(value: T) {
        val lastNode = last
        last = if (lastNode == null) {
            Node(value)
        } else {
            val newNode = Node(value)
            lastNode.changeNext(newNode)
            newNode.changePrev(lastNode)
            newNode
        }
        if (first == null) {
            first = last
        }
        count++
    }

}