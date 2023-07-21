package ppalli.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import org.hibernate.validator.constraints.CompositionType
import org.hibernate.validator.constraints.ConstraintComposition
import kotlin.reflect.KClass

/**
 * 대상 문자열이 영어 알파벳 혹은 숫자를 포함하는 지 검사한다
 *
 * @see [RequireAlphabet]
 * @see [RequireNumber]
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@ConstraintComposition(CompositionType.OR)
@RequireAlphabet
@RequireNumber
annotation class RequireAlphabetOrNumber(
  val message: String = "",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
