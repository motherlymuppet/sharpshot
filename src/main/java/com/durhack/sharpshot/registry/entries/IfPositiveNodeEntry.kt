package com.durhack.sharpshot.registry.entries

import com.durhack.sharpshot.core.nodes.routing.conditional.IfPositiveNode
import com.durhack.sharpshot.core.state.Direction
import com.durhack.sharpshot.gui.shapes.Draw
import com.durhack.sharpshot.registry.RegistryEntry
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class IfPositiveNodeEntry() : RegistryEntry<IfPositiveNode>(
        IfPositiveNode(Direction.UP),
        "If Positive",
        "Redirects all values larger than zero"
                                                           ) {

    override fun draw(node: IfPositiveNode,
                      gc: GraphicsContext,
                      x: Double,
                      y: Double,
                      scale: Int) {
        Draw.triangle(gc, node.direction, x, y, scale, Color.LIGHTSKYBLUE)
        Draw.text(gc, "+", x, y, scale)
    }
}