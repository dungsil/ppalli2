package ppalli.web.rest.user

import jakarta.transaction.Transactional
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import ppalli.domain.user.UserRepository
import ppalli.web.error.ErrorCode
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("I0002. 사용자 관리 API 통합테스트")
internal class UserRestControllerTest {

  @Autowired
  lateinit var mock: MockMvc

  @Test
  @DisplayName("C01. 사용자를 생성할 때 정상상태면 CREATED 를 반환한다")
  fun createUser_withNormal_shouldCreated() {
    mockCreateUser()
      .andExpect {
        status { isCreated() }
        jsonPath("$.id", greaterThanOrEqualTo(10000000000000))
        jsonPath("$.username", `is`("test-user"))
        content { not(contains("28ia5pG4Db@@")) }
      }
  }

  @Test
  @DisplayName("C02. 사용자를 생성할 때 사용자 계정이 중복되면 `DUPLICATE_USERNAME`을 반환한다")
  fun createUser_withDuplicateUsername_shouldDuplicateUsername() {
    // 중복 상태를 만들기 위한 호출
    mockCreateUser().andDo {}

    // 실제 테스트
    mockCreateUser()
      .andExpect {
        status { isBadRequest() }

        jsonPath("$.code", `is`(ErrorCode.DUPLICATE_USERNAME.name))
        jsonPath("$.username", `is`("test-user"))
      }
  }

  private fun mockCreateUser(): ResultActionsDsl {
    return mock.post("/users") {
      contentType = MediaType.APPLICATION_JSON
      content = """{ "username": "test-user", "password": "28ia5pG4Db@" }"""
    }
  }
}
