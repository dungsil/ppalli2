package ppalli.utils

import org.junit.jupiter.api.DisplayName
import ppalli.utils.PasswordUtils.DEFAULT_VERSION
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
@DisplayName("PasswordUtils 유닛 테스트")
internal class PasswordUtilsTest {

  @Test
  fun `비밀번호를 암호화한 경우 암호화된 비밀번호는 버전 프리픽스를 포함해야함`() {
    val encryptedPassword = PasswordUtils.encrypt("2uZDgsTCcwIiVwWilAv6cP")

    assertTrue { encryptedPassword.startsWith("{$DEFAULT_VERSION}") }
  }

  @Test
  fun `비밀번호를 암호화한 경우 암호화된 비밀번호는 평문 비밀번호와 일치하면 안됨`() {
    val rawPassword = "fBJ03TOGecAqvq8Dtc5Tzp"
    val encryptedPassword = PasswordUtils.encrypt(rawPassword)

    assertNotEquals(rawPassword, encryptedPassword)
  }

  @Test
  fun `비밀번호를 암호화한 경우 암호화된 비밀번호는 동일한 비밀번호와 다른 값이 출력되어야 함`() {
    val rawPassword = "password"

    val encryptedPassword = PasswordUtils.encrypt(rawPassword)
    val anotherEncryptedPassword = PasswordUtils.encrypt(rawPassword)

    assertNotEquals(anotherEncryptedPassword, encryptedPassword)
  }

  @Test
  fun `비밀번호를 검증하는 경우 두 값이 동일하면 'true'를 리턴해야 함`() {
    val rawPassword = "tLNzOrZrEh"
    val encryptedPassword = PasswordUtils.encrypt(rawPassword)

    assertTrue { PasswordUtils.matches(rawPassword, encryptedPassword) }
  }

  @Test
  fun `비밀번호를 검증하는 경우 두 값이 동일하지 않으면 'false'를 리턴해야 함`() {
    val rawPassword = "tLNzOrZrEh"
    val wrongPassword = rawPassword + "123"
    val encryptedPassword = PasswordUtils.encrypt(rawPassword)

    assertFalse { PasswordUtils.matches(wrongPassword, encryptedPassword) }
  }
}
