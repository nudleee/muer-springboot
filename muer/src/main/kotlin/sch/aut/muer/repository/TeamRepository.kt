package sch.aut.muer.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sch.aut.muer.modell.Team
import sch.aut.muer.modell.User

@Repository
interface TeamRepository: JpaRepository<Team, Long> {
    fun findByMembers(member: User, pr: PageRequest): Page<Team>
    fun findAllByMembers(member: User):List<Team>
    fun findAllByCoach(coach:User): List<Team>

}