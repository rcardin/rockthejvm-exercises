object Monads {

  // Exercise 1
  // Implement a Lazy[T] monad = computation which will only be executed when it's needed.

  class Lazy[+A](value: => A) {
    def flatMap[B](f: A => Lazy[B]): Lazy[B] = {
      f(value)
    }
  }
}
