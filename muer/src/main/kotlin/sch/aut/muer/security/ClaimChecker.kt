package sch.aut.muer.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component("claimChecker")
class ClaimChecker {
    fun checkClaim( claimName:String, value: String) : Boolean {
        val auth = SecurityContextHolder.getContext().authentication

        if(auth!=null && auth.principal is Jwt) {
            val jwt = auth.principal as Jwt
            val claim = jwt.getClaim<String>(claimName)
            return claim == value
        }
        return false
    }
}