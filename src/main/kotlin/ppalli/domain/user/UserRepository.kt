package ppalli.domain.user

import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository

@Primary
interface UserRepository : JpaRepository<User, Long> {
  fun findByUsernameIgnoreCase(username: String): User
}
