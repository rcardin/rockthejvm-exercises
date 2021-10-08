package akka.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source

object FirstPrinciples {

  implicit val system: ActorSystem = ActorSystem("FirstPrinciples")

  val names: Source[String, NotUsed] =
    Source(List("Riccardo", "Tobia", "Daniela", "Giuseppe", "Tommaso"))

  def main(args: Array[String]): Unit = {
    names.filter(_.length > 5).take(2).runForeach(println)
  }
}
