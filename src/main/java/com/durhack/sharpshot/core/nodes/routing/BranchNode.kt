package com.durhack.sharpshot.core.nodes.routing

import com.durhack.sharpshot.core.nodes.AbstractNode
import com.durhack.sharpshot.core.state.Direction
import java.math.BigInteger

class BranchNode(direction: Direction) : AbstractNode(direction) {

    override fun process(relativeDirection: Direction, value: BigInteger?) =
            mapOf(Direction.UP to value)

    override fun reset() {}
    override val type = "branch"
}
