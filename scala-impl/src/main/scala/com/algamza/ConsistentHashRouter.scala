package com.algamza

class Node(val name: String){
  def key(): String = name
  override def toString: String = s"Node_$name"
}

trait ConsistentHashRouter {
  def addNode(node: Node): Unit
  def deleteNode(node: Node): Unit
  def getRouteNode(key: String): Node
}
