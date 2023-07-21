package ppalli.domain.user

import ppalli.utils.PasswordUtils

/**
 * 사용자 비밀번호 캡슐화 객체
 *
 * 보안 상 비밀번호의 노출을 막고 비밀번호 암호화를 한곳에서 관리하기 위한 클래스
 *
 * 비밀번호 변경 시, 새로운 객체를 생성해야한다.
 *
 * 비밀번호 암호화는 Spring security에서 제공하는 [org.springframework.security.crypto.password.DelegatingPasswordEncoder]를
 * 이용하여 암호화하며 그에 따른 비밀번호 매치, 신규 알고리즘으로 마이그레이션 등의 함수를 제공한다.
 *
 * @property encryptedPassword 암호화된 비밀번호
 * @see [passwordEncoder] 비밀번호 암호화 인코더
 */
@JvmInline
value class UserPassword private constructor(private val encryptedPassword: String) {
  override fun toString(): String = "[masked]" // 의도치 않은 비밀번호 노출을 방지하기 위해 비밀번호를 마스킹한다.

  /**
   * 저장된 비밀번호의 [rawPassword] 일치 여부
   *
   * @param [rawPassword] 확인할 비밀번호
   * @return 동일한 비밀번호인 경우 `true`
   */
  fun matches(rawPassword: String): Boolean {
    return PasswordUtils.matches(rawPassword, this.encryptedPassword)
  }

  companion object {
    /**
     * 입력받은 평문 비밀번호를 암호화된 비밀번호로 변환하여 저장하는 [UserPassword] 객체를 반환한다.
     *
     * @param rawPassword 평문 비밀번호
     */
    @JvmStatic
    fun of(rawPassword: String): UserPassword {
      return UserPassword(
        encryptedPassword = PasswordUtils.encrypt(rawPassword)
      )
    }
  }
}
