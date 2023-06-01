package sch.aut.muer.controller

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import sch.aut.muer.dto.CreateTraining
import sch.aut.muer.modell.AutoMapper
import sch.aut.muer.modell.Training
import sch.aut.muer.repository.TeamRepository
import sch.aut.muer.repository.TrainingRepository
import sch.aut.muer.security.ClaimChecker

@RequestMapping("api/trainings")
@RestController
class TrainingController(
        val checker:ClaimChecker,
        val trainingRepository: TrainingRepository,
) {

    @PutMapping("/{id}")
    fun updateTraining(
           @PathVariable id: Long,
           @RequestBody training:Training
    ): ResponseEntity<Training> {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null){
            if(trainingRepository.existsById(id)){
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
    fun deleteTraining(
            @PathVariable id:Long
    ) {
        val currentUser = checker.checkClaim("extension_Role", listOf("Admin", "Coach"))
        if(currentUser != null) {
            if(trainingRepository.existsById(id))
                trainingRepository.deleteById(id)
        }else
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }


}