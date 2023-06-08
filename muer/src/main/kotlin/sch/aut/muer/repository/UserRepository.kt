package sch.aut.muer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sch.aut.muer.modell.User

@Repository
interface UserRepository: JpaRepository<User, String> {
    fun findAllByRole(role:String):List<User>
}