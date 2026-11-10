package it.unibo.pps.trials

// The Uniform Access Principle states that a client should not be able to tell whether a property
// is implemented as a stored field (a variable in memory) or computed dynamically via a method.

object UniformAccess:
  // =========================
  // 1. Using Class Parameters
  // =========================

  // 1a. Just a parameter (Not accessible outside, used only for initialization)
  class Box(size: Int)

  // 1b. Encapsulated immutable state (Compiler generates a private field and a public getter)
  class ReadOnlyBox(val size: Int)

  // 1c. Encapsulated mutable state (Compiler generates private field, public getter, and public setter)
  class MutableBox(var size: Int)

  // =========================
  // 2. Overriding some methods
  // =========================

  trait Shape:
    def area: Double

  class Circle(radius: Double) extends Shape:
    // Dynamically computed every time area is called
    override def area: Double = Math.PI * radius * radius

  class Square(side: Double) extends Shape:
    // Computed ONCE at initialization and stored in memory
    override val area: Double = side * side

  // =========================
  // 3. Overriding a var with methods
  // =========================

  trait BankAccount:
    var balance: Double

  class AuditedAccount extends BankAccount:
    private var _balance: Double = 0.0

    // Override the Getter
    def balance: Double = _balance

    // Override the Setter (The syntax `_=` intercepts assignments)
    def balance_=(newAmount: Double): Unit =
      if newAmount < 0 then
        println("Audit Alert: Attempted negative assignment blocked!")
      else
        println(s"Audit Log: Balance changing from $_balance to $newAmount")
        _balance = newAmount




@main def tryUniformAccess(): Unit =
  import UniformAccess.*

  val box = ReadOnlyBox(10)
  println(box.size) // Uniform access: Looks like field access, but it's a method call

  val square: Shape = Square(4.0)
  println(square.area) // The client has no idea if this was computed or loaded from memory.

  val account: BankAccount = AuditedAccount()
  account.balance = 100.0 // Triggers the custom setter!
  println(account.balance) // Triggers the getter!



