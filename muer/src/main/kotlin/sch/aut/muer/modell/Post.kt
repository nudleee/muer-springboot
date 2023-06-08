package sch.aut.muer.modell

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    open lateinit var createdBy:User

    @field:CreationTimestamp
    open lateinit var createdAt: Date

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    open var training:Training? = null

    constructor()

}
enum class PostType {
    EVENT,
    TRAINING,
    DEFAULT
}

