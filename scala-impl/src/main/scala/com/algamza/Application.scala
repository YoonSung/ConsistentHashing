package com.algamza

object Application {

	def main(args: Array[String]): Unit = {
		val router: ConsistentHashRouter = args.headOption match {
			case Some("1") | None =>
				new HashRingRouter()
			case Some("2") =>
				new HashRingRouter()
			case Some("3") =>
				new HashRingRouter()
			case unknown =>
				throw new IllegalArgumentException(s"unknown value: $unknown")
		}

		val nodes = (1 to 3).map(_.toString).map(NodeImpl)
		val keys = (150 to 300).map(_.toString).take(100)

		nodes.foreach { node =>
			router.addNode(node)
		}
		show(router, keys)
		val willDeleteNode = nodes.last
		println(s"\n==> delete node: ${willDeleteNode.key()}\n")
		router.deleteNode(willDeleteNode)
		show(router, keys)
	}

	private def show(router: ConsistentHashRouter, keys: Seq[String]): Unit = {
		println("------------------")
		keys.map { key =>
			router.getRouteNode(key)
		}.groupBy(_.key()).map(tuple => (tuple._1, tuple._2.size))
			.toSeq.sortBy(_._1)
			.foreach {
				case (node, count) => println(s"Node_$node => $count")
			}
	}
}