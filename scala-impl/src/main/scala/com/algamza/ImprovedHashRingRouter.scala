package com.algamza

import scala.collection.mutable
import scala.util.hashing.MurmurHash3

class VirtualNodeImpl(node: Int, virtualNodeIndex: Int) extends Node(node){
  override def key(): String = s"${node}__$virtualNodeIndex"

  override def toString: String = s"${super.toString()} (Virtual Idx: $virtualNodeIndex)"
}

class ImprovedHashRingRouter(virtualCountPerNode: Int) extends ConsistentHashRouter {
  private val ring: java.util.SortedMap[Int, VirtualNodeImpl] = new java.util.TreeMap[Int, VirtualNodeImpl]()
  private val virtualNodesByNodeName: mutable.Map[String, Seq[VirtualNodeImpl]] = mutable.Map.empty

  override def addNode(node: Node): Unit = {
    val virtualNodeList = (1 to virtualCountPerNode).map { idx =>
      new VirtualNodeImpl(node = node.node, virtualNodeIndex = idx)
    }
    virtualNodeList.foreach(virtualNode => ring.put(hash(virtualNode.key()), virtualNode))

    // 삭제를 위해 별도 메모리에 저장
    virtualNodesByNodeName.put(node.key(), virtualNodeList)
  }

  override def deleteNode(node: Node): Unit = {
    val keys = virtualNodesByNodeName(node.key()).map(node => hash(node.key()))
    keys.foreach(ring.remove)
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

