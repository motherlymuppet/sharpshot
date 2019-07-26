package com.durhack.sharpshot.gui.input.layers

import com.durhack.sharpshot.core.state.Coordinate
import com.durhack.sharpshot.gui.container.ContainerView
import com.durhack.sharpshot.gui.util.addClickHandler
import com.durhack.sharpshot.gui.util.coord
import com.durhack.sharpshot.util.globalContainer
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import tornadofx.*

class SingleNodeEdit : View() {
    private val containerView: ContainerView by inject()

    private var mouseX = 0.0
    private var mouseY = 0.0
    private val hoverCoord: Coordinate? get() = containerView.getCoord(mouseX, mouseY).validInGlobalContainer

    override val root = stackpane {
        id = "Single Node Edit"
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS

        addClickHandler {
            if (it.button == MouseButton.PRIMARY) {
                handleRotate(it)
                it.consume()
            }
            else if (it.button == MouseButton.SECONDARY) {
                handleDelete(it)
                it.consume()
            }
        }

        setOnMouseMoved {
            mouseX = it.x
            mouseY = it.y
        }

        addEventHandler(KeyEvent.KEY_PRESSED) {
            when {
                it.code == KeyCode.Q -> rotateHoveredACW()
                it.code == KeyCode.E -> rotateHoveredCW()
                it.code in listOf(KeyCode.DELETE, KeyCode.BACK_SPACE, KeyCode.R) -> deleteHovered()
            }
        }
    }

    private fun handleRotate(event: MouseEvent) {
        event.consume()

        val coord = event.coord ?: return
        val node = globalContainer.nodes[coord] ?: return
        val newNode = node.clockwise
        globalContainer.nodes[coord] = newNode

        containerView.render()
    }

    private fun handleDelete(event: MouseEvent) {
        event.consume()

        val coord = event.coord ?: return
        delete(coord)
    }

    private fun rotateHoveredCW() {
        val coord = hoverCoord ?: return
        val node = globalContainer.nodes[coord] ?: return
        val newNode = node.clockwise
        globalContainer.nodes[coord] = newNode

        containerView.render()
    }

    private fun rotateHoveredACW() {
        val coord = hoverCoord ?: return
        val node = globalContainer.nodes[coord] ?: return
        val newNode = node.antiClockwise
        globalContainer.nodes[coord] = newNode

        containerView.render()
    }

    private fun deleteHovered() {
        delete(hoverCoord ?: return)
    }

    private fun delete(coord: Coordinate) {
        globalContainer.nodes.remove(coord)
        containerView.render()
    }
}