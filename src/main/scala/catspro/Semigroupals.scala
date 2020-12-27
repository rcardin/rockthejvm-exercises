package catspro

object Semigroupals {

  import cats.Semigroupal
  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._
  def productWithMonad[F[_], A, B](fa: F[A], fb: F[B])(implicit monad: Monad[F]): F[(A, B)] =
    for {
      a <- fa
      b <- fb
    } yield (a, b)

  val myListSemigroupal: Semigroupal[List] = new Semigroupal[List] {
    override def product[A, B](fa: List[A], fb: List[B]): List[(A, B)] =
      fa.zip(fb)
  }

  def main(args: Array[String]): Unit = {
    import cats.instances.list._
    val product = productWithMonad(List(1, 2), List("a", "b"))
    println(product)
    val myProduct = myListSemigroupal.product(List(1, 2), List("a", "b"))
    println(myProduct)
  }
}
