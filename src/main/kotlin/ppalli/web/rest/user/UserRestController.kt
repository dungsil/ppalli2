package ppalli.web.rest.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

/**
 * 사용자 Restful API 컨트롤러
 *
 * 이 컨트롤러는 사용자 관련 Restful API 를 라우팅하여 요청을 처리한다.
 *
 * @property service 실제 요청을 처리할 서비스 ([org.springframework.stereotype.Service])
 */
@RestController
@RequestMapping("/users")
class UserRestController(private val service: UserRestService) {

  /**
   * 신규 사용자를 생성하고 생성된 사용자 정보를 반환한다.
   *
   * @param spec 사용자 생성에 필요한 정보를 담은 [CreateUserSpec] 객체; 이 객체는 HTTP 요청 바디를 통해 전달된다.
   * @return 생성된 사용자 정보
   */
  @PostMapping
  @ResponseStatus(CREATED)
  fun createUser(@Valid @RequestBody spec: CreateUserSpec): UserInfo = service.createUser(spec)
}
