package sch.aut.muer.security


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig() {

    @Bean
    fun apiFilterChain(http: HttpSecurity) :SecurityFilterChain {

        http.authorizeRequests { request ->
            request.anyRequest().authenticated()
        }.oauth2ResourceServer { oauth2 ->
            oauth2.jwt()
        }

        return http.build()
    }

}