package akka.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Balance, Broadcast, GraphDSL, Merge, RunnableGraph, Sink, Source}

import scala.language.postfixOps

object GraphDSLBasics extends App {

  implicit val system: ActorSystem = ActorSystem("GraphDSLBasics")

  val source = Source(1 to 1000)
  val sink1 = Sink.foreach[Int] { n =>
    println(s"Sink-1 received number $n")
  }

  val sink2 = Sink.foreach[Int] { n =>
    println(s"Sink-2 received number $n")
  }

  val twoSinksGraph = RunnableGraph.fromGraph {
    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      val broadcast = builder.add(Broadcast[Int](2))

      source ~> broadcast
      broadcast.out(0) ~> sink1
      broadcast.out(1) ~> sink2

      ClosedShape
    }
  }

  // twoSinksGraph.run()

  import scala.concurrent.duration._
  val fastSource = source.throttle(5, 1 second)

  val slowSource = source.throttle(1, 1 second)

  val mergeAndBalanceGraph = RunnableGraph.fromGraph {
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val merge = builder.add(Merge[Int](2))
      val balance = builder.add(Balance[Int](2))

      fastSource ~> merge
      slowSource ~> merge
      merge ~> balance
      balance ~> sink1
      balance ~> sink2

      ClosedShape
    }
  }

  mergeAndBalanceGraph.run()
}
