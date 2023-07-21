package ppalli.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ppalli.utils.JsonUtils


/**
 * Spring MVC 및 Spring Security 의 MVC 통합을 설정하는 클래스
 *
 * @see [SecurityConfig]
 */
@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

  override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
    converters
      .filterIsInstance<MappingJackson2HttpMessageConverter>()
      .map { it.objectMapper = JsonUtils.instance }
  }
}
