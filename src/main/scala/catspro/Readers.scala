package catspro

object Readers {

  import cats.data.Reader

  case class Configuration(dbUsername: String,
                           dbPassword: String,
                           host: String,
                           port: Int,
                           nThreads: Int,
                           emailReplyTo: String)
  case class DbConnection(username: String, password: String) {
    def getOrderStatus(orderId: Long): String = "dispatched"
    def getLastOrderId(username: String): Long = 542643
  }

  val config: Configuration =
    Configuration("daniel", "rockthejvm1!", "localhost", 1234, 8, "me@email.com")

  val dbReader: Reader[Configuration, DbConnection] =
    Reader(conf => DbConnection(conf.dbUsername, conf.dbPassword))

  def getLastOrderStatus(username: String): String = {
    dbReader.map(_.getLastOrderId(username))
      .flatMap(orderId => dbReader.map(_.getOrderStatus(orderId)))
      .run(config)

    val orderStatusByUsername = for {
      orderId <- dbReader.map(_.getLastOrderId(username))
      orderStatus <- dbReader.map(_.getOrderStatus(orderId))
    } yield orderStatus
    orderStatusByUsername.run(config)
  }

  case class EmailService(emailReplyTo: String) {
    def sendEmail(address: String, contents: String): String =
      s"From: $emailReplyTo; To: $address >>> $contents"
  }

  val emailReader: Reader[Configuration, EmailService] =
    Reader(conf => EmailService(conf.emailReplyTo))

  def emailUser(username: String, userEmail: String): String = {
    val orderStatus = getLastOrderStatus(username)
    val email = emailReader.map(_.sendEmail(
      userEmail,
      s"Your last order has the status: $orderStatus")
    )
    email.run(config)
  }

  def main(args: Array[String]): Unit = {
    val email = emailUser("rcardin", "rcardin@gmail.com")
    println(email)
  }
}
