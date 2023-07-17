package ppalli.web.rest.user

import jakarta.validation.constraints.Size
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ppalli.domain.user.UserRepository
import ppalli.validatior.RequireAlphabet
import ppalli.validatior.RequireNumber
import ppalli.web.error.ErrorCode.*


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("I0001. 사용자 REST API 서비스 통합 테스트")
class UserRestServiceIntegrationTest {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var userRestService: UserRestService

  @Autowired
  private lateinit var mock: MockMvc

  @BeforeEach
  fun setUp() {
    userRepository.deleteAll()
  }

  @Test
  @DisplayName("C01. 사용자 생성시 정상 요청이면 `Created` 응답")
  fun createUser_withValidData_shouldReturnCreated() {
    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content(createUserSpecJson())
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.username", `is`("testuser")))
  }

  @Test
  @DisplayName("C02. 사용자 생성시 요청 BODY 가 비어있는 경우 `EMPTY_BODY` 응답")
  fun createUser_withEmptyBody_shouldReturnEmptyBody() {
    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content("{}")
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.code", `is`(EMPTY_BODY.name)))
  }

  @Test
  @DisplayName("C03. 사용자 생성시 사용자 계정이 중복되는 경우 `ALREADY_USERNAME` 응답")
  fun createUser_withAlreadyExistingUsername_shouldReturnAlreadyUsername() {
    // 중복 데이터 입력
    userRestService.createUser(CreateUserSpec("already", "d7QaN4Os@oydTPtQLg", "already@example.com"))

    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content(createUserSpecJson(username = "already"))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.code", `is`(ALREADY_USERNAME.name)))
      .andExpect(jsonPath("$.username", `is`("already")))
  }

  @Test
  @DisplayName("C04. 사용자 생성시 사용자 계정이 비어있으면 `VALIDATION_FAILED` 응답")
  fun createUser_withEmptyUsername_shouldReturnValidationFailed() {
    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content(createUserSpecJson(username = ""))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.code", `is`(VALIDATION_FAILED.name)))
      .andExpect(jsonPath("$.fields.length()", equalTo(1)))
      .andExpect(
        jsonPath(
          "$.fields.username[*].validation",
          containsInAnyOrder(
            Size::class.simpleName,
            RequireAlphabet::class.simpleName,
            RequireNumber::class.simpleName
          )
        )
      )
  }

  @Test
  @DisplayName("C05. 사용자 생성시 사용자 계정이 20 자를 초과하면 `VALIDATION_FAILED` 응답")
  fun createUser_withOver20CharactersUsername_shouldReturnValidationFailed() {
    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content(createUserSpecJson(username = "a".repeat(21)))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.code", `is`(VALIDATION_FAILED.name)))
      .andExpect(jsonPath("$.fields.length()", equalTo(1)))
      .andExpect(
        jsonPath(
          "$.fields.username[*].validation",
          containsInAnyOrder(
            Size::class.simpleName
          )
        )
      )
  }

  @Test
  @DisplayName("C06. 사용자 생성시 사용자 계정이 특수문자로만 이루어져 있으면 `VALIDATION_FAILED` 응답")
  fun createUser_withOnlySpecialCharacterUsername_shouldReturnValidationFailed() {
    mock.perform(
      post("/users")
        .contentType(APPLICATION_JSON)
        .content(createUserSpecJson(username = "---____----"))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.code", `is`(VALIDATION_FAILED.name)))
      .andExpect(jsonPath("$.fields.length()", equalTo(1)))
      .andExpect(
        jsonPath(
          "$.fields.username[*].validation",
          containsInAnyOrder(
            RequireAlphabet::class.simpleName,
            RequireNumber::class.simpleName
          )
        )
      )
  }

  private fun createUserSpecJson(
    username: String = "testuser",
    password: String = "d1SDUabmrSbhWz@",
    email: String = "testuser@example.com"
  ): String {
    return """{ "username":"$username", "password":"$password", "email":"$email" }"""
  }
}
