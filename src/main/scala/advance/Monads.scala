package advance

object Monads {

  // Right identity
  // Monad.unit(x).flatMap(f) = f(x)
  // Left identity
  // x.flatMap(y => Monad(y)) = x
  // Associativity
  // x.flatMap(f).flatMap(g) = o.flatMap(x => f(x).flapMap(g))

  // Exercise 1
  // Implement a Lazy[T] monad = computation which will only be executed when it's needed.

  class Lazy[A](value: => A) {
    private lazy val internal: A = value

    def get: A = internal

    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internal)

    def map[B](f: A => B): Lazy[B] = flatMap(x => Lazy(f(x)))

    def flatten(m: Lazy[Lazy[A]]): Lazy[A] = m.flatMap(x => x)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value) //Unit
  }

  def main(args: Array[String]): Unit = {
    val lazyInt: Lazy[Int] = Lazy {
      println("The response to everything is 42")
      42
    }
    val lazyString42: Lazy[String] = lazyInt.flatMap { intValue =>
      Lazy(intValue.toString)
    }

    val result: Lazy[Int] = for {
      first <- Lazy(1)
      second <- Lazy(2)
      third <- Lazy(3)
    } yield first + second + third

    val anotherResult: Lazy[Int] =
      Lazy(1).flatMap { first =>
        Lazy(2).flatMap { second =>
          Lazy(3).map { third =>
            first + second + third
          }
        }
      }

    // Lazy(x).flatMap(f) == f(x)
    // Lazy(x).flatMap(y => Lazy(y)) == Lazy(x)
    // Lazy(x).flapMap(f).flatMap(g) == f(x).flatMap(g)
  }
}
