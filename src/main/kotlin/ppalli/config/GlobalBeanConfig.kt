package ppalli.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Spring 전역에서 사용되는 Bean 을 설정하는 클래스
 */
@Configuration
class GlobalBeanConfig {

  /**
   * Spring 라이프사이클에 [ppalli.utils.objectMapper] 를 등록한다
   *
   * @see [ppalli.utils.objectMapper]
   */
  @Bean
  fun objectMapper(): ObjectMapper = ppalli.utils.objectMapper
}
