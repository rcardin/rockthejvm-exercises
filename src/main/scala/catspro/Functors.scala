package catspro

object Functors {

  // binary tree
  trait Tree[+T]
  case class Leaf[+T](value: T) extends Tree[T]
  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

  import cats.Functor
  implicit object TreeFunctor extends Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(v) => Leaf(f(v))
      case Branch(v, l, r) => Branch(f(v), map(l)(f), map(r)(f))
    }
  }

  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
    functor.map(container)(_ * 10)

  import cats.syntax.functor._
  def do10xShorter[F[_]: Functor](container: F[Int]): F[Int] = container.map(_ * 10)


  def main(args: Array[String]): Unit = {
    val tree: Tree[Int] = Branch(1, Branch(2, Leaf(3), Leaf(4)), Leaf(5))
    println(do10x(tree))
    println(do10xShorter(tree))
  }
}
