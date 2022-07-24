package zio

import scala.util.Try

object ZIOErrorHandling extends ZIOAppDefault {

  // Exercise 1
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

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
