package sch.aut.muer.modell

import jakarta.persistence.*

@Entity(name = "users")
open class User {
    @Id
    open var id:String = ""

    open var name: String = ""

    open var role:String = ""

    open var email:String = ""

    constructor()


    constructor(id: String, name: String, role:String, email:String)  {
        this.id = id
        this.name = name
        this.role = role
        this.email = email
    }

}
