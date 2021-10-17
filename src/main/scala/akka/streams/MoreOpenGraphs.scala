package akka.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ClosedShape, Graph, UniformFanInShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}

object MoreOpenGraphs extends App {
  implicit val system: ActorSystem = ActorSystem("MoreOpenGraphs")

  val source1 = Source(1 to 10)
  val source2 = Source((1 to 10).map(_ => 5))
  val source3 = Source((1 to 10).reverse)

  val maxSink = Sink.foreach[Int](x => println(s"Max is $x"))

  val maxStaticGraph: Graph[UniformFanInShape[Int, Int], NotUsed] = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val zip1 = builder.add(ZipWith[Int, Int, Int]((a, b) => Math.max(a, b)))
    val zip2 = builder.add(ZipWith[Int, Int, Int]((a, b) => Math.max(a, b)))

    zip1.out ~> zip2.in0

    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  val maxRunnableGraph = RunnableGraph.fromGraph {
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val max3 = builder.add(maxStaticGraph)

      source1 ~> max3.in(0)
      source2 ~> max3.in(1)
      source3 ~> max3.in(2)
      max3.out ~> maxSink

      ClosedShape
    }
  }

  maxRunnableGraph.run()
}
