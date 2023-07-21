package ppalli.exception

import org.springframework.http.HttpStatus
import ppalli.web.error.ErrorAdditional

/**
 * PPALLI 비지니스 예외 클래스
 *
 * @property status 예외가 발생할 때 사용자에게 응답할 HTTP 상태코드
 * @property code 예외가 발생할 때 사용자에게 응답할 오류 코드
 * @property errors 예외가 발생할 때 사용자에게 표시할 추가 정보
 * @property message 예외 메시지
 * @property cause 관련된 예외
 */
open class PpalliException(
  val status: HttpStatus,
  val code: String = status.name,
  val errors: ErrorAdditional? = null,

  override val message: String? = "[$status] $code : $errors",
  override val cause: Throwable? = null
) : RuntimeException(message, cause) {
  constructor(status: HttpStatus, code: String, error: Pair<String, Any>) : this(
    status = status,
    code = code,
    errors = mapOf(error),
  )
}
