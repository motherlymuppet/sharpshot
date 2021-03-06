package com.durhack.sharpshot.gui.shapes

import com.durhack.sharpshot.core.state.Direction
import javafx.geometry.Point2D
import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Rotate

private val rightRotation = Rotate(Direction.RIGHT.degrees, 0.5, 0.5)
private val downRotation = Rotate(Direction.DOWN.degrees, 0.5, 0.5)
private val leftRotation = Rotate(Direction.LEFT.degrees, 0.5, 0.5)

object Draw {


    fun square(gc: GraphicsContext, x: Double, y: Double, scale: Int, color: Color) {
        gc.fill = color
        gc.stroke = null
        gc.fillRect(x, y, scale.toDouble(), scale.toDouble())
    }

    fun circle(gc: GraphicsContext, x: Double, y: Double, scale: Int, color: Color) {
        gc.fill = color
        gc.stroke = null
        gc.fillOval(x, y, scale.toDouble(), scale.toDouble())
    }

    private val diamond = DrawablePolygon(listOf(
            Point2D(0.5, 0.0),
            Point2D(1.0, 0.5),
            Point2D(0.5, 1.0),
            Point2D(0.0, 0.5)
                                                ))

    fun diamond(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        diamond.draw(gc, direction, x, y, scale, color)
    }

    private val triangle = DrawablePolygon(listOf(
            Point2D(0.0, 1.0),
            Point2D(0.5, 0.0),
            Point2D(1.0, 1.0)
                                                 ))

    fun triangle(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        triangle.draw(gc, direction, x, y, scale, color)
    }

    private val arrow = DrawablePolygon(listOf(
            Point2D(0.0, 0.5),
            Point2D(0.5, 0.0),
            Point2D(1.0, 0.5),
            Point2D(0.75, 0.5),
            Point2D(0.75, 1.0),
            Point2D(0.25, 1.0),
            Point2D(0.25, 0.5)
                                              ))

    fun arrow(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        arrow.draw(gc, direction, x, y, scale, color)
    }

    private val doubleArrow = DrawablePolygon(listOf(
            Point2D(0.5, 0.0),
            Point2D(0.75, 0.25),
            Point2D(0.625, 0.25),
            Point2D(0.625, 0.75),
            Point2D(0.75, 0.75),
            Point2D(0.5, 1.0),
            Point2D(0.25, 0.75),
            Point2D(0.375, 0.75),
            Point2D(0.375, 0.25),
            Point2D(0.25, 0.25)
                                              ))

    fun doubleArrow(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        doubleArrow.draw(gc, direction, x, y, scale, color)
    }

    private val rightAngleTriangle = DrawablePolygon(listOf(
            Point2D(0.0, 0.0),
            Point2D(1.0, 0.0),
            Point2D(0.0, 1.0)
    ))

    fun rightAngleTriangle(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        rightAngleTriangle.draw(gc, direction, x, y, scale, color)
    }

    private val bullet = DrawablePolygon(listOf(
            Point2D(0.5, 0.0),
            Point2D(1.0, 0.5),
            Point2D(1.0, 1.0),
            Point2D(0.0, 1.0),
            Point2D(0.0, 0.5)
                                                           ))

    fun bullet(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        bullet.draw(gc, direction, x, y, scale, color)
    }

    private val simpleArrow = DrawablePolygon(listOf(
            Point2D(0.0, 1.0),
            Point2D(0.5, 0.0),
            Point2D(1.0, 1.0)
                                                    ))

    fun rotate(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        gc.stroke = color
        gc.lineWidth = scale / 20.0
        gc.fill = color

        val arrowScale = scale / 4
        val offsetForFar = scale - arrowScale.toDouble()
        val offsetForMiddle = (offsetForFar) / 2.0
        simpleArrow.draw(gc, Direction.RIGHT, x + offsetForMiddle, y, arrowScale, color)
        simpleArrow.draw(gc, Direction.DOWN, x + offsetForFar, y + offsetForMiddle, arrowScale, color)
        simpleArrow.draw(gc, Direction.LEFT, x + offsetForMiddle, y + offsetForFar, arrowScale, color)
        simpleArrow.draw(gc, Direction.UP, x, y + offsetForMiddle, arrowScale, color)
        val halfArrowScale = arrowScale / 2.0
        gc.strokeOval(x + halfArrowScale, y + halfArrowScale, offsetForFar, offsetForFar)
    }

    fun rotateAnti(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        gc.stroke = color
        gc.lineWidth = scale / 20.0
        gc.fill = color

        val arrowScale = scale / 4
        val offsetForFar = scale - arrowScale.toDouble()
        val offsetForMiddle = (offsetForFar) / 2.0
        simpleArrow.draw(gc, Direction.LEFT, x + offsetForMiddle, y, arrowScale, color)
        simpleArrow.draw(gc, Direction.UP, x + offsetForFar, y + offsetForMiddle, arrowScale, color)
        simpleArrow.draw(gc, Direction.RIGHT, x + offsetForMiddle, y + offsetForFar, arrowScale, color)
        simpleArrow.draw(gc, Direction.DOWN, x, y + offsetForMiddle, arrowScale, color)
        val halfArrowScale = arrowScale / 2.0
        gc.strokeOval(x + halfArrowScale, y + halfArrowScale, offsetForFar, offsetForFar)
    }

    fun text(gc: GraphicsContext, text: String, x: Double, y: Double, scale: Int, color: Color = Color.BLACK) {
        gc.textAlign = TextAlignment.CENTER
        gc.textBaseline = VPos.CENTER
        gc.fill = color
        gc.stroke = null
        gc.font = Font(scale * 0.5)
        val offset = scale / 2
        gc.fillText(text, x + offset, y + offset, scale * 0.8)

    }
}

class DrawablePolygon(private val upPoints: List<Point2D>) {
    private val rightPoints = upPoints.map(rightRotation::transform)
    private val downPoints = upPoints.map(downRotation::transform)
    private val leftPoints = upPoints.map(leftRotation::transform)
    private val n = upPoints.size

    fun draw(gc: GraphicsContext, direction: Direction, x: Double, y: Double, scale: Int, color: Color) {
        gc.fill = color
        gc.stroke = null

        val points = when (direction) {
            Direction.UP -> upPoints
            Direction.RIGHT -> rightPoints
            Direction.DOWN -> downPoints
            Direction.LEFT -> leftPoints
        }

        val xs = points.map { x + it.x * scale }.toDoubleArray()
        val ys = points.map { y + it.y * scale }.toDoubleArray()
        gc.fillPolygon(xs, ys, n)
    }
}