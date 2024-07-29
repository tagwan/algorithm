package com.github.tagwan.pattern.behavioral

import kotlin.properties.Delegates

interface TextChangedListener {

    fun onTextChanged(oldText: String, newText: String)
}

class PrintingTextChangedListener : TextChangedListener {

    private var text = ""

    override fun onTextChanged(oldText: String, newText: String) {
        text = "Text is changed: $oldText -> $newText"
    }
}

class TextView {

    val listeners = mutableListOf<TextChangedListener>()

    var text: String by Delegates.observable("<empty>") { _, old, new ->
        listeners.forEach { it.onTextChanged(old, new) }
    }
}

fun main() {
    val textView = TextView().apply {
        this.listeners.add(PrintingTextChangedListener())
    }

    with(textView) {
        text = "Lorem ipsum"
        text = "dolor sit amet"
    }
}