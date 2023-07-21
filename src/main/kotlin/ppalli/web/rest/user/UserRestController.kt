package ppalli.web.rest.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * 사용자 API 컨트롤러
 *
 * RESTful 형식의 HTTP API형태로 사용자의 요청을 처리한다.
 *
 * @see [UserRestService] 비지니스 로직
 */
@RestController
@RequestMapping("/users")
class UserRestController(private val service: UserRestService) {

  /**
   * 사용자 생성 API
   *
   * 클라이언트로 부터 사용자 정보를 입력받아 신규 사용자를 생성하고 DB에 저장하는 업무를 진행한다.
   * 입력받는 사용자 정보는 [spec]에 매핑되어 있으며 정상적으로 생성된 경우, HTTP 상태코드 201 CREATED와 함께
   * 생성된 사용자 정보를 리턴한다.
   *
   * @param spec 사용자 요청 body 정보
   * @return 생성된 사용자 정보가 포함된 [UserInfo] 인스턴스
   * @throws ppalli.exception.client.DuplicateUsernameException 사용자 이름이 이미 존재하는 경우에 발생
   */
  @PostMapping
  @ResponseStatus(CREATED)
  fun createUser(@Valid @RequestBody spec: CreateUserSpec) = service.createUser(spec.username, spec.password)
}
