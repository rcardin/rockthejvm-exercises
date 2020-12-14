package catspro

object States {

  import cats.data.State

  case class ShoppingCart(items: List[String], total: Double)
  def addToCart(item: String, price: Double): State[ShoppingCart, Double] =
    State { (sc: ShoppingCart) =>
      (ShoppingCart(item :: sc.items, sc.total + price), sc.total + price)
    }

  def main(args: Array[String]): Unit = {
    val finalShoppingCart = for {
      _ <- addToCart("MacBook", 2999.0)
      total <- addToCart("iPhone", 1199.0)
    } yield total
    println(finalShoppingCart.run(ShoppingCart(List(), 0.0)).value)
  }
}
