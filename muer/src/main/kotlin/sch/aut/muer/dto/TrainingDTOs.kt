package sch.aut.muer.dto

import sch.aut.muer.modell.Team
import sch.aut.muer.modell.User
import java.util.*

class CreateTraining{
    lateinit var createdBy: User
    val location: String = ""
    lateinit var date: Date
    val startAt: String = ""
    constructor()
}

class TrainingDTO{
    var id:Long = 0
    lateinit var createdBy: User
    lateinit var date: Date
    var startAt:String = ""

    constructor()
}