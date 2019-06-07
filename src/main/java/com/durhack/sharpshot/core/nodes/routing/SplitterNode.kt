package com.durhack.sharpshot.core.nodes.routing

import com.durhack.sharpshot.core.nodes.AbstractNode
import com.durhack.sharpshot.core.state.Direction
import java.math.BigInteger

class SplitterNode : AbstractNode() {
    /**
     * Shoot out 3 bullets in other directions
     */
    override fun process(relativeDirection: Direction, value: BigInteger?): Map<Direction, BigInteger?> =
            relativeDirection.inverse.others.map { it to value }.toMap()

    override fun reset() {}
    override val type = "splitter"
    override val tooltip = "A bullet in one side produces 3 bullets in the others"
}
