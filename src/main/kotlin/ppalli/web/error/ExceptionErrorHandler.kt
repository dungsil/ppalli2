package ppalli.web.error

import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ppalli.exception.PpalliException
import ppalli.utils.createLogger
import ppalli.validation.SpecialCharacterConstraint

/**
 * PPALLI 내에서 발생하는 예외 ([Exception]) 을 공통적으로 처리하여
 * 사용자에게 올바른 응답정보를 리턴하는 오류 핸들러
 */
@RestControllerAdvice
class ExceptionErrorHandler {
  private val log = createLogger {}

  // ===================================================================================================================
  // PPALLI 어플리케이션에서 발생하는 예외를 처리하는 메소드
  // ===================================================================================================================

  /**
   * 비지니스 예외 처리
   *
   * 이 예외는 의도적으로 발생하였기 때문에 예외 정보를 그대로 사용자에게 전달한다
   *
   * 의도적으로 발생되었으므로 로그는 디버그 레벨에서만 표시한다
   *
   * @param e 발생한 예외
   */
  @ExceptionHandler(PpalliException::class)
  fun handlePpalliException(e: PpalliException): ResponseEntity<ErrorInfo> {
    log.debug("Business exception occurred", e)
    return createErrorResponse(status = e.status, code = e.code, errors = e.errors)
  }

  // ===================================================================================================================
  // Spring 내부에서 발생하는 예외를 처리하는 메소드
  // ===================================================================================================================

  /**
   * Bean Validation 예외 처리
   *
   * `spring-boot-starter-validation`, `hibernate-validator`등의
   * Bean Validation 에서 유효성 검증 실패 시 발생된 예외를 처리한다
   *
   * Spring 내부에서 의도적으로 발생하였으므로 디버그 레벨에서만 로그를 남긴다
   *
   * @param e 발생한 예외
   * @see [transformFieldErrorsToDetails] 사용자가 알맞은 오류 정보를 제공하도록 오류 정보를 변환하는 메소드
   */
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorInfo> {
    log.debug("Validation error occurred", e) // 비지니스 예외는 의도된 사항이기 때문에 디버그 로그로 남긴다

    return createErrorResponse(
      status = BAD_REQUEST,
      code = "VALIDATION_FAILED",
      errors = transformFieldErrorsToDetails(e.bindingResult.fieldErrors)
    )
  }

  /**
   * API 요청에 본문이 있어야하지만 없을 때 발생하는 예외
   * 즉, [org.springframework.web.bind.annotation.RequestBody] 태그가 붙은 파라미터가 null 일 때 발생한다
   *
   * Spring 내부에서 의도적으로 발생하였으므로 디버그 레벨에서만 로그를 남긴다
   *
   * @param e 발생한 예외
   */
  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorInfo> {
    log.debug("Request body is null", e)

    return createErrorResponse(status = BAD_REQUEST, code = "EMPTY_BODY")
  }

  // ===================================================================================================================
  // 그 외 예외를 처리하는 메소드
  // ===================================================================================================================

  /**
   * 예외처리 되지 않은 모든 예외는 이 핸들러에서 처리한다
   *
   * 위의 메소드에서 처리되지 않은 모든 예외는 이곳에서 처리된다
   *
   * 처리되지 않은 오류이므로 로그를 에러 레벨로 남긴다
   *
   * @param e 발생한 예외
   */
  @ExceptionHandler(Exception::class)
  fun handleDefaultException(e: Exception): ResponseEntity<ErrorInfo> {
    log.error("Unhandled exception occurred", e)

    return createErrorResponse(status = INTERNAL_SERVER_ERROR)
  }

  // ===================================================================================================================
  // 데이터 파싱을 위해 분리된 내부 (private) 메소드
  //  ===================================================================================================================

  /**
   * [FieldError] 클래스의 데이터를 [ErrorInfo.additional] 형식에 맞게 반환하는 메소드
   *
   * @see [getAdditionalInfo]
   */
  private fun transformFieldErrorsToDetails(fieldErrors: List<FieldError>): ErrorAdditional {
    val fields = mutableMapOf<String, MutableList<Map<String, Any?>>>()

    for (error in fieldErrors) {
      val errors = mapOf("validation" to error.code) + getAdditionalInfo(error)

      fields
        .computeIfAbsent(error.field) { mutableListOf() }
        .add(errors)
    }

    return mapOf("fields" to fields)
  }

  /**
   * [FieldError] 의 코드 값에 따라 추가 정보를 반환하는 메소드
   *
   * [Size], [SpecialCharacterConstraint] 어노테이션은 값을 반환하며 그 외에는 `null`을 반환한다
   *
   * @see [getSizeAdditionalInfo]
   * @see [getSpecialCharacterAdditionalInfo]
   */
  private fun getAdditionalInfo(error: FieldError): Map<String, Any?> {
    return when (error.code) {
      Size::class.simpleName -> getSizeAdditionalInfo(error)
      SpecialCharacterConstraint::class.simpleName -> getSpecialCharacterAdditionalInfo(error)
      else -> mapOf()
    }
  }

  /**
   * [Size] 검증 오류의 상세 정보를 가져온다
   *
   * * [min] 의 경우 값이 `0`이면 `null`을 반환한다
   * * [max] 의 경우 값이 [Int.MAX_VALUE] 이면 `null`을 반환한다
   *
   * @see [Size]
   */
  private fun getSizeAdditionalInfo(error: FieldError): Map<String, Any?> {
    val min = error.arguments
      ?.last()
      ?.takeIf { it != 0 }

    val max = error.arguments
      ?.get(error.arguments!!.lastIndex - 1)
      ?.takeIf { it != Int.MAX_VALUE }

    return mapOf("min" to min, "max" to max)
  }

  /**
   * [SpecialCharacterConstraint] 검증 오류의 상세 정보를 가져온다
   *
   * 허용하는 특수 문자 목록을 반환한다
   *
   * @see [SpecialCharacterConstraint]
   */
  private fun getSpecialCharacterAdditionalInfo(error: FieldError): Map<String, Any?> {
    val allow = (error.arguments?.last() as? CharArray)?.toList()

    return mapOf("allow" to allow)
  }
}
