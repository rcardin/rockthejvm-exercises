package zio

import java.io.IOException
import scala.util.Try

object ZIOErrorHandling extends ZIOAppDefault {

  // Exercise 1.a
  // Implement a version of fromTry, fromEither, fromOption, either, absolve using fold and foldZIO

  def fromTry[A](t: Try[A]): Task[A] =
    t.fold(e => ZIO.fail(e), a => ZIO.succeed(a))

  def fromEither[E, A](e: Either[E, A]): IO[E, A] =
    e.fold(e => ZIO.fail(e), a => ZIO.succeed(a))

  def fromOption[A](o: Option[A]): IO[Option[Nothing], A] =
    o.fold(ZIO.fail(None))(a => ZIO.succeed(a))

  def either[R, E, A](zio: ZIO[R, E, A]): RIO[R, Either[E, A]] =
    zio.fold(e => Left(e), a => Right(a))

  def absolve[E, A](zio: UIO[Either[E, A]]): IO[E, A] =
    zio.flatMap(either => either.fold(e => ZIO.fail(e), a => ZIO.succeed(a)))

  // Exercise 1.b
  // Make this effect fail with a TYPED error
  val aBadFailure: UIO[Int] = ZIO.succeed[Int](throw new RuntimeException("This is bad!"))
  val aBetterFailure: Task[Int] = aBadFailure.unrefine {
    case e => e
  }

  // Exercise 2
  // Transform a ZIO into another ZIO with a narrower exception type
  def ioException[R, A](zio: ZIO[R, Throwable, A]): ZIO[R, IOException, A] =
    zio.refineOrDie {
      case e: IOException => e
    }

  // Exercise 3
  def left[R, E, A, B](zio: ZIO[R, E, Either[A, B]]): ZIO[R, Either[E, A], B] =
    zio.foldZIO(
      e => ZIO.fail(Left(e)),
      either => either match {
        case Left(a) => ZIO.fail(Right(a))
        case Right(b) => ZIO.succeed(b)
      }
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
