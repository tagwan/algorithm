package com.github.tagwan.pattern.other.btree

enum class EStatus {
    Invalid, // 初始状态
    Success,
    Failure,
    Running,
    Aborted // 终止
}

enum class EPolicy {
    RequireOne,
    RequireAll
}

enum class EActionMode {
    Attack, Patrol, Runaway
}

enum class EConditionMode {
    IsSeeEnemy, IsHealthLow, IsEnemyDead
}