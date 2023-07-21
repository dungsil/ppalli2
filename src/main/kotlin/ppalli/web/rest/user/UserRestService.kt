package ppalli.web.rest.user

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ppalli.domain.user.User
import ppalli.domain.user.UserPassword
import ppalli.domain.user.UserRepository
import ppalli.exception.client.DuplicateUsernameException

/**
 * 사용자 API 서비스
 *
 * [UserRestController]에서 라우팅된 요청을 처리하는 비지니스 로직을 가진다.
 */
@Service
class UserRestService(private val repo: UserRepository) {

  /**
   * 주어진 계정 정보로 사용자를 생성한다
   *
   * @param spec 사용자 생성 정보([CreateUserSpec])
   * @return 생성된 사용자의 공개 사용자 정보([UserInfo])
   * @throws DuplicateUsernameException 사용자 계정이 중복되면 발생하는 예외
   */
  @Transactional(dontRollbackOn = [DuplicateUsernameException::class])
  fun createUser(spec: CreateUserSpec): UserInfo {
    // 사용자 계정 중복 체크
    if (repo.existsByUsernameIgnoreCase(spec.username)) {
      throw DuplicateUsernameException(spec.username)
    }

    // 사용자 정보 저장
    val user = User.of(
      username = spec.username,
      password = UserPassword.of(spec.password) // 메소드 내부에서 비밀번호 암호화
    )

    // DB에 사용자 정보 저장
    repo.save(user)

    // 저장된 사용자 엔티티를 UserInfo 형식으로 변환한다.
    return UserInfo(user = user)
  }
}
