package zio

object ZIOEffect {

  // Exercise 1
  // Sequence two ZIOs and take the value of the last one
  def sequenceTakeLast[R, E, A, B](zioa: ZIO[R, E, A], ziob: ZIO[R, E, B]): ZIO[R, E, B] =
    zioa *> ziob

  // Exercise 2
  // Sequence two ZIOs and take the value of the first one
  def sequenceTakeFirst[R, E, A, B](zioa: ZIO[R, E, A], ziob: ZIO[R, E, B]): ZIO[R, E, A] =
    zioa <* ziob

  // Exercise 3
  // Run a ZIO forever
  def runForever[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = zio.forever

  // Exercise 4
  // Convert the value of a ZIO to something else
  def convert[R, E, A, B](zio: ZIO[R, E, A], value: B): ZIO[R, E, B] = zio.as(value)

  // Exercise 5
  // Discard the value of a ZIO to Unit
  def asUnit[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, Unit] = zio.unit

  // Exercise 6
  def sum(n: Int): Int =
    if (n == 0) 0
    else n + sum(n - 1)

  def sumZIO(n: Int): UIO[Int] =
    if (n == 0) ZIO.succeed(0)
    else for {
      current <- ZIO.succeed(n)
      prevSum <- sumZIO(n - 1)
    } yield current + prevSum


  def main(args: Array[String]): Unit = {
    val runtime = Runtime.default
    implicit val trace: Trace = Trace.empty
    Unsafe.unsafe { implicit u =>
      val firstEffect = ZIO.succeed {
        println("First effect...")
        1
      }
      val secondEffect = ZIO.succeed {
        println("Second effect...")
        2
      }
      println(
        runtime.unsafe.run(
          runForever {
            ZIO.succeed {
              println("Running...")
              Thread.sleep(1000)
            }
          }
        )
      )
    }
  }
}
