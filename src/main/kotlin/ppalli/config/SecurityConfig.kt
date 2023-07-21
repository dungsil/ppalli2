package ppalli.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import ppalli.utils.PasswordUtils

/**
 * Spring security 공통 설정
 * 메소드 레벨 보안을 활성화하고 비밀번호 암호화를 위한 [PasswordEncoder] 를 등록한다
 *
 * @see PasswordUtils
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordUtils.instance
}
