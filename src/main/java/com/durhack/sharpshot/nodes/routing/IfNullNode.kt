package com.durhack.sharpshot.nodes.routing

import com.durhack.sharpshot.gui.shapes.Triangle
import javafx.scene.paint.Color
import java.math.BigInteger

class IfNullNode : AbstractConditionalNode() {
    override fun branch(value: BigInteger?) = value == null
    override fun graphic() = Triangle(rotation, Color.web("#FF6699"), "\" \"")
    override val type = "branch if null"
    override val tooltip = "Redirects all empty/null bullets. Other bullets pass through unaffected"
}
