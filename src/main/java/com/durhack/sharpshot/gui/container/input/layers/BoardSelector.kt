package com.durhack.sharpshot.gui.container.input.layers

import com.durhack.sharpshot.gui.container.ContainerView
import com.durhack.sharpshot.gui.container.input.layers.popovers.DragBox
import com.durhack.sharpshot.gui.container.input.layers.popovers.SelectionMenu
import com.durhack.sharpshot.gui.util.FractionalCoordinate
import com.durhack.sharpshot.gui.util.addClickHandler
import com.durhack.sharpshot.gui.util.point
import javafx.scene.input.MouseButton
import javafx.scene.layout.Priority
import tornadofx.*

class BoardSelector : View() {
    private val containerView: ContainerView by inject()
    private val selectionMenu: SelectionMenu by inject()
    private val dragBox: DragBox by inject()

    private var dragStart: FractionalCoordinate? = null
    private var dragEnd: FractionalCoordinate? = null
    var dragging = false
        private set

    override val root = stackpane {
        id = "Board Selector"
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS

        addClickHandler {
            if (selectionMenu.isSelected && it.button == MouseButton.SECONDARY) {
                selectionMenu.hide()
            }
        }

        setOnMousePressed {
            if (it.button == MouseButton.PRIMARY) {
                val point = it.point
                val coord = containerView.getCoord(point)
                selectionMenu.hide()
                dragStart = coord
            }
        }

        setOnMouseReleased {
            if (it.button == MouseButton.PRIMARY) {
                val start = dragStart
                val end = dragEnd

                if (start != null && end != null) {
                    selectionMenu.show(start, end)
                    dragBox.hide()
                }

                dragStart = null
                dragEnd = null
                dragging = false
            }
        }

        setOnMouseDragged {
            val start = dragStart ?: return@setOnMouseDragged

            if (it.button == MouseButton.PRIMARY) {
                dragging = true
                val point = it.point
                dragEnd = containerView.getCoord(point)
                dragBox.show(start, point)
            }
        }
    }
}