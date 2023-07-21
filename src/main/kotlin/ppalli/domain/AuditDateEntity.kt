package ppalli.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import java.time.Instant

/**
 * 데이터의 생성 및 최근 수정일자를 추적하고 감사(監査)하는 테이플
 *
 * @property createdAt 생성일시
 * @property lastModifiedAt 최근수정일시
 */
@MappedSuperclass
open class AuditDateEntity(
  @Column(name = "created_at")
  protected val createdAt: Instant = Instant.now(),

  @Column(name = "last_modified_at")
  protected var lastModifiedAt: Instant? = null
) {

  /**
   * JPA 영속성 업데이트 이전 최근 수정 일시를 현재로 업데이트한다
   */
  @PreUpdate
  private fun preUpdate() {
    this.lastModifiedAt = Instant.now()
  }
}
