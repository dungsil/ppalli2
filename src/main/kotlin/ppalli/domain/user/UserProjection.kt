package ppalli.domain.user

/**
 * 사용자 프로젝션 DTO
 *
 * JPA 에서 사용자 정보 중 공개 가능한 정보를 가져오기 위한 DTO
 *
 * @property id 사용자 식별자
 * @property username 사용자 계정
 */
interface UserProjection {
  val id: Long
  val username: String
}
