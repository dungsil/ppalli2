package ppalli.web.rest.user

import ppalli.domain.user.User
import ppalli.domain.user.UserProjection

/**
 * 사용자 응답 정보 DTO
 *
 * 사용자 API에서 사용자 정보를 응답할 때 사용되는 DTO
 *
 * 공개적으로 노출되어도 괜찮은 정보만 포함된다.
 *
 * [UserInfo]는 [User] 및 [UserProjection]과 동일하거나 그 일부만을 포함한다
 *
 * @property id 사용자 식별자
 * @property username 사용자 계정
 *
 * @see [User]
 * @see [UserProjection]
 */
data class UserInfo(
  val id: Long,
  val username: String
) {
  constructor(user: User) : this (id = user.id, username = user.username)
  constructor(projection: UserProjection) : this(id = projection.id, username = projection.username)
}
