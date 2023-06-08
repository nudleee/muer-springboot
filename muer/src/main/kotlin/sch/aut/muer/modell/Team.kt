package sch.aut.muer.modell

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.Date

@Entity
open class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long = 0

    @ManyToOne
    open lateinit var coach: User

    @ManyToMany
    open var members:  MutableList<User> = mutableListOf()

    open var name: String = ""

    open var description: String = ""

    @ManyToMany
    @OrderBy(value = "date DESC", )
    open var trainings: MutableList<Training> = mutableListOf()

    @field:CreationTimestamp
    open lateinit var createdAt:Date

    constructor()

}