package catspro

import scala.annotation.tailrec

object Writers {

  import cats.data.Writer
  import cats.instances.vector._
  def countAndLog(n: Int): Writer[Vector[String], Int] = {
    @tailrec
    def countWithWriter(w: Writer[Vector[String], Int]): Writer[Vector[String], Int] = w.run match {
      case (_, 0) => w
      case (_, m) => countWithWriter(w.bimap(m.toString +: _, _ - 1))
    }
    countWithWriter(Writer(Vector(), n))
  }

  def naiveSum(n: Int): Int = {
    if (n <= 0) 0
    else {
      println(s"Now at $n")
      val lowerSum = naiveSum(n - 1)
      println(s"Computed sum(${n - 1}) = $lowerSum")
      lowerSum + n
    }
  }

  def naiveSumWithWriters(n: Int): Writer[Vector[String], Int] = {
    if (n <= 0) Writer(Vector(), 0)
    else for {
      _ <- Writer(Vector(s"Now at $n"), n)
      lowerSum <- naiveSumWithWriters(n - 1)
      _ <- Writer(Vector(s"Computed sum(${n - 1}) = $lowerSum"), n)
    } yield lowerSum + n
  }

  def main(args: Array[String]): Unit = {
    println(countAndLog(10).written)
    println(naiveSum(10))
    naiveSumWithWriters(10).written.foreach(println)
  }
}
