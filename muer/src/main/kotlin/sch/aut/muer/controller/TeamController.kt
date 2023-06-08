package sch.aut.muer.controller

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import sch.aut.muer.dto.CreateTeam
import sch.aut.muer.dto.CreateTraining
import sch.aut.muer.dto.TeamResponse
import sch.aut.muer.modell.AutoMapper
import sch.aut.muer.modell.Team
import sch.aut.muer.modell.Training
import sch.aut.muer.modell.User
import sch.aut.muer.repository.TeamRepository
import sch.aut.muer.repository.TrainingRepository
import sch.aut.muer.repository.UserRepository
import sch.aut.muer.security.ClaimChecker

@RequestMapping("api/teams")
@RestController
class TeamController(
    val checker: ClaimChecker,
    val teamRepository: TeamRepository,
    val trainingRepository: TrainingRepository,
    val userRepository: UserRepository,
    val mapper: AutoMapper
) {

    @GetMapping("/all")
    fun allTeams(
            @RequestParam(defaultValue = "1") page: Int
    ): ResponseEntity<TeamResponse> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if(currentUser !=  null) {
            return ResponseEntity.status(HttpStatus.OK).body(getTeams(page,4))
        }else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/trainings")
    fun getTrainings(): ResponseEntity<List<Training>> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null) {
            val teams = if(currentUser.role == "Admin"){
                teamRepository.findAll()
            }else {
                teamRepository.findAllByCoach(currentUser)
            }
            val trainingList = mutableListOf<Training>()
            teams.forEach { trainingList.addAll(it.trainings)}
            return ResponseEntity.status(HttpStatus.OK).body(trainingList)
        }else
            throw  ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping
    fun teams(
            @RequestParam(defaultValue = "1") page: Int,
            @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<TeamResponse> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if (currentUser != null) {
            lateinit var teams: TeamResponse
            if(currentUser.role == "Player") {
                teams = teamRepository
                        .findByMembers(currentUser, PageRequest.of(page - 1, size, Sort.by("createdAt").descending()))
                        .let {
                            TeamResponse(
                                    data = it.toList(),
                                    page= page,
                                    total = teamRepository.findAllByMembers(currentUser).size
                            )
                        }

            } else {
                teams = getTeams(page, size)
            }

            return ResponseEntity.status(HttpStatus.OK).body(teams)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    fun getTeams(page: Int, size : Int): TeamResponse {
        return teamRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by("createdAt").descending()))
                .let {
                    TeamResponse(
                            data = it.toList(),
                            page = page,
                            total = teamRepository.findAll().size
                    )
                }
    }

    @GetMapping("/{id}")
    fun team(
            @PathVariable id: Long,
    ): ResponseEntity<Team> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if (currentUser != null) {
            val team = teamRepository.findByIdOrNull(id)
            if(team != null) {
                return ResponseEntity.status(HttpStatus.OK).body(team)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/{id}/members")
    fun joinTeam(
            @PathVariable id: Long,
            @RequestBody user:User
    ): ResponseEntity<Team> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if(currentUser != null){
            val team = teamRepository.findByIdOrNull(id)
            val dbUser = userRepository.findByIdOrNull(user.id)
            if(team != null && dbUser != null) {
                if(!team.members.contains(dbUser))
                    team.members.add(dbUser)

                val updatedTeam = teamRepository.save(team)

                return ResponseEntity.status(HttpStatus.CREATED).body(updatedTeam)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/{id}/trainings")
    fun createTraining(
            @PathVariable id: Long,
            @RequestBody training: CreateTraining
    ): ResponseEntity<Team> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            val team = teamRepository.findByIdOrNull(id)
            if(team != null ) {
                val createdTraining = trainingRepository.save(mapper.createTrainingToTraining(training))
                createdTraining.createdBy = currentUser
                team.trainings.add(createdTraining)
                val updatedTeam = teamRepository.save(team)
                return ResponseEntity.status(HttpStatus.CREATED).body(updatedTeam)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
    @PostMapping
    fun createTeam(
            @RequestBody team: CreateTeam
    ): ResponseEntity<Team> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            val createTeam = mapper.createTeamToTeam(team)
            createTeam.members.addAll(listOf(team.coach, currentUser))
            val createdTeam = teamRepository.save(createTeam)
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PutMapping("/{id}")
    fun updateTeam(
            @PathVariable id: Long,
            @RequestBody team: Team
    ): ResponseEntity<Team> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null) {
            if(teamRepository.existsById(id)) {
                team.id = id
                val updatedTeam = teamRepository.save(team)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTeam)
            } else
                throw  ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTeam(
            @PathVariable id: Long
    ) {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))

        if(currentUser != null) {
            if(teamRepository.existsById(id)) {
                teamRepository.deleteById(id)
            }
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{id}/trainings/{trainingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTraining(
        @PathVariable id: Long,
        @PathVariable trainingId: Long
    ) {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            val team = teamRepository.findByIdOrNull(id)
            if(team != null ) {
                val training = trainingRepository.findByIdOrNull(trainingId)
                if(team.trainings.contains(training)) {
                    team.trainings.remove(training)
                    teamRepository.save(team)
                }else
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @DeleteMapping("/{id}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeMember(
        @PathVariable id: Long,
        @RequestBody user: User
    ){
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            val team = teamRepository.findByIdOrNull(id)
            val dbUser = userRepository.findByIdOrNull(user.id)
            if(team != null && dbUser != null) {
                if(team.members.contains(dbUser)){
                    team.members.remove(dbUser)
                    teamRepository.save(team)
                }
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}