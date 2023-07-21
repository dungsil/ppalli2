package ppalli.web.error

import org.springframework.http.HttpStatus

/**
 * PPALLI 에서 발생하는 에러 코드
 */
enum class ErrorCode {
  // ===================================================================================================================
  // 비지니스 : 클라이언트 오류
  // ===================================================================================================================

  /** 유효성 검증 실패 */
  VALIDATION_FAILED,

  /** 요청 바디가 없음 */
  EMPTY_BODY,

  /** 사용자 계정이 이미 존재함 */
  DUPLICATE_USERNAME,

  // ===================================================================================================================
  // 비지니스 : 서버 오류
  // ===================================================================================================================
  MAX_TRY_TOKEN_GENERATION,

  // ===================================================================================================================
  // 폴백 오류 : HTTP 클라이언트 오류
  // ===================================================================================================================

  /** 잘못된 요청 - HTTP 상태코드: 400 Bad Request */
  BAD_REQUEST,

  /** 인증 실패 - HTTP 상태코드: 401 Unauthorized */
  UNAUTHORIZED,

  /** 접근 금지 - HTTP 상태코드: 403 Forbidden */
  FORBIDDEN,

  /** 리소스 없음 - HTTP 상태코드: 404 Not Found */
  NOT_FOUND,

  /** 허용되지 않은 HTTP 메소드 - HTTP 상태코드: 405 Method Not Allowed */
  METHOD_NOT_ALLOWED,

  // ===================================================================================================================
  // 폴백 오류 : HTTP 서버 오류
  // ===================================================================================================================

  /** 서버 오류 - HTTP 상태코드: 500 Internal Server Error */
  SERVER_ERROR,

  // ===================================================================================================================
  // 기타
  // ===================================================================================================================

  /** 알 수 없는 오류 */
  UNKNOWN_ERROR
  ;

  companion object {

    /**
     * [HttpStatus] 에 해당하는 [ErrorCode] 를 반환한다.
     *
     * [HttpStatus] 가 아래의 값과 같을 때는 동일한 이름의 [ErrorCode] 를 반환한다.
     *  - 400 [BAD_REQUEST]
     *  - 401 [UNAUTHORIZED]
     *  - 403 [FORBIDDEN]
     *  - 404 [NOT_FOUND]
     *
     * 그 외의 클라이언트 오류는 [BAD_REQUEST] 를 반환한다.
     * 그 외의 서버 오류는 [SERVER_ERROR] 를 반환한다.
     * 오류가 아닌 상태코드를 받았을 때는 [UNKNOWN_ERROR] 를 반환한다.
     *
     * @param status HTTP 상태코드
     */
    @JvmStatic
    fun valueOf(status: HttpStatus): ErrorCode {
      return when {
        status == HttpStatus.BAD_REQUEST -> BAD_REQUEST
        status == HttpStatus.UNAUTHORIZED -> UNAUTHORIZED
        status == HttpStatus.FORBIDDEN -> FORBIDDEN
        status == HttpStatus.NOT_FOUND -> NOT_FOUND
        // NOTE: 이 외의 상태코드를 나열할지 고민 중...

        status.is4xxClientError -> BAD_REQUEST
        status.is5xxServerError -> SERVER_ERROR
        else -> UNKNOWN_ERROR
      }
    }
  }
}
