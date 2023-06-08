package sch.aut.muer.dto

import sch.aut.muer.modell.Post
import sch.aut.muer.modell.PostType

class CreatePost {
    val title: String = ""
    val description: String = ""
    lateinit var type: PostType
    var trainingId: Long? = 0
    constructor()
}

class PostResponse(
        val data:List<Post>,
        val page: Int,
        val total:Int
)