package com.algamza

class Node(val node: Int){
  def key(): String = node.toString
  override def toString: String = s"Node_$node"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Node]

  override def equals(other: Any): Boolean = other match {
    case that: Node =>
      (that canEqual this) &&
        node == that.node
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(node)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

trait ConsistentHashRouter {
  def addNode(node: Node): Unit
  def deleteNode(node: Node): Unit
  def getRouteNode(key: String): Node
}
