package ppalli.validatior

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * 대상 문자열이 알파벳을 하나 이상 포함하는지 검사한다
 *
 * `null`은 유효하지 않은 것으로 간주한다
 *
 * **사용예시**:
 * ```kotlin
 * data class UserSpec (@RequireAlphabet val username: String)
 * ```
 *
 * @param message 예외 발생시 출력할 메시지
 * @param groups 이 제약 조건이 속한 유효성 검사 그룹이다 기본값은 빈 배열이다
 * @param payload 제약 조건과 연관된 페이로드이다 기본값은 빈 배열이다
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RequireAlphabet.Validator::class])
annotation class RequireAlphabet(
  val message: String = "The value should contain at least one alphabet character",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  class Validator : ConstraintValidator<RequireAlphabet, String> {
    companion object {
      private val regex = Regex("[a-z]", IGNORE_CASE)
    }


    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
      return value
        ?.contains(regex)
        ?: false
    }
  }
}
