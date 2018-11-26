package com.durhack.sharpshot.nodes.input

import com.durhack.sharpshot.gui.shapes.Triangle
import com.durhack.sharpshot.gui.util.getNumberInput
import com.durhack.sharpshot.logic.Bullet
import com.durhack.sharpshot.logic.Direction
import com.durhack.sharpshot.nodes.INode
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import javafx.scene.paint.Color
import java.math.BigInteger

class InNode(private val index: Int?) : AbstractInputNode() {
    private var input: BigInteger? = null

    override fun input(inputs: List<BigInteger?>): Map<Direction, BigInteger?> {
        input = if (index == null) null else inputs.getOrNull(index)
        return mapOf(Direction.UP to input)
    }

    override fun run(bullet: Bullet) = mapOf(bullet.direction to bullet.value, Direction.UP to input)

    override fun graphic() = Triangle(rotation,
                                      Color.web("#FFFF00"),
                                      "IN${index ?: ""}")

    override fun reset() {}

    override val type = "input"

    override val tooltip = "Provides Input at program start and every time a bullet passes through"
    override val factory = {
        val index = getNumberInput("Enter Input Index",
                                   "Blank to shoot empty bullet at start\nArguments are 0-indexed")
        if (index.isPresent) {
            val string = index.get()
            if (string.isBlank()) {
                InNode(null)
            }
            else {
                InNode(string.toInt())
            }
        }
        else {
            null
        }
    }

    override fun toJson(): JsonElement {
        val json = super.toJson().asJsonObject
        json.addProperty("index", index?.toString() ?: "null")
        return json
    }

    override val jsonFactory: (JsonObject) -> INode = { json ->
        val indexString = json["index"].asString
        val index = when (indexString) {
            "null" -> null
            else -> indexString.toInt()
        }

        val node = InNode(index)
        val rotation = json["rotation"].asInt
        node.rotation = Direction.ofQuarters(rotation)
        node
    }
}