package ppalli.web.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

typealias ErrorAdditional = Map<String, Any?>

/**
 * 오류 정보 ([ErrorInfo]) 가 포함된 [ResponseEntity] 를 생성하는 메소드
 *
 * @param status HTTP 상태 코드
 * @param code 오류 코드
 * @param errors 오류 정보
 * @return 오류 정보 ([ErrorInfo]) 가 포함된 [ResponseEntity]
 */
internal fun createErrorResponse(
  status: HttpStatus,
  code: String = status.name,
  errors: ErrorAdditional? = null
): ResponseEntity<ErrorInfo> {
  return ResponseEntity
    .status(status)
    .body(ErrorInfo(code = code, additional = errors))
}
