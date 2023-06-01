package sch.aut.muer.modell

import jakarta.persistence.*

@Entity(name = "users")
open class User {
    @Id
    open var id:String = ""

    open var name: String = ""

    open var role:String = ""
/*

    @ManyToMany(fetch = FetchType.LAZY)
    open var teams: MutableList<Team> = mutableListOf()

    @OneToMany(mappedBy = "createdBy")
    open var trainings:MutableList<Training> = mutableListOf()

    @ManyToOne
    open var training:Training? = null

    @OneToMany(mappedBy = "coach")
    open var teams: MutableList<Team> = mutableListOf()

    @OneToMany(mappedBy = "createdBy")
    open var posts: MutableList<Post> = mutableListOf()

 */
    constructor()


    constructor(id: String, name: String, role:String)  {
        this.id = id
        this.name = name
        this.role = role
    }

}
