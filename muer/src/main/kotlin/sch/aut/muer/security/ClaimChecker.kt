package sch.aut.muer.security

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import sch.aut.muer.modell.User
import sch.aut.muer.repository.UserRepository

@Component("claimChecker")
class ClaimChecker(val userRepository: UserRepository) {
    fun checkClaim( claimName:String, values: List<String>) : User? {
        val auth = SecurityContextHolder.getContext().authentication

        if(auth!=null && auth.principal is Jwt) {
            val jwt = auth.principal as Jwt

            val user = userRepository.findByIdOrNull(jwt.getClaim("oid"))
            var savedUser: User? = null

            if(user == null || user.role != jwt.getClaim("extension_Role")){
                val id = jwt.getClaim<String>("oid")
                val name = jwt.getClaim<String>("family_name") + " " + jwt.getClaim<String>("given_name")
                val role = jwt.getClaim<String>("extension_Role")
                savedUser = userRepository.save(User(id,name,role))
            }

            val claim = jwt.getClaim<String>(claimName)
            if(values.contains(claim)) {
                return user ?: savedUser
            }
        }

        return null
    }
}