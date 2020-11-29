package catspro

object Monoids {

  import cats.Monoid
  import cats.instances.int._
  import cats.syntax.monoid._

  def combineFold[T: Monoid](list: List[T]): T = {
    val monoid = implicitly[Monoid[T]]
    list.foldLeft(monoid.empty)(_ |+| _)
  }

  val numbers: List[Int] = (1 to 1000).toList

  import cats.instances.option._
  val emptyOptionOfInt: Option[Int] = Monoid[Option[Int]].empty

  val phonebooks = List(
    Map(
      "Alice" -> 235,
      "Bob" -> 647
    ),
    Map(
      "Charlie" -> 372,
      "Daniel" -> 889
    ),
    Map(
      "Tina" -> 123
    )
  )

  import cats.instances.map._
  import cats.instances.string._
  case class ShoppingCart(items: List[String], amount: Double)
  implicit val shoppingCartMonoid: Monoid[ShoppingCart] =
    Monoid.instance(
      ShoppingCart(List(), 0.0),
      (sc1: ShoppingCart, sc2: ShoppingCart) => ShoppingCart(sc1.items |+| sc2.items, sc1.amount |+| sc2.amount)
    )
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = combineFold(shoppingCarts)

  def main(args: Array[String]): Unit = {
    println(combineFold(numbers))
    println(emptyOptionOfInt)
    println(combineFold(phonebooks))
    val shoppingCarts = List(
      ShoppingCart(List("MacBook", "iPhone 12"), 3500),
      ShoppingCart(List("iPhone 12 case", "iPhone 12 charger"), 250)
    )
    println(checkout(shoppingCarts))
  }
}
