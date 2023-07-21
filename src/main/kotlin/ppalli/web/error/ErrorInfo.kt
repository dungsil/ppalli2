package ppalli.web.error

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * 오류 응답 정보 DTO
 *
 * **사용 예시**:
 * ```kotlin
 * // 응답 데이터: { "code": "NOT_FOUND" }
 * val notFound = ErrorInfo(code = ErrorCode.NOT_FOUND)
 *
 * // 응답 데이터: { "code": "DUPLICATE_USERNAME", "username": "user01"}}
 * val alreadyUsername = ErrorInfo(
 *   code = ErrorCode.DUPLICATE_USERNAME,
 *   additional = mapOf("username" to "user01")
 * )
 * ```
 *
 * @property code 오류 코드
 * @property additional 오류 상세 정보
 * @see [ErrorCode]
 * @see [ErrorAdditional]
 */
data class ErrorInfo(
  val code: ErrorCode,

  @field:JsonIgnore // field 레벨에서의 [details] 는 무시한다.
  @get:JsonAnyGetter // getter 를 통해서 [details] 를 직렬화한다.
  val additional: ErrorAdditional? = null
)
