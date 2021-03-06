package com.durhack.sharpshot.registry.entries

import com.durhack.sharpshot.core.nodes.routing.VoidNode
import com.durhack.sharpshot.core.state.Direction
import com.durhack.sharpshot.gui.shapes.Draw
import com.durhack.sharpshot.registry.RegistryEntry
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class VoidNodeEntry() : RegistryEntry<VoidNode>(
        VoidNode(Direction.UP),
        "Void",
        "Blocks bullets"
                                               ) {

    override fun draw(node: VoidNode,
                      gc: GraphicsContext,
                      x: Double,
                      y: Double,
                      scale: Int) {
        Draw.circle(gc, x, y, scale, Color.BLACK)
    }
}