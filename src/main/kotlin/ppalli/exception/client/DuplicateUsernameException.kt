package ppalli.exception.client

import org.springframework.http.HttpStatus.BAD_REQUEST
import ppalli.exception.PpalliException

/**
 * 사용자 계정이 중복해서 존재하여 발생하는 예외
 *
 * @property username 중복된 사용자 계정
 */
class DuplicateUsernameException(val username: String) : PpalliException(
  status = BAD_REQUEST,
  code = "DUPLICATE_USERNAME",
  error = "username" to username
)
