package akka.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MaterializingStreams {

  implicit val system: ActorSystem = ActorSystem("MaterializingStreams")

  val nameSource: Source[String, NotUsed] = Source(List("Alice", "Bob", "Charlie", "Martin"))
  val lastValueSink: Sink[String, Future[String]] = Sink.last[String]

  val nameGraph: Future[String] = nameSource.toMat(lastValueSink)(Keep.right).run()

  val sentencesSource: Source[String, NotUsed] = Source(
    List(
      "Lorem ipsum dolor sit amet",
      "consectetur adipiscing elit",
      "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    )
  )

  val tokenizer: Flow[String, Array[String], NotUsed] = Flow[String].map(_.split(" "))
  val numberOfWordsOfASingleSentence: Flow[Array[String], Int, NotUsed] =
    Flow[Array[String]].map(_.length)

  val sum: Sink[Int, Future[Int]] = Sink.reduce[Int]((a, b) => a + b)

  val wordsCount: Future[Int] = sentencesSource
    .viaMat(tokenizer)(Keep.right)
    .viaMat(numberOfWordsOfASingleSentence)(Keep.right)
    .toMat(sum)(Keep.right)
    .run()

  def main(args: Array[String]): Unit = {
    import system.dispatcher
    nameGraph.onComplete {
      case Success(value) => println(s"The last value is $value")
      case Failure(exception) => println(s"The stream fails with error: $exception")
    }

    wordsCount.onComplete {
      case Success(value) => println(s"The word count is $value")
      case Failure(exception) => println(s"The word count process fails with error: $exception")
    }
  }
}
