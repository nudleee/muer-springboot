package sch.aut.muer.dto

import sch.aut.muer.modell.User


class UserResponse (
    val data:List<User>,
    val page: Int,
    val total:Int
)