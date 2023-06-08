package sch.aut.muer.modell

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
open class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long = 0

    @ManyToOne
    open lateinit var createdBy:User

    @ManyToMany
    open var participants: MutableList<User> = mutableListOf()


    open var location: String = ""

    @JsonFormat(pattern = "yyyy-MM-dd")
    open lateinit var date: Date

    open var startAt: String = ""

    open var description: String = ""

    @field:CreationTimestamp
    open lateinit var createdAt: Date

    constructor()
}