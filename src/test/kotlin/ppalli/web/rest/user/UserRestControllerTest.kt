package ppalli.web.rest.user

import jakarta.transaction.Transactional
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import java.time.Instant.MIN
import kotlin.test.BeforeTest
import kotlin.test.Test

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
@Suppress("NonAsciiCharacters")
@DisplayName("사용자 API 통합 테스트")
class UserRestControllerIntegrationTest {

  @Autowired
  lateinit var mock: MockMvc

  @Autowired
  lateinit var service: UserRestService

  @BeforeTest
  fun setup() {
    service.createUser(CreateUserSpec(username = "registered", password = "pVwFycFVeprTB6nO!"))
  }

  @Test
  fun `사용자 생성 시 정상적으로 생성되어야 한다`() {
    mockUserApi("user01", "0u!vxt0Wg3U9xDz")
      .andExpect {
        status { isCreated() }

        jsonPath("$.id", greaterThan(0L))
        jsonPath("$.username", `is`("user01"))
        jsonPath("$.joinedAt", greaterThan(MIN.toString()))
      }
  }

  @Test
  fun `사용자 생성 시 사용자 계정이 동일하면 DUPLICATE_USERNAME 오류를 반환해야 한다`() {
    mockUserApi("registered", "qNp!BPB3Uc3ujD6JcbXlMa06")
      .andExpect {
        status { isBadRequest() }

        jsonPath("$.code", `is`("DUPLICATE_USERNAME"))
        jsonPath("$.username", `is`("registered"))
      }
  }

  @Test
  fun `사용자 생성 시 사용자 계정에 잘못된 특수문자가 들어가면 SpecialCharacterConstraint 오류를 반환해야 한다`() {
    mockUserApi("user.123", "EdGZhu7kELijaEbL2q!")
      .andExpect {
        status { isBadRequest() }

        jsonPath("$.code", `is`("VALIDATION_FAILED"))
        jsonPath("$.fields.username", hasSize<Int>(1))
        jsonPath("$.fields.username[0].validation", `is`("SpecialCharacterConstraint"))
        jsonPath("$.fields.username[0].allow[0]", `is`("-"))
        jsonPath("$.fields.username[0].allow[1]", `is`("_"))
      }
  }

  @Test
  fun `사용자 생성 시 사용자 계정 길이가 너무 짧으면 Size 오류를 반환해야한다`() {
    mockUserApi("u", "p@ssw0rd")
      .andExpect {
        status { isBadRequest() }

        jsonPath("$.code", `is`("VALIDATION_FAILED"))
        jsonPath("$.fields.username", hasSize<Int>(1))
        jsonPath("$.fields.username[0].validation", `is`("Size"))
        jsonPath("$.fields.username[0].min", `is`(3))
      }
  }

  @Test
  fun `사용자 생성 시 사용자 계정 길이가 너무 길면 Size 오류를 반환해야한다`() {
    mockUserApi("username-is-very-very-loooooooong", "92aJdwIeo3egfwkgCIxAZauG!")
      .andExpect {
        status { isBadRequest() }

        jsonPath("$.code", `is`("VALIDATION_FAILED"))
        jsonPath("$.fields.username", hasSize<Int>(1))
        jsonPath("$.fields.username[0].validation", `is`("Size"))
        jsonPath("$.fields.username[0].max", `is`(20))
      }
  }

  private fun mockUserApi(username: String, password: String): ResultActionsDsl {
    return mock.post("/users") {
      contentType = APPLICATION_JSON
      content = """{ "username": "$username", "password":"$password" }"""
    }
  }
}
