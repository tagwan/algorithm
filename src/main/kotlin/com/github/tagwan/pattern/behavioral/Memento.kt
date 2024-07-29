package com.github.tagwan.pattern.behavioral

data class Memento(val state: String)

class Originator(var state: String) {

    fun createMemento(): Memento {
        return Memento(state)
    }

    fun restore(memento: Memento) {
        state = memento.state
    }
}

class CareTaker {
    private val mementoList = ArrayList<Memento>()

    fun saveState(state: Memento) {
        mementoList.add(state)
    }

    fun restore(index: Int): Memento {
        return mementoList[index]
    }
}

/**
 * 备忘录模式是在不破坏封装的前提下，捕获一个对象的内部状态，
 *  并在该对象之外保存这个状态，这样可以在以后将对象恢复到原先保存的状态。
 *
 */
fun main() {
    val originator = Originator("initial state")
    val careTaker = CareTaker()
    careTaker.saveState(originator.createMemento())

    originator.state = "State #1"
    originator.state = "State #2"
    careTaker.saveState(originator.createMemento())

    originator.state = "State #3"
    println("Current State: " + originator.state)
    //assertThat(originator.state).isEqualTo("State #3")

    originator.restore(careTaker.restore(1))
    println("Second saved state: " + originator.state)
    //assertThat(originator.state).isEqualTo("State #2")

    originator.restore(careTaker.restore(0))
    println("First saved state: " + originator.state)
}