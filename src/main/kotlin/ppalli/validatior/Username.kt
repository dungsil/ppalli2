package ppalli.validatior

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import kotlin.reflect.KClass

/**
 * 대상 문자열이 사용자 계정 (username) 형식에 올바른지 검사한다
 *
 * 다음의 제약조건을 모두 만족해야한다:
 * - `null`은 허용하지 않는다
 * - 사용자 계정은 3 자 이상 20 자 이하여야 한다 [Size.min], [Size.max]
 * - 사용자 계정은 알파벳과 숫자가 포함되어야 한다 [RequireAlphabetOrNumber]
 * - 사용자 계정에는 하이픈 ('-') 및 언더스코어 ('_') 만 포함할 수 있다 [SpecialCharacterConstraint]
 *
 * **사용예시**:
 * ```kotlin
 * data class User(
 *     @Username
 *     val username: String
 * )
 * ```
 *
 * @param message 예외 발생시 출력할 메시지; 기본값은 빈 문자열이다
 * @param groups 이 제약 조건이 속한 유효성 검사 그룹이다 기본값은 빈 배열이다
 * @param payload 제약 조건과 연관된 페이로드이다 기본값은 빈 배열이다
 * @see [Size]
 * @see [RequireAlphabetOrNumber]
 * @see [SpecialCharacterConstraint]
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@ConstraintComposition(CompositionType.AND)
@Size(min = 3, max = 20)
@RequireAlphabetOrNumber
@SpecialCharacterConstraint
annotation class Username(
  val message: String = "",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
