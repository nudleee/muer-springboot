package sch.aut.muer.dto

import sch.aut.muer.modell.Team
import sch.aut.muer.modell.User

class TeamResponse (
        val data: List<Team>,
        val page: Int,
        val total: Int
)

class TeamDTO{
    var id: Long = 0
    lateinit var coach:User
    lateinit var members:List<User>
    var name: String = ""
    var description: String  =""
    lateinit var trainings:List<TrainingDTO>

    constructor()
}

class CreateTeam {
    val name:String = ""
    val description:String =""
    lateinit var coach: User
    constructor()
}