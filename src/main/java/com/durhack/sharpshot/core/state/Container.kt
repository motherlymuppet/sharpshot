package com.durhack.sharpshot.core.state

import com.durhack.sharpshot.core.control.CollisionReport
import com.durhack.sharpshot.core.control.TickReport
import com.durhack.sharpshot.core.nodes.INode
import com.durhack.sharpshot.core.nodes.input.AbstractInputNode
import com.durhack.sharpshot.core.nodes.other.HaltNode
import com.durhack.sharpshot.util.IdiotProgrammerException
import com.durhack.sharpshot.util.filterType
import java.math.BigInteger

class Container(var width: Int, var height: Int) {
    val nodes = mutableMapOf<Coordinate, INode>()
    val bullets = mutableListOf<Bullet>()

    fun clearBullets() {
        bullets.clear()
    }

    fun clearNodes() {
        nodes.clear()
    }

    fun resetNodes() {
        nodes.values.forEach(INode::reset)
    }

    fun launch(input: List<BigInteger?>) {
        val inputNodes = nodes.filterType<Coordinate, AbstractInputNode>()
        inputNodes.forEach { (coord, node) ->
            val (dir, value) = node.initialise(input) ?: return@forEach
            val bullet = createBulletFromNode(coord, dir, value)
            bullets.add(bullet)
        }
    }

    private fun createBulletFromNode(coordinate: Coordinate, relativeDirection: Direction, value: BigInteger?): Bullet {
        val node = nodes[coordinate] ?: throw IdiotProgrammerException("There is no node at coordinate $coordinate")
        val absDirection = node.direction + relativeDirection
        return Bullet(coordinate, absDirection, value)
    }

    private fun createBulletsFromNode(coordinate: Coordinate, data: Map<Direction, BigInteger?>) =
            data.map { (relativeDirection, value) ->
                createBulletFromNode(coordinate, relativeDirection, value)
            }

    /**
     * Bullets on top of node -> store
     * Other bullets -> tick
     * stored bullets -> process and output
     * all bullets -> check
     */
    fun tick(): TickReport {
        val halted = tryHalt()
        processBullets()
        val movements = bulletMovement()
        val collisionReport = collide(movements)
        clearCollidedBullets(collisionReport)
        moveBullets()

        val outputs = readOutputs()
        return TickReport(collisionReport, outputs, halted)
    }

    private fun tryHalt(): Boolean {
        val halt = bullets
                .mapNotNull { nodes[it.coordinate] }
                .filterIsInstance<HaltNode>()
                .any()
        if (halt) bullets.clear()
        return halt
    }

    /**
     * Generate the list of movements
     */
    private fun bulletMovement(): Set<BulletMovement> =
            bullets.map { bullet ->
                val coord = bullet.coordinate
                val nextCoord = bullet.nextCoord()
                val movement = Movement(coord, nextCoord)
                return@map BulletMovement(bullet, movement)
            }.toSet()

    /**
     * Partition movements based on whether they cause a collision
     */
    private fun collide(movements: Set<BulletMovement>): CollisionReport {
        val remaining = movements.toMutableSet()
        val swapCollisions = swapCollisions(movements)

        val swapRemove = swapCollisions.map(Collision::a) + swapCollisions.map(Collision::b)
        remaining.removeAll(swapRemove)

        val finalCollisions = finalCollisions(movements)

        val finalRemove = finalCollisions.map(Collision::a) + finalCollisions.map(Collision::b)
        remaining.removeAll(finalRemove)

        return CollisionReport(swapCollisions, finalCollisions, remaining)
    }

    /**
     * Given a list of movements (s1,t1), (s2,t2), (s3,t3)...
     * Find any pairs where (si == tj and sj == ti)
     * I.e. pairs where one is the inverse of the other
     * Return only i for each pair i,j
     */
    private fun swapCollisions(movements: Set<BulletMovement>): Set<Collision> {
        val processing = movements.toMutableList()
        val output = mutableSetOf<Collision>()

        while (processing.isNotEmpty()) {
            val search = processing.first()
            val from = search.movement.from
            val to = search.movement.to
            processing.removeAt(0)

            val found = processing.firstOrNull { (_, movement) ->
                movement.from == to && movement.to == from
            }

            if (found != null) {
                processing.remove(found)
                val collision = Collision(search, found)
                output.add(collision)
            }
        }

        return output
    }

    /**
     * Given a list of movements (s1,t1), (s2,t2), (s3,t3)...
     * Find any pairs where (ti == tj)
     * I.e. pairs where both end in the same square
     * Return both i and j for each pair
     */
    private fun finalCollisions(movements: Set<BulletMovement>): Set<Collision> {
        val processing = movements.toMutableList()
        val output = mutableSetOf<Collision>()

        while (processing.isNotEmpty()) {
            val search = processing.first()
            val to = search.movement.to
            processing.removeAt(0)

            val found = processing.firstOrNull { (_, movement) ->
                movement.to == to
            }

            if (found != null) {
                processing.remove(found)
                val collision = Collision(search, found)
                output.add(collision)
            }
        }

        return output
    }

    /**
     * Remove all bullets that collided
     */
    private fun clearCollidedBullets(collisionReport: CollisionReport) {
        bullets.removeAll(collisionReport.bulletsToRemove)
    }

    /**
     * Move all bullets in the direction they are facing
     */
    private fun moveBullets() {
        bullets.replaceAll(Bullet::increment)
    }

    /**
     * Update any bullets that are on a node
     */
    private fun processBullets() {
        val toProcess =
                bullets.mapNotNull { bullet ->
                    val node = nodes[bullet.coordinate] ?: return@mapNotNull null
                    return@mapNotNull bullet to node
                }

        val newBullets = toProcess.flatMap { (bullet, node) ->
            val data = node.process(bullet)
            createBulletsFromNode(bullet.coordinate, data)
        }

        bullets.clear()
        bullets += newBullets
    }

    private fun readOutputs(): List<BigInteger?> {
        val outputting = bullets.filterNot { isInside(it.coordinate) }
        bullets.removeAll(outputting)
        return outputting.map(Bullet::value)
    }

    private fun isInside(coord: Coordinate) = coord.x in (0 until width) && coord.y in (0 until height)

    fun setTo(oth: Container) {
        clearBullets()
        clearNodes()
        nodes.putAll(oth.nodes)
    }
}