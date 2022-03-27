package com.algamza

object Application {

	def main(args: Array[String]): Unit = {
		val router: ConsistentHashRouter = args.headOption match {
			case Some("1") =>
				new HashRingRouter()
			case Some("2") | None =>
				new ImprovedHashRingRouter(3)
			case Some("3") =>
				new JumpHashRouter()
			case unknown =>
				throw new IllegalArgumentException(s"unknown value: $unknown")
		}

		val nodes = (1 to 3).map(new Node(_))
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
		}.groupBy(node => node).map(tuple => (tuple._1, tuple._2.size))
			.toSeq.sortBy(_._1.toString)
			.foreach {
				case (node, count) => println(s"$node => $count")
			}
	}
}