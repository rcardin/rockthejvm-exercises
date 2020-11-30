package catspro

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object Monads {

  import cats.Monad
  import cats.instances.future._

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  val futureMonad: Monad[Future] = Monad[Future]
  val future: Future[Int] = futureMonad.pure(4)
  val mappedFuture: Future[Int] = futureMonad.flatMap(future)(x => Future(x + 1))

  trait MyMonad[M[_]] {
    def pure[A](value: A): M[A]

    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    def map[A, B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(x => pure(f(x)))
  }

  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A, B)] =
    monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def getPairsForComprehension[M[_] : Monad[M], A, B](ma: M[A], mb: M[B]): M[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  def main(args: Array[String]): Unit = {
    println(future)
    println(mappedFuture)
  }
}
