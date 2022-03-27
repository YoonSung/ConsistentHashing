package com.algamza

import scala.util.hashing.MurmurHash3

class HashRingRouter() extends ConsistentHashRouter {
  private val ring: java.util.SortedMap[Int, Node] = new java.util.TreeMap[Int, Node]()

  override def addNode(node: Node): Unit = {
    ring.put(hash(node.key()), node)
  }

  override def deleteNode(node: Node): Unit = {
    ring.remove(hash(node.key()))
  }

  override def getRouteNode(key: String): Node = {
    val tailMap = ring.tailMap(hash(key))
    val nodeKey = if (!tailMap.isEmpty) {
      tailMap.firstKey()
    } else {
      ring.firstKey()
    }
    ring.get(nodeKey)
  }

  private def hash(key: String): Int = {
    MurmurHash3.stringHash(key, key.hashCode)
  }
}
