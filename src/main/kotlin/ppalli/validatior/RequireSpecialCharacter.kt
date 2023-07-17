package ppalli.validatior

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * 대상 문자열이 특수문자를 하나 이상 포함하는지 검사한다
 *
 * `null`은 유효하지 않은 것으로 간주한다
 *
 * **사용예시**:
 * ```kotlin
 * data class UserSpec (@RequireSpecialCharacter val password: String)
 * ```
 *
 * @param message 예외 발생시 출력할 메시지
 * @param groups 이 제약 조건이 속한 유효성 검사 그룹 (기본 값: 빈 배열)
 * @param payload 제약 조건과 연관된 페이로드 (기본 값: 빈 배열)
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RequireSpecialCharacter.Validator::class])
annotation class RequireSpecialCharacter(
  val message: String = "The string must contain at least one special character",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {

  class Validator : ConstraintValidator<RequireSpecialCharacter, String> {
    companion object {
      private const val SPECIAL_CHARACTER_PATTERN = "[^a-z0-9]"
      private val regex: Regex = Regex(SPECIAL_CHARACTER_PATTERN, IGNORE_CASE)
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
      return value
        ?.contains(regex)
        ?: false
    }
  }
}
