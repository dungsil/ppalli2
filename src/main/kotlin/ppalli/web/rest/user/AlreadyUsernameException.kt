package ppalli.web.rest.user

import org.springframework.http.HttpStatus
import ppalli.exception.PpalliException
import ppalli.web.error.ErrorCode.ALREADY_USERNAME

/**
 * 이미 존재하는 사용자 이름으로 회원가입을 시도할 때 발생하는 예외
 *
 * @param username 이미 존재하는 사용자 이름
 */
class AlreadyUsernameException(username: String) : PpalliException(
  status = HttpStatus.BAD_REQUEST,
  code = ALREADY_USERNAME,
  error = "username" to username,
)
