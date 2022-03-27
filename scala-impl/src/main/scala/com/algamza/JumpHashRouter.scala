package com.algamza

import scala.util.Random

class JumpHashRouter extends ConsistentHashRouter {
  var bucketCount = 0
  override def addNode(node: Node): Unit = {
    bucketCount += 1
  }

  override def deleteNode(node: Node): Unit = {
    bucketCount -= 1
  }

  override def getRouteNode(key: String): Node = {
    val _key = key.toInt
    val random = new Random(seed = _key)
    var b      = -1
    for (j <- 0 until bucketCount) {
      if (random.nextDouble() < 1.0 / (j + 1)) {
        b = j
      }
    }
    new Node(b)
  }
}
