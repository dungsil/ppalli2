package ppalli.domain.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("U0001. 사용자 도메인 유닛 테스트")
internal class UserTest {

  val user = User.createUserWithPassword(
    username = "testuser",
    rawPassword = "testpassword",
    unverifiedEmail = "testuser@example.com"
  )

  @Test
  @DisplayName("C01. 사용자 생성 시 새로운 사용자임을 확인할 수 있음")
  fun `Generated user should be new`() {
    assertNotNull(user)
    assertEquals(true, user.isNew())
  }

  @Test
  @DisplayName("C10. 사용자 인증 요청 시 미인증된 이메일이 추가됨")
  fun `adding verification should increase unverified emails`() {
    // When
    user.addVerification("newemail@example.com")

    // Then
    assertEquals(2, user.unverifiedEmails.size)

    assertNull(user.verifiedEmail)
    assertEquals("newemail@example.com", user.unverifiedEmails.last())
  }
}
