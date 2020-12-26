package catspro

object DataValidation {

  // n must be a prime
  // n must be a non-negative
  // n <= 100
  // n must be even
  def testNumber(n: Int): Either[List[String], Int] = {
    def isPrime(n: Int): Either[String, Int] =
      if (!((2 until n - 1) exists (n % _ == 0))) Right(n)
      else Left(s"$n is not prime")

    def isPositive(n: Int): Either[String, Int] =
      if (n >= 0) Right(n)
      else Left(s"$n is negative")

    def isLessThan100(n: Int): Either[String, Int] =
      if (n <= 100) Right(n)
      else Left(s"$n is greater than 100")

    def isEven(n: Int): Either[String, Int] =
      if (n % 2 == 0) Right(n)
      else Left(s"$n is not even")

    null
    //    isPrime(n).fold(prime => List(prime), x => x)
  }

  import cats.data.Validated

  object FormValidation {
    type FormValidation[T] = Validated[List[String], T]

    def validateFrom(form: Map[String, String]): FormValidation[String] = {
      def getValue(form: Map[String, String], fieldName: String): FormValidation[String] =
        Validated.fromOption(form.get(fieldName), List(s"Field $fieldName cannot be empty"))

      def isBlank(value: String, fieldName: String) =
        Validated.cond(!value.isBlank, value, List(s"Field $fieldName cannot be blank"))

      def checkForEmailValidity(email: String)=
        Validated.cond(email.contains("@"), email, List("Email is not valid"))

      def checkForPasswordLength(password: String) =
        Validated.cond(password.length >= 10, password, List("The password must be at least 10 characters long"))

      getValue(form, "name").andThen(isBlank(_, "name"))
        .combine(getValue(form, "email").andThen(checkForEmailValidity))
          .combine(getValue(form, "password").andThen(checkForPasswordLength))
          .map(_ => "User registration is valid")
    }
  }

  def main(args: Array[String]): Unit = {
    val registrationFields = Map(
      "name" -> "",
      "email" -> "me.email.com",
      "password" -> "123456789"
    )
    println(FormValidation.validateFrom(registrationFields))
  }
}
