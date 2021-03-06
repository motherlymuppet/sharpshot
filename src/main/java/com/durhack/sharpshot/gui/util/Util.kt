package com.durhack.sharpshot.gui.util

import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.ObservableValueBase
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import tornadofx.*

fun ui(function: () -> Unit) = Platform.runLater(function)

fun Node.addClickHandler(func: (MouseEvent) -> Unit){
    addEventHandler(MouseEvent.MOUSE_CLICKED){event ->
        if (event.isStillSincePress){
            func(event)
        }
    }
}

fun Node.addClickFilter(func: (MouseEvent) -> Unit){
    addEventFilter(MouseEvent.MOUSE_CLICKED){event ->
        if (event.isStillSincePress){
            func(event)
        }
    }
}

fun <T> ObservableValue<T>.ui() = ObservableUIValue(this)

fun <T> ObservableList<T>.observableValue() = object: ObservableValueBase<List<T>>(){
    init {
        this@observableValue.addListener(InvalidationListener{
            fireValueChangedEvent()
        })
    }

    override fun getValue() = synchronized(this){toList()}
}

fun <T> ObservableList<T>.ui() = observableValue().ui()

class ObservableUIValue<T>(private val underlying: ObservableValue<T>) : ObservableValueBase<T>() {
    init {
        underlying.addListener(InvalidationListener{
            ui { fireValueChangedEvent() }
        })
    }

    override fun getValue(): T = underlying.value
}

fun ObservableValue<Boolean>.not() = booleanBinding { it?.not() ?: true }