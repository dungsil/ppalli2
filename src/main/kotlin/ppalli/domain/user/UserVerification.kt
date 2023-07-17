package ppalli.domain.user

import jakarta.persistence.*
import java.time.Instant

/**
 * 사용자 검증을 위한 엔티티
 *
 * @property id 사용자 검증 레코드의 고유 식별자. 이 값은 자동으로 생성된다
 * @property user 관련 사용자 객체. 이는 N:1 관계
 * @property unverifiedEmail 사용자의 미인증 이메일 주소
 * @property verificationToken 사용자 검증과 관련된 검증 토큰
 * @property tokenExpiredAt 검증 토큰의 만료 타임스탬프
 * @property tokenSentAt 검증 토큰이 사용자에게 전송된 시간의 타임스탬프
 * @property verifiedAt 사용자 검증이 완료된 시간의 타임스탬프
 */
@Entity
@Table(name = "user_verifications")
@SequenceGenerator(name = "user_verifications__seq--id", allocationSize = 1, initialValue = 1)
class UserVerification(

  @Id
  @GeneratedValue(generator = "user_verifications__seq--id", strategy = GenerationType.SEQUENCE)
  val id: Long = 0,

  @ManyToOne
  @JoinColumn(name = "user_id")
  val user: User,

  @Column(name = "unverified_email")
  val unverifiedEmail: String,

  @Column(name = "verification_token")
  val verificationToken: String? = null,

  @Column(name = "token_expired_at")
  val tokenExpiredAt: Instant? = null,

  @Column(name = "token_sent_at")
  var tokenSentAt: Instant? = null,

  @Column(name = "verified_at")
  var verifiedAt: Instant? = null,
)
