package ppalli.domain.user

import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.FetchType.LAZY
import org.springframework.data.domain.Persistable
import ppalli.utils.generateTSID

/**
 * 사용자 엔티티
 *
 * @param id 사용자의 고유 식별자
 * @param username 사용자의 사용자 이름
 * @param verifiedEmail 사용자의 검증된 이메일 주소
 * @param verifications 사용자와 연관된 사용자 검증 목록
 * @param passwordAuthentication 사용자와 연관된 비밀번호 인증 정보
 * @param new JPA 내부적으로 신규 생성 여부를 판단하기 위한 플래그
 */
@Entity
@Table(name = "users")
class User private constructor(

  @Id
  @Column(name = "id", updatable = false)
  val id: Long,

  @Column(name = "username", updatable = false)
  val username: String,

  @Column(name = "verified_email")
  var verifiedEmail: String? = null,

  @OneToMany(mappedBy = "user", fetch = LAZY, cascade = [ALL])
  private val verifications: MutableList<UserVerification> = mutableListOf(),

  @OneToOne(mappedBy = "user", fetch = LAZY, cascade = [ALL])
  private var passwordAuthentication: UserPasswordAuthentication? = null,

  @Transient
  private val new: Boolean = false, // DB 및 캐시에서 가져오면 무조건 [false]
) : Persistable<Long> {
  val unverifiedEmails: List<String>
    get() = this.verifications.map { it.unverifiedEmail }

  override fun getId(): Long = this.id
  override fun isNew(): Boolean = this.new

  fun addVerification(unverifiedEmail: String) {
    val verification = UserVerification(
      user = this,
      unverifiedEmail = unverifiedEmail,
    )

    this.verifications.add(verification)
  }

  fun changePassword(rawPassword: String) {
    if (this.passwordAuthentication != null) {
      this.passwordAuthentication!!.changePassword(rawPassword)
    } else {
      this.passwordAuthentication = UserPasswordAuthentication.of(user = this, rawPassword = rawPassword)
    }
  }

  companion object {
    @JvmStatic
    fun createUserWithPassword(
      username: String,
      rawPassword: String,
      unverifiedEmail: String,

      id: Long = generateTSID(),
    ): User = User(id = id, username = username, new = true)
      .apply { addVerification(unverifiedEmail) }
      .apply { changePassword(rawPassword) }
  }
}
