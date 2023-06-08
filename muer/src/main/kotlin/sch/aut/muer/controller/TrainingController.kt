package sch.aut.muer.controller

import com.azure.core.annotation.BodyParam
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import sch.aut.muer.modell.Training
import sch.aut.muer.modell.User
import sch.aut.muer.repository.TeamRepository
import sch.aut.muer.repository.TrainingRepository
import sch.aut.muer.repository.UserRepository
import sch.aut.muer.security.ClaimChecker

@RequestMapping("api/trainings")
@RestController
class TrainingController(
        val checker:ClaimChecker,
        val trainingRepository: TrainingRepository,
        val userRepository: UserRepository,
        val teamRepository: TeamRepository
    ) {


    @PutMapping("/{id}/participants")
    fun addParticipant(@PathVariable id:Long) :ResponseEntity<Training> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if(currentUser != null) {
            val training = trainingRepository.findByIdOrNull(id)
            val teams = teamRepository.findAllByMembers(currentUser)
            var isMember = false
            teams.forEach {t->  if(t.trainings.contains(training)){
                isMember = true
            }
            }
            if(training != null){
                if(!training.participants.contains(currentUser) && isMember){
                    training.participants.add(currentUser)
                    val updatedTraining = trainingRepository.save(training)
                    return ResponseEntity.status(HttpStatus.OK).body(updatedTraining)
                }else
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }else
                throw  ResponseStatusException(HttpStatus.NOT_FOUND)
        }else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    @PutMapping("/{id}")
    fun updateTraining(
           @PathVariable id: Long,
           @RequestBody training:Training
    ): ResponseEntity<Training> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            if(trainingRepository.existsById(id) ){
                training.id = id
                val updatedTraining = trainingRepository.save(training)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTraining)
            }else
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeParticipant(
        @PathVariable id:Long,
        @RequestBody user: User
    )  {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach", "Player"))
        if(currentUser != null) {
            val training = trainingRepository.findByIdOrNull(id)
            val dbUser = if(currentUser.role == "Player" && user.id != currentUser.id) {
                null
            } else {
                userRepository.findByIdOrNull(user.id)
            }
            if(training != null && dbUser != null ){
                if(training.participants.contains(dbUser)){
                    training.participants.remove(dbUser)
                    trainingRepository.save(training)
                }else
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }else
                throw  ResponseStatusException(HttpStatus.NOT_FOUND)
        }else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}