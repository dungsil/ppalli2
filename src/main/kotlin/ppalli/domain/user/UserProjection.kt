package ppalli.domain.user

import java.time.Instant

/**
 * 사용자 프로젝션 DTO
 *
 * JPA 에서 사용자 정보 중 공개 가능한 정보를 가져오기 위한 DTO
 *
 * @property id 사용자 식별자
 * @property username 사용자 계정
 * @property createdAt 사용자 생성일시
 */
interface UserProjection {
  val id: Long
  val username: String
  val createdAt: Instant
}
