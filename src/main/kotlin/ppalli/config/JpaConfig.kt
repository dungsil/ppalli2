package ppalli.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ppalli.Ppalli

/**
 * Spring Data JPA 설정
 *
 * 이 클래스는 PPALLI 어플리케이션의 JPA 와 관련된 설정으로
 * 엔티티 스캔, JPA 저장소 스캔, JPA 감사 기능 활성화한다
 *
 * @see [ppalli.domain]
 */
@Configuration
@EntityScan(basePackageClasses = [Ppalli::class])
@EnableJpaRepositories(basePackageClasses = [Ppalli::class])
@EnableJpaAuditing(modifyOnCreate = false)
class JpaConfig
