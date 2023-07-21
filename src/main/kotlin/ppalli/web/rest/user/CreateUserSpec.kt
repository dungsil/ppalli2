package ppalli.web.rest.user

import ppalli.validation.Password
import ppalli.validation.Username

/**
 * 사용자 생성 요청 DTO
 *
 * @param username 생성할 사용자 계정
 * @param password 생성할 사용자의 평문 비밀번호
 */
data class CreateUserSpec(

  @field:Username
  val username: String,

  @field:Password
  val password: String
)
