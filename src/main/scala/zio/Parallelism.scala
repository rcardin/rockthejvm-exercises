package zio

object Parallelism extends ZIOAppDefault {

  // Exercise 1
  // Do the word counting exercise, using a parallel combinator
  def countWords(path: String): UIO[Int] =
    ZIO.succeed {
      val source = scala.io.Source.fromFile(path)
      val nWords = source.getLines().mkString(" ").split(" ").count(_.nonEmpty)
      println(s"Number of words in $path: $nWords")
      source.close()
      nWords
    }

  def countWordsPar(index: Int): UIO[Int] = {
    val effects: Seq[UIO[Int]] = (1 to index).map { n =>
      s"src/main/resources/words_$n.txt"
    }.map(countWords)
    ZIO.collectAllPar(effects).map(_.sum)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ???
}
