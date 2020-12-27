package catspro

object Applicatives {

  import cats.Applicative

  def productWithApplicatives[W[_], A, B](wa: W[A], wb: W[B])(implicit app: Applicative[W]): W[(A, B)] = {
    val fw: W[B => (A, B)] = app.map(wa)(a => (b: B) => (a, b))
    app.ap(fw)(wb)
  }
}
