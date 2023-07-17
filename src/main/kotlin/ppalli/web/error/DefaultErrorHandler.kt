package ppalli.web.error

import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * PPALLI 기본 전역 오류 컨트롤러 ([ErrorController])
 *
 * Spring 내부에서 처리되어 오류를 리턴하는 경우 이곳에서 공통 형식에 재가공하여 사용자에게 표시한다
 *
 * @see [ErrorController]
 */
@RestController
class DefaultErrorHandler : ErrorController {
  companion object {
    // Spring 내부에서 오류 페이지는 [ERROR_PATH] 경로로 리다이렉트되어 처리된다
    const val ERROR_PATH = "/error"
  }

  /**
   * [HttpServletResponse] 를 받아 실제 사용자에게 표시될 응답으로 변환한다
   *
   * 응답한 오류 코드는 [ErrorCode] 에 정의되어 있다
   *
   * **응답 예시 (존재하지 않는 페이지)**:
   * ```json
   * { "code": "NOT_FOUND" }
   * ```
   *
   * **응답 예시 (서버 에러)**:
   * ```json
   * { "code": "SERVER_ERROR" }
   * ```
   *
   * @param res Spring 내부에서 결정된 HTTP 응답 정보
   * @return HTTP 상태 코드 및 오류 정보가 포함된 [ResponseEntity]
   * @see [ErrorCode]
   */
  @RequestMapping(ERROR_PATH)
  fun handleError(res: HttpServletResponse): ResponseEntity<ErrorInfo> {
    val status = transformPublicErrorStatus(res.status)
    return createErrorResponse(status = status)
  }

  /**
   * 지정된 HTTP 상태 코드에 따라 사용자에게 표시할 HTTP 상태코드를 반환한다
   *
   * 다음과 같은 경우 실제 상태코드와 응답할 상태코드가 변경된다
   *  - 403 Forbidden -> 404 Not Found : 권한 없음은 페이지가 있다는 것을 전제함으로
   *    권한이 없는 사용자에게는 페이지가 없는 것으로 표시한다
   *  - 501 ~ 599 -> 500 Internal Server Error : 서버 에러 정보를 노출하지 않기 위해
   *    모든 서버 오류는 500 으로 통일하여 사용자에게 표시한다
   *
   * 이러한 변환은 개발자가 의도적으로 예외를 발생시키는 경우는 해당 사항을 적용하지 않다
   *
   * @param statusCode Spring 내부에서 처리된 HTTP 상태 코드
   * @return 사용자에게 표시할 HTTP 상태 코드
   */
  private fun transformPublicErrorStatus(statusCode: Int): HttpStatus =
    when (statusCode) {
      403 -> NOT_FOUND
      in 500..599 -> INTERNAL_SERVER_ERROR
      else -> HttpStatus.valueOf(statusCode)
    }
}
