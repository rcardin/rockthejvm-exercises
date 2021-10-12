package akka.streams

import akka.actor.ActorSystem
import akka.stream.FlowShape
import akka.stream.scaladsl.{Concat, Flow, GraphDSL}

object OpenGraphs extends App {
  implicit val system: ActorSystem = ActorSystem("OpenGraphs")

  val flow1 = Flow[Int].map(_ + 1)
  val flow2 = Flow[Int].map(_ * 10)

  val flowGraph = Flow.fromGraph {
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val flow1Shape = builder.add(flow1)
      val flow2Shape = builder.add(flow2)

      flow1Shape ~> flow2Shape

      FlowShape(flow1Shape.in, flow2Shape.out)
    }
  }
}
