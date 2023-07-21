package ppalli.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ppalli.utils.JsonUtils

/**
 * Spring 전역에서 사용되는 Jackson 모듈에 대한 추가 설정
 */
@Configuration
class JacksonConfig {

  @Bean
  fun objectMapper(): ObjectMapper = JsonUtils.instance
}
