package catspro

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object MonadTransformers {

  import cats.data.EitherT
  import cats.instances.future._
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  val bandwidths = Map(
    "server1.rockthejvm.com" -> 50,
    "server2.rockthejvm.com" -> 300,
    "server3.rockthejvm.com" -> 170,
  )
  type AsyncResponse[T] = EitherT[Future, String, T]

  def getBandwidth(server: String): AsyncResponse[Int] = bandwidths.get(server) match {
    case None => EitherT.left(Future(s"Server $server unreachable"))
    case Some(b) => EitherT.right(Future(b))
  }

  def canWithstandSurge(s1: String, s2: String): AsyncResponse[Boolean] = for {
    b1 <- getBandwidth(s1)
    b2 <- getBandwidth(s2)
  } yield b1 + b2 > 250

  def generateTrafficSpikeReport(s1: String, s2: String): AsyncResponse[String] =
    canWithstandSurge(s1, s2).transform {
      case Left(reason) => Left(reason)
      case Right(true) => Right("All green")
      case Right(false) => Left("The spike is too big!")
    }
}
