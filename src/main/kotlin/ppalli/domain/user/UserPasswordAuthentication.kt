package ppalli.domain.user

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import ppalli.utils.passwordEncoder
import java.time.Instant

/**
 * 사용자 비밀번호 인증 엔티티
 *
 * 사용자가 비밀번호 인증을 설정한 경우 관련 정보를 저장하고 관리하는 역할을 한다
 *
 * @property user 해당 비밀번호 인증
 * @property id 이 비밀번호 인증의 고유한 식별자로 [User.id] 와 동일한다
 * @property encryptedPassword 사용자의 암호화된 비밀번호
 * @property failedLoginCount 사용자의 로그인 실패 횟수
 * @property lastFailedLoginAt 사용자의 마지막 로그인 실패 시간
 * @property lastLoginAt 사용자의 마지막 성공적인 로그인 시간
 * @property passwordChangedAt 사용자의 마지막 비밀번호 변경 시간, 변경되지 않았다면 null
 */
@Entity
@Table(name = "user_password_authentications")
@EntityListeners(AuditingEntityListener::class)
class UserPasswordAuthentication private constructor(
  @MapsId
  @OneToOne
  @JoinColumn(name = "user_id")
  val user: User,

  @Id
  private val id: Long? = user.id,

  @Column(name = "encrypted_password")
  private var encryptedPassword: String,

  @Column(name = "failed_login_count")
  var failedLoginCount: Short = 0,

  @Column(name = "last_failed_login_at")
  var lastFailedLoginAt: Instant? = null,

  @Column(name = "last_login_at")
  var lastLoginAt: Instant? = null,

  @Column(name = "password_changed_at")
  var passwordChangedAt: Instant,
) : UserAuthentication {

  fun changePassword(rawPassword: String) {
    this.encryptedPassword = passwordEncoder.encode(rawPassword)
    this.passwordChangedAt = Instant.now()

    this.failedLoginCount = 0
    this.lastFailedLoginAt = null
  }

  companion object {
    fun of(user: User, rawPassword: String): UserPasswordAuthentication {
      return UserPasswordAuthentication(
        user = user,
        encryptedPassword = passwordEncoder.encode(rawPassword),
        passwordChangedAt = Instant.now()
      )
    }
  }
}
