package ppalli.web.rest.user

import jakarta.validation.constraints.Email
import ppalli.validatior.Password
import ppalli.validatior.Username

/**
 * 사용자 생성 요청 정보 DTO
 *
 * 이 클래스는 API 요청에 사용되며 클라이언트 단에서 전달된 정보를 담는다.
 *
 * @property username 생성할 사용자 계정
 * @property password 생성할 사용자 계정의 평문 비밀번호
 * @property email 생성한 사용자가 사용할 이메일 주소
 */
data class CreateUserSpec(
  @field:Username
  val username: String,

  @field:Password
  val password: String,

  @field:Email
  val email: String
)
