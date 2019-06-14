package com.durhack.sharpshot.gui.controls

import com.durhack.sharpshot.gui.container.ContainerController
import com.durhack.sharpshot.util.container
import javafx.beans.property.*
import javafx.geometry.Orientation
import javafx.geometry.Pos
import tornadofx.*

class Playback : View() {
    private val controller: ContainerController by inject()

    private val runningLayer = hbox(8.0, Pos.CENTER) {
        visibleWhen(container.runningProp)

        val animateProp = SimpleBooleanProperty(true)
        val speedSettingProp = SimpleIntegerProperty(1)
        label("Speed") {
            visibleWhen(animateProp)
        }
        slider(1, 5, orientation = Orientation.HORIZONTAL){
            blockIncrement = 1.0
            majorTickUnit = 1.0
            isSnapToTicks = true
            isShowTickMarks = true
            minorTickCount = 0
            visibleWhen(animateProp)
            enableWhen(controller.idleProp)
            bind(speedSettingProp)
        }
        checkbox("Animate", animateProp){
            enableWhen(controller.idleProp)
        }
        button("Step") {
            enableWhen(controller.idleProp)
            action {
                runLater {
                    if(animateProp.get()){
                        val speedSetting = speedSettingProp.get()
                        val lengthMs = 1000/(speedSetting * speedSetting * speedSetting)
                        controller.animatedTick(lengthMs.toLong())
                    }
                    else{
                        controller.quickTick()
                    }
                }
            }
        }
        button("Reset") {
            enableWhen(controller.idleProp)
            action {
                runLater {
                    controller.reset()
                }
            }
        }
        stackpane {
            button("Play") {
                visibleWhen(controller.playingProp.not())
                enableWhen(controller.idleProp)
                action {
                    runLater {
                        val speedSetting = speedSettingProp.get()
                        val lengthMs = 1000/(speedSetting * speedSetting * speedSetting)
                        controller.play(lengthMs.toLong())
                    }
                }
            }
            button("Stop"){
                visibleWhen(controller.playingProp)
                enableWhen(controller.stoppingProp.not())
                action {
                    runLater {
                        controller.stopPlaying()
                    }
                }
            }
        }
        button("Fast-Forward") {
            enableWhen(controller.idleProp)
            action {
                runLater {
                    controller.simulate(1000*1000)
                }
            }
        }
    }

    private val stoppedLayer = hbox(8.0, Pos.CENTER) {
        alignment = Pos.CENTER
        visibleWhen(container.runningProp.not())

        val inputsProp = SimpleStringProperty("")
        button("Start Test") {
            action {
                val inputString = inputsProp.get()
                val inputs = inputString.split(" ").filter { it.isNotBlank() }.map { it.toBigInteger() }
                controller.start(inputs)
            }
        }
        textfield(inputsProp) {
            promptText = "Space-separated inputs"

            val regex = "(-?\\d+\\s?)*".toRegex()
            filterInput { (it.text ?: return@filterInput false).matches(regex) }
        }
    }

    override val root = stackpane {
        add(runningLayer)
        add(stoppedLayer)
    }
}