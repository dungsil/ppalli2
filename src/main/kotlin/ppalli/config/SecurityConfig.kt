package ppalli.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Spring security 공통 설정
 * 메소드 레벨 보안을 활성화하고 비밀번호 암호화를 위한 [PasswordEncoder] 를 등록한다
 *
 *  웹 레벨 보안 설정은 [WebConfig] 참고
 *
 * @see [WebConfig]
 * @see [ppalli.utils.passwordEncoder]
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

  /**
   * Spring 라이프사이클에 [ppalli.utils.passwordEncoder] 를 등록한다
   *
   * @see [ppalli.utils.passwordEncoder]
   */
  @Bean
  fun passwordEncoder(): PasswordEncoder = ppalli.utils.passwordEncoder
}
