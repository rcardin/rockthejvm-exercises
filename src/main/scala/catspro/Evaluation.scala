package catspro

object Evaluation {
  import cats.Eval

  def defer[T](eval: => Eval[T]): Eval[T] =
    Eval.later(()).flatMap(_ => eval)

  def reverseList[T](list: List[T]): List[T] =
    if (list.isEmpty) list
    else reverseList(list.tail) :+ list.head

  def reverseEval[T](list: List[T]): Eval[List[T]] =
    if (list.isEmpty) Eval.later(list)
    else reverseEval(list.tail).map(_ :+ list.head)

  def main(args: Array[String]): Unit = {
    defer(Eval.now {
      println("Time is running out")
      42
    })
  }
}
