package ppalli.web.rest.user

import ppalli.domain.user.UserRepository

/**
 * 사용자 RESTful API 를 위한 JPA 저장소 ([org.springframework.stereotype.Repository])
 *
 * 이 인터페이스는 [UserRepository] 를 상속받아 [UserInfo] 객체를 반환하는 메서드를 추가로 제공한다.
 *
 * @see [UserRepository]
 * @see [UserInfo]
 */
interface UserRestRepository : UserRepository {
  /**
   * 사용자 계정명이 일치하는 사용자가 존재하는지 여부를 반환한다.
   *
   * 대소문자는 구분하지 않는다.
   *
   * @param username 사용자 계정명
   * @return 사용자 계정명이 일치하는 사용자가 존재하는지 여부
   */
  fun existsByUsernameIgnoreCase(username: String): Boolean
}
