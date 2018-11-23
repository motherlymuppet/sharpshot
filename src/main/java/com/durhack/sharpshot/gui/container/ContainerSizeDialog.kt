package com.durhack.sharpshot.gui.container

import com.durhack.sharpshot.gui.util.Dialog
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class ContainerSizeDialog : Dialog<Pair<Int, Int>>("Container Size") {
    private val gridWidthProp = SimpleIntegerProperty(20)
    private val gridHeightProp = SimpleIntegerProperty(15)

    override fun getValue(): Pair<Int, Int>? {
        val width = gridWidthProp.get()
        val height = gridHeightProp.get()
        return width to height
    }

    override fun getDialogContents() = form {
        fieldset("Container Size") {
            field("Width") {
                textfield(gridWidthProp) {
                    promptText = "Enter grid width in squares"
                }
            }
            field("Height") {
                textfield(gridHeightProp) {
                    promptText = "Enter grid height in squares"
                }
            }
        }
    }
}