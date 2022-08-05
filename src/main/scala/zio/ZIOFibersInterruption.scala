package zio

object ZIOFibersInterruption extends ZIOAppDefault {

  // Exercise 1
  // Implement a timeout function
  def timeout[R, E, A](zio: ZIO[R, E, A], time: Duration): ZIO[R, E, A] = for {
    fib <- zio.fork
    _ <- fib.interrupt.delay(time).fork
    result <- fib.join
  } yield result

  // Exercise 2
  // Implement a timeout function that always returns something
  def timeout_V2[R, E, A](zio: ZIO[R, E, A], time: Duration): ZIO[R, E, Option[A]] = for {
    fib <- zio.fork
    _ <- fib.interrupt.delay(time).fork
    result <- fib.join.foldCauseZIO(
      cause => if (cause.isInterrupted) ZIO.succeed(None) else ZIO.failCause(cause),
      a => ZIO.succeed(Some(a))
    )
  } yield result

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
