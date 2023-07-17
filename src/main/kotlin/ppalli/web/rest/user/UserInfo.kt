package ppalli.web.rest.user

import ppalli.domain.user.User

/**
 * 사용자 응답 정보 DTO
 *
 * 이 클래스는 API 응답에 사용되며 클라이언트 단에 공개적으로 노출가능한 정보만 표시한다.
 *
 * **사용예시**:
 * ```kotlin
 * // 응답: { "id": 1, "username": "user1", "email": "user1@example.com" }
 * val userInfo = UserInfo(id = 1, username = "user1", email = "user1@example.com")
 *
 * // 응답: { "id": 2, "username": "user2", "email": null }
 * val userInfoWithUser = UserInfo(user = User(id = 2, username = "user2"))
 * ```
 *
 * @property id 사용자 식별자
 * @property username 사용자 계정
 * @property email 사용자 이메일 주소
 */
data class UserInfo(
  val id: Long,
  val username: String,
  val email: String?
) {
  constructor(user: User) : this(
    id = user.id,
    username = user.username,
    email = user.verifiedEmail
  )
}
