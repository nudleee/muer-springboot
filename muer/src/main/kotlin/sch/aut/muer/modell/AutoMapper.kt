package sch.aut.muer.modell

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import sch.aut.muer.dto.CreatePost
import sch.aut.muer.dto.CreateTeam
import sch.aut.muer.dto.CreateTraining
import sch.aut.muer.dto.TeamDTO

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AutoMapper {

    fun createTeamToTeam(team: CreateTeam):Team
    fun createPostToPost(post:CreatePost):Post
    fun teamToTeamDTO(trainings: List<Team>): List<TeamDTO>
    fun createTrainingToTraining(training: CreateTraining):Training

}