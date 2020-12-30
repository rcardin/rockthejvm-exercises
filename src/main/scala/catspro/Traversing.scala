package catspro

import cats.{Applicative, Foldable}

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Traversing {

  import cats.syntax.applicative._
  import cats.syntax.apply._

  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (acc, a) =>
      val monadicValue: F[B] = f(a)
      (acc, monadicValue).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] =
    list.foldLeft(List.empty[A].pure[F]) { (wAcc, elem) =>
      (wAcc, elem).mapN(_ :+ _)
    }

  trait MyTraverse[L[_]] extends Foldable[L] {
    def traverse[F[_]: Applicative, A, B](container: L[A])(f: A => F[B]): F[L[B]]
    def sequence[F[_]: Applicative, A](container: L[F[A]]): F[L[A]] = traverse(container)(identity)

    type Identity[T] = T
    def map[A, B](wa: L[A])(f: A => B): L[B] = traverse[Identity, A, B](wa)(f)
  }

  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3)
    val traversed = listTraverse(list)(a => Future(a + 1))
    traversed.foreach(println)

    val listOptions = List(Option(1), Option(2), Option(3))
    val sequenced = listSequence(listOptions)
    println(sequenced)
  }
}
