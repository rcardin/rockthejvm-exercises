package catspro

object MonadsPlayground {
  case class Connection(host: String, port: String)
  val config = Map(
    "host" -> "localhost",
    "port" -> "4848"
  )

  import cats.Monad
  import cats.instances.option._
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  trait HttpService[M[_]] {
    def getConnection(cfg: Map[String, String]): M[Connection]
    def issueRequest(connection: Connection, payload: String): M[String]
  }

  def getResponse[M[_]: Monad](service: HttpService[M], payload: String): M[String] = for {
    conn <- service.getConnection(config)
    response <- service.issueRequest(conn, payload)
  } yield response

  object HttpServiceUsingOption extends HttpService[Option] {
    val optionMonad: Monad[Option] = Monad[Option]
    override def getConnection(cfg: Map[String, String]): Option[Connection] =
      for {
        host <- cfg.get("host")
        port <- cfg.get("port")
      } yield Connection(host, port)

    override def issueRequest(connection: Connection, payload: String): Option[String] =
      if (payload.length < 20)
        Some(s"request ($payload) has been accepted")
      else
        None
  }
  type ErrorOr[T] = Either[Throwable, T]
  object HttpServiceUsingErrorOr extends HttpService[ErrorOr] {
    override def getConnection(cfg: Map[String, String]): ErrorOr[Connection] =
      if (cfg.contains("host") && cfg.contains("port")) {
        Right(Connection(cfg("host"), cfg("port")))
      } else {
        Left(new RuntimeException("Either host or port information missing"))
      }

    override def issueRequest(connection: Connection, payload: String): ErrorOr[String] =
      if (payload.length < 20)
        Right(s"request ($payload) has been accepted")
      else
        Left(new RuntimeException("Payload is too large"))
  }

  def main(args: Array[String]): Unit = {
    val response = for {
      conn <- HttpServiceUsingErrorOr.getConnection(config)
      response <- HttpServiceUsingErrorOr.issueRequest(conn, "This is my payload")
    } yield response
    println(response)

    val optionResponse = getResponse(HttpServiceUsingOption, "My payload")
    println(optionResponse)
  }
}
