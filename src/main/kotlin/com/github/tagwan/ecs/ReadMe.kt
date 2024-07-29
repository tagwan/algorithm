package com.github.tagwan.ecs

object ReadMe {

    override fun toString(): String {
        "https://www.igiven.com/unity-2020-01-01-unity-entitas/"
        return """
        +------------------+
        |     Context      |
        |------------------|
        |    e       e     |      +-----------+
        |        e     e---|----> |  Entity   |
        |  e        e      |      |-----------|
        |     e  e       e |      | Component |
        | e            e   |      |           |      +-----------+
        |    e     e       |      | Component-|----> | Component |
        |  e    e     e    |      |           |      |-----------|
        |    e      e    e |      | Component |      |   Data    |
        +------------------+      +-----------+      +-----------+
        |
        |
        |     +-------------+  Groups:
        |     |      e      |  Subsets of entities in the context
        |     |   e     e   |  for blazing fast querying
        +---> |        +------------+
        |     e  |    |       |
        |  e     | e  |  e    |
        +--------|----+    e  |
        |     e      |
        |  e     e   |
        +------------+ 
                """.trimIndent()
    }
}