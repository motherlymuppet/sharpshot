package com.durhack.sharpshot.core.nodes.other

import com.durhack.sharpshot.core.nodes.AbstractNode
import com.durhack.sharpshot.core.state.Direction
import java.math.BigInteger

class HaltNode(direction: Direction) : AbstractNode(direction) {
    override fun process(relativeDirection: Direction, value: BigInteger?) = mapOf<Direction, BigInteger?>()
    override val type = "halt"
    override fun reset() {}
}
