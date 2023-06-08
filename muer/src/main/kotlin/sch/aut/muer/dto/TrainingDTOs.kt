package sch.aut.muer.dto

import sch.aut.muer.modell.Team
import sch.aut.muer.modell.User
import java.util.*

class CreateTraining{
    val location: String = ""
    lateinit var date: Date
    val startAt: String = ""
    val description:String = ""
    constructor()
}

class TrainingDTO{
    var id:Long = 0
    lateinit var createdBy: User
    lateinit var date: Date
    var startAt:String = ""

    constructor()
}

class TrainingOption{
    var id:Long = 0
    lateinit var date: Date
    var location: String = ""
    var startAt:String = ""
}