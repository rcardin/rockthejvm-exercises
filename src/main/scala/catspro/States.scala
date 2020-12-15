package catspro

object States {

  import cats.data.State

  case class ShoppingCart(items: List[String], total: Double)
  def addToCart(item: String, price: Double): State[ShoppingCart, Double] =
    State { (sc: ShoppingCart) =>
      (ShoppingCart(item :: sc.items, sc.total + price), sc.total + price)
    }

  def inspect[A, B](f: A => B): State[A, B] = State (a => (a, f(a)))
  def get[A]: State[A, A] = State(a => (a, a))
  def set[A](value: A): State[A, Unit] = State(_ => (value, ()))
  def modify[A](f: A => A): State[A, Unit] = State(a => (f(a), ()))

  import cats.data.State._

  val program: State[Int, (Int, Int, Int)] = for {
    a <- get[Int]
    _ <- set[Int](a + 10)
    b <- get[Int]
    _ <- modify[Int](_ + 43)
    c <- inspect[Int, Int](_ * 2)
  } yield (a, b, c)

  def main(args: Array[String]): Unit = {
    val finalShoppingCart = for {
      _ <- addToCart("MacBook", 2999.0)
      total <- addToCart("iPhone", 1199.0)
    } yield total
    println(finalShoppingCart.run(ShoppingCart(List(), 0.0)).value)

    println(program.run(10).value) // (63, (10, 20, 126))
  }
}
