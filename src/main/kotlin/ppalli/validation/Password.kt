package ppalli.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import kotlin.reflect.KClass

/**
 * 대상 문자열의 비밀번호 유효성을 검사한다
 *
 * 다음의 제약조건을 모두 만족해야한다:
 * - `null`은 허용하지 않는다
 * - 비밀번호는 8 자 이상이어야 한다 [Size.min]
 * - 비밀번호는 알파벳을 하나 이상 포함해야 한다 [RequireAlphabet]
 * - 비밀번호는 숫자를 하나 이상 포함해야 한다 [RequireNumber]
 * - 비밀번호는 특수문자를 하나 이상 포함해야 한다 [RequireSpecialCharacter]
 *
 * **사용예시**:
 * ```kotlin
 * class User {
 *    @Password
 *    val password: String
 * }
 * ```
 *
 * @param message 예외 발생시 출력할 메시지; 기본값은 빈 문자열
 * @param groups 이 제약 조건이 속한 유효성 검사 그룹 (기본 값: 빈 배열)
 * @param payload 제약 조건과 연관된 페이로드 (기본 값: 빈 배열)
 * @see [Size]
 * @see [RequireAlphabet]
 * @see [RequireNumber]
 * @see [RequireSpecialCharacter]
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@ConstraintComposition(CompositionType.AND)
@Size(min = 8)
@RequireAlphabet
@RequireNumber
@RequireSpecialCharacter
annotation class Password(
  val message: String = "",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
