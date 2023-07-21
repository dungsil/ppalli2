package ppalli.domain.user

import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import ppalli.domain.AuditDateEntity
import ppalli.utils.IdGenerator.generateTSID
import java.time.Instant

/**
 * 사용자 엔티티
 *
 * @property id 사용자 식별자
 * @property username 사용자 계정
 * @property password 사용자 비밀번호 캡슐화 객체
 * @property createdAt 사용자 생성 일시
 * @property lastModifiedAt 최근 사용자 정보 변경일시
 * @property new 신규 사용자 여부; 객체 생성 시에만 `true`, DB에서 데이터를 가져오면 `false`로 고정된다.
 */
@Entity
@Table(name = "users")
class User private constructor(
  @Id
  @Column(name = "id", updatable = false)
  val id: Long,

  @Column(name = "username", updatable = false)
  val username: String,

  @Column(name = "encrypted_password")
  var password: UserPassword,

  @Transient // DB에 포함되지 않음
  val new: Boolean = false,
): Persistable<Long>, AuditDateEntity() {
  override fun getId(): Long = this.id
  override fun isNew(): Boolean = this.new


  /**
   * 사용자 가입일시
   *
   * [createdAt]와 동일한 값을 제공하는 alias 이다.
   */
  val joinedAt: Instant
    get() = this.createdAt

  companion object {
    /**
     * 입력받은 사용자 계정과 평문 비밀번호를 기준으로 [User] 객체를 생성한다.
     *
     * 입력받은 평문비밀번호는 이 시점에서 암호화된다.
     *
     * @param username 사용자 계정
     * @param password 사용자 계정의 비밀번호
     * @param id 사용자의 ID 값 (선택사항, 기본 값: TSID 생성)
     * @see [generateTSID]
     */
    @JvmStatic
    fun of(
      username: String,
      password: UserPassword,
    ): User {
      return User(
        id = generateTSID(),
        username = username,
        password = password
      )
    }
  }
}
