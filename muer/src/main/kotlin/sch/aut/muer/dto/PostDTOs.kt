package sch.aut.muer.dto

import sch.aut.muer.modell.Post
import sch.aut.muer.modell.PostType
import sch.aut.muer.modell.User

class CreatePost {
    val title: String = ""
    val description: String = ""
    val type: PostType = PostType.DEFAULT
    lateinit var createdBy: User
    constructor()
}

class PostResponse(
        val data:List<Post>,
        val total:Int
)