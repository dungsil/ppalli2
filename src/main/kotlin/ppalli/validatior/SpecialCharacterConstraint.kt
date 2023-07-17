package ppalli.validatior

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * 대상 문자열에 허용되는 특수문자만 사용되었는지 검증하는 노테이션
 *
 * 이 어노테이션은 대상 문자열에 알파벳, [allow] 에 정의된 문자열로만 이루워져있는지 확인한다
 *
 * `null`은 유효하지 않은 것으로 간주한다
 *
 * **사용 예시**:
 * ```kotlin
 * @SpecialCharacterConstraint // 하이픈 ('-'), 언더스코어 ('_') 만 허용
 * val myString: String
 *
 * @SpecialCharacterConstraint(allow = ['@', '#']) // 골뱅이 ('@'), 샵 ('#') 만 허용
 * var myOtherString: String
 * ```
 *
 * @param allow 허용할 특수문자 배열 기본값은 ['-', '_']
 * @param message 예외 발생시 출력할 메시지
 * @param groups 이 제약 조건이 속한 유효성 검사 그룹 (기본 값: 빈 배열)
 * @param payload 제약 조건과 연관된 페이로드 (기본 값: 빈 배열)
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SpecialCharacterConstraint.SpecialCharacterValidator::class])
annotation class SpecialCharacterConstraint(
  val allow: CharArray = ['-', '_'],

  val message: String = "The string can only contain alphabets, numbers, and allow characters",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
) {
  class SpecialCharacterValidator : ConstraintValidator<SpecialCharacterConstraint, String> {
    companion object {
      // '[^a-zA-Z0-9]'는 영문자, 숫자가 아닌것을 의미 '%s'에 사용자가 지정한 특수문자를 넣는다
      private const val ALLOW_CHARACTERS_PATTERN_TEMPLATE = "[^a-zA-Z0-9%s]"
    }

    private lateinit var regex: Regex

    override fun initialize(annotation: SpecialCharacterConstraint) {
      val specialCharacters = Regex.escape(annotation.allow.joinToString(""))
      this.regex = String.format(ALLOW_CHARACTERS_PATTERN_TEMPLATE, specialCharacters).toRegex()
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
      if (value == null) {
        return false
      }

      return !value.contains(regex)
    }
  }
}
