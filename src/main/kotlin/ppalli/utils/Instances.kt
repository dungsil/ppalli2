package ppalli.utils

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.util.StdDateFormat
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

/**
 * PPALLI 프로젝트 내부에서 사용하는 공통 [ObjectMapper] 인스턴스
 */
val objectMapper: ObjectMapper = ObjectMapper()
  .findAndRegisterModules()
  .apply { dateFormat = StdDateFormat.instance }
  .apply { propertyNamingStrategy = LOWER_CAMEL_CASE }
  .apply { setDefaultPropertyInclusion(NON_NULL) }
  .apply { configure(WRITE_DATES_AS_TIMESTAMPS, false) }
  .apply { configure(WRITE_DURATIONS_AS_TIMESTAMPS, false) }

/**
 * 비밀번호를 인코딩하고 검증하는데 사용되는 비밀번호 인코더
 *
 * [DelegatingPasswordEncoder] 를 통해 비밀번호 앞 prefix 를 통해
 * 비밀번호 마이그레이션을 간편하게 제공한다
 *
 * @see DelegatingPasswordEncoder
 * @see Argon2PasswordEncoder
 */
val passwordEncoder: DelegatingPasswordEncoder = DelegatingPasswordEncoder(
  "v1",
  mapOf(
    "v1" to Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
  )
)
