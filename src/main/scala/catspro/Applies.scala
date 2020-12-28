package catspro

import cats.{Functor, Semigroupal}

object Applies {

  trait MyApply[W[_]] extends Functor[W] with Semigroupal[W] {
    def ap[B, T](wf: W[B => T])(wb: W[B]): W[T]

    override def product[A, B](fa: W[A], fb: W[B]): W[(A, B)] = {
      val functionWrapper: W[B => (A, B)] = map(fa)(a => (b: B) => (a, b))
      ap(functionWrapper)(fb)
    }

    def mapN[A, B, C](tuple: (W[A], W[B]))(f: (A, B) => C): W[C] = {
      val wrappedTuple: W[(A, B)] = product(tuple._1, tuple._2)
      map(wrappedTuple) {
        case (a, b) => f(a, b)
      }
    }
  }

  def main(args: Array[String]): Unit = {

  }
}
