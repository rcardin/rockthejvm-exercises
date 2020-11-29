package catspro

object Semigroups {
  case class Expense(id: Long, amount: Double)

  import cats.Semigroup
  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] { (ex1, ex2) =>
    Expense(ex1.id + ex2.id, ex1.amount + ex2.amount)
  }

  def reduceThings[T: Semigroup](list: List[T]): T = {
    val semigroup = implicitly[Semigroup[T]]
    list.reduce(semigroup.combine)
  }

  import cats.syntax.semigroup._
  def reduceThings2[T: Semigroup](list: List[T]): T = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    val expenses = List(Expense(1, 10.0), Expense(2, 20.0), Expense(3, 30.0))
    println(reduceThings(expenses))
    println(reduceThings2(expenses))
  }
}
