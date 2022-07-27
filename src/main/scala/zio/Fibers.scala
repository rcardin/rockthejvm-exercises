package zio

object Fibers extends ZIOAppDefault {

  // Exercise 1
  // Zip two fibers without using then zip combinator
  def zipFibers[E, A, B](fa: Fiber[E, A], fb: Fiber[E, B]): UIO[Fiber[E, (A, B)]] = {
    val finalEffect = for {
      a <- fa.join
      b <- fb.join
    } yield (a, b)
    finalEffect.fork
  }

  // Exercise 2
  // Same thing with orElse
  def chainFibers[E, A](fiber1: Fiber[E, A], fiber2: Fiber[E, A]): UIO[Fiber[E, A]] = {
    fiber1.join.orElse(fiber2.join).fork
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
