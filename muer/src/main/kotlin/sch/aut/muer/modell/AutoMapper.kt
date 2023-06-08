package sch.aut.muer.modell

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import sch.aut.muer.dto.*

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AutoMapper {

    fun createTeamToTeam(team: CreateTeam):Team
    fun createPostToPost(post:CreatePost):Post
    fun teamToTeamDTO(trainings: List<Team>): List<TeamDTO>
    fun createTrainingToTraining(training: CreateTraining):Training

    fun trainingToTrainingOption(teams: List<Training>): List<TrainingOption>

}