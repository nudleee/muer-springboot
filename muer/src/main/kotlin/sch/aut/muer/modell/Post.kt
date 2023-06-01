package sch.aut.muer.modell

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
open class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long = 0

    open var title: String = ""

    open var description:String = ""

    open var  type: PostType = PostType.DEFAULT

    @ManyToOne
    open lateinit var createdBy:User

    @field:CreationTimestamp
    open lateinit var createdAt: Date
    constructor()

}
enum class PostType {
    EVENT,
    TRAINING,
    DEFAULT
}

