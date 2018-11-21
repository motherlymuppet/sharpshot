package com.durhack.sharpshot.serialisation

import com.durhack.sharpshot.logic.Container
import com.durhack.sharpshot.logic.Coordinate
import com.durhack.sharpshot.nodes.NodeRegistry
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

internal object Serialiser {
    fun saveContainer(container: Container): String {
        val nodes = JsonArray()
        container.nodes.forEach { coordinate, node ->
            val nodeJson = JsonObject()
            nodeJson.add("coordinate", coordinate.toJson())
            nodeJson.add("node", node.toJson())
            nodes.add(nodeJson)
        }

        val containerJson = JsonObject()
        containerJson.addProperty("width", container.width)
        containerJson.addProperty("height", container.height)
        containerJson.add("nodes", nodes)

        val gson = GsonBuilder().create()
        return gson.toJson(containerJson)
    }

    fun loadContainer(jsonString: String): Container {
        val json = JsonParser().parse(jsonString).asJsonObject

        val width = json["width"].asInt
        val height = json["height"].asInt
        val container = Container(width, height)

        val nodesJson = json["nodes"].asJsonArray
        nodesJson.forEach { jsonElement ->
            val jsonObject = jsonElement.asJsonObject
            val coordinateJson = jsonObject["coordinate"].asJsonObject
            val nodeJson = jsonObject["node"].asJsonObject

            val coordinate = Coordinate.fromJson(coordinateJson)
            val node = NodeRegistry.fromJson(nodeJson)
            container.nodes[coordinate] = node
        }
        return container
    }
}
