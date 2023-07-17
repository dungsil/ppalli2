package ppalli.web.rest.user

import org.springframework.stereotype.Service
import ppalli.domain.user.User

/**
 * [UserRestController] 를 통해 라우팅된 요청을 처리하는 서비스 ([Service]) 클래스
 */
@Service
class UserRestService(private val repo: UserRestRepository) {

  /**
   * 전달 받은 [spec] 의 정보를 토대로 새로운 사용자를 생성한다
   *
   * @param spec 새로운 사용자를 생성하기 위한 정보
   *              - [spec.username] : 새로운 사용자의 이름
   *              - [spec.password] : 새로운 사용자의 비밀번호
   *              - [spec.email] : 새로운 사용자의 이메일
   * @return 새로 생성된 사용자의 정보
   * @throws AlreadyUsernameException 만약 사용자 계정이 이미 존재한다면 발생한다
   */
  fun createUser(spec: CreateUserSpec): UserInfo {

    // 생성 전 검증
    if (repo.existsByUsernameIgnoreCase(spec.username)) {
      throw AlreadyUsernameException(spec.username)
    }

    // 실제 생성 로직
    val user = User.createUserWithPassword(
      username = spec.username,
      rawPassword = spec.password,
      unverifiedEmail = spec.email
    )
    repo.save(user)

    // 생성된 사용자 정보 반환
    return UserInfo(user)
  }
}
