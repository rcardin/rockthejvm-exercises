package catspro

import cats.Monoid

object Folding {

  def map[A, B](list: List[A])(f: A => B): List[B] =
    list.foldLeft(List.empty[B]) {
      case (acc, a) => acc :+ f(a)
    }

  def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
    list.foldLeft(List.empty[B]) {
      case (acc, a) => acc :++ f(a)
    }

  def filter[A](list: List[A])(predicate: A => Boolean): List[A] =
    list.foldLeft(List.empty[A]) {
      case (acc, a) =>
        if (predicate(a))
          acc :+ a
        else
          acc
    }

  def combineAll[A](list: List[A])(implicit monoid: Monoid[A]): A =
    list.foldLeft(monoid.empty) {
      case (acc, a) => monoid.combine(acc, a)
    }

  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)
    println(map(list)(_ + 1))
    println(flatMap(list)(a => List(a + 1)))
    println(filter(list)(_ % 2 == 0))
    import cats.instances.int._
    println(combineAll(list))
  }
}
