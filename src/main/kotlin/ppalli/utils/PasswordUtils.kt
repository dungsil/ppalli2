package ppalli.utils

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

/**
 * 비밀번호 처리를 위한 공통 유틸리티
 *
 * 비밀번호를 암호화하고 암호화된 비밀번호가 평문 비밀번호와 일치하는지 확인하는 기능을 제공한다
 *
 * 추후, 암호화 알고리즘 업그레이드의 용이성을 위해 비밀번호 앞에 prefix가 붙으며
 * 알고리즘이 변경된 경우 prefix는 정수로 증가한다 (ex. "v1" -> "v2" -> "v3")
 * 동일한 알고리즘이나 세부 설정이 변경된 경우 prefix는 마이너 버전을 증가시킨다. (ex. "v1" -> "v1.1", "v1.9" -> "v1.10")
 *
 * **출력예시**:
 * ```
 * {id}encryptedPassword
 * ```
 *
 * **버전 별 예시**:
 * ```
 * // v1
 * {v1}$argon2id$v=19$m=16384,t=2,p=1$xTxUmsnixEraULyIT8+lCw$VytIxMDo1smvF7Yh/lTPGPXbFAK0axspkYbmSYAycyM
 * ```
 *
 *
 * @see [Argon2 specification](https://www.cryptolux.org/images/0/0d/Argon2.pdf)
 */
object PasswordUtils {
  const val DEFAULT_VERSION = "v1"

  private const val V1_SALT_BYTES = 16
  private const val V1_HASH_BYTES = 32
  private const val V1_PARALLELISM = 1
  private const val V1_MEMORY_COST = 1 shl 14
  private const val V1_ITERATIONS = 2

  val instance = DelegatingPasswordEncoder(
    DEFAULT_VERSION, // 기본 값으로 사용할 암호화 알고리즘 ID
    mapOf(
      "v1" to Argon2PasswordEncoder(V1_SALT_BYTES, V1_HASH_BYTES, V1_PARALLELISM, V1_MEMORY_COST, V1_ITERATIONS)
    )
  )

  /**
   * 비밀번호 암호화
   *
   * [rawPassword]를 암호화하여 암호화 알고리즘 버전을 prefix로 붙여서 리턴한다.
   *
   * **사용 예시**:
   * ```kotlin
   * PasswordUtils.encryptPassword("P@ssw0rd") // 출력: {v1}$argon2id$v=19$m=16384,t=2,p=1$xTxUmsnixEraULyIT8+lCw$VytIxMDo1smvF7Yh/lTPGPXbFAK0axspkYbmSYAycyM
   * ```
   *
   * @param rawPassword 암호화 대상 평문 비밀번호
   * @return 암호화된 비밀번호
   */
  fun encrypt(rawPassword: String): String {
    return instance.encode(rawPassword)
  }

  /**
   * 평문 비밀번호가 암호화된 비밀번호와 일치하는 지 여부
   *
   * @param rawPassword 일치 여부를 확인할 평문 비밀번호
   * @param encryptedPassword 비교할 암호화된 비밀번호
   * @return [rawPassword]가 [encryptedPassword]와 일치하면 `true`, 아니면 `false`
   */
  fun matches(rawPassword: String, encryptedPassword: String): Boolean {
    return instance.matches(rawPassword, encryptedPassword)
  }
}
