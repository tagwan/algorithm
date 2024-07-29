package com.github.tagwan.other.wheel

class MinimumIntervalException(should: Long, real: Long) : RuntimeException(
    "BlockingQueueTimer Minimum Interval should not less than " + should
            + ", now is " + real
)