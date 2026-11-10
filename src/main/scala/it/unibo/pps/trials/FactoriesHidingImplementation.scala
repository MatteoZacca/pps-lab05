package it.unibo.pps.trials

import it.unibo.pps.trials.FactoriesHidingImplementation.UserPermissions.Read_Only

object FactoriesHidingImplementation:
  enum UserPermissions:
    case Read_Only
    case Read_Write_Execute

  // 1. Public Contract
  trait User:
    def name: String
    def permissions: UserPermissions

  // Companion Object (The Factory)
  object User:
    // 3. Hidden Implementation
    private class StandardUser(val name: String) extends User:
      val permissions: UserPermissions = UserPermissions.Read_Only

    private class AdminUser(val name: String) extends User:
      val permissions: UserPermissions = UserPermissions.Read_Write_Execute

    // 4. The apply method hides the decision and the concrete classes
    def apply(name: String, isAdmin: Boolean): User =
      if isAdmin then new AdminUser(name) else new StandardUser(name)

    // 5. The Extractor Method: takes the abstract User trait and extracts the data
    def unapply(user: User): Option[(String, UserPermissions)] =
      Some((user.name, user.permissions))

@main def trialsFactoriesHidingImplementation(): Unit =
  import FactoriesHidingImplementation.*
  import User.*

  val adminUser = User("Fred", isAdmin = true)
  val standardUser = User("Slave", isAdmin = false)

  adminUser match
    case User(n, p) => println(s"Found user $n with access: $p")
    case _ => println("Unknown user")



