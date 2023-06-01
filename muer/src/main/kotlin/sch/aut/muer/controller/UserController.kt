package sch.aut.muer.controller

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors


@RestController
@RequestMapping("/user")
class UserRestController {
    @GetMapping
    fun getPrincipalInfo(principal: JwtAuthenticationToken): Map<String, Any> {
        val authorities = principal.authorities

        val info: MutableMap<String, Any> = HashMap()
        info["name"] = principal.name
        info["authorities"] = authorities
        info["tokenAttributes"] = principal.tokenAttributes
        return info
    }
}