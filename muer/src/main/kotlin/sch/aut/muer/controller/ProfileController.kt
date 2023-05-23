package sch.aut.muer.controller

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import sch.aut.muer.security.ClaimChecker

@RequestMapping("api/profiles")
@RestController
class ProfileController(val checker: ClaimChecker) {
    @GetMapping()
    fun profile(): ResponseEntity<String> {
        if(checker.checkClaim("extension_Role", "Admin"))
            return ResponseEntity.status(HttpStatus.OK).body("Hurray")
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")
    }


}