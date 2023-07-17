package ppalli.domain

import org.springframework.data.repository.CrudRepository

/**
 * 지정된 모든 엔터티를 저장한다
 *
 * @param entities 저장할 엔티티
 * @return 저장된 엔티티
 * @throws OptimisticLockingFailureException 하나 이상의 엔터티가 낙관적 잠금을 사용하고 지속성 저장소에 있는 것과
 *                                           다른 값을 가진 Version 특성이 있는 경우,
 *                                           또한 하나 이상의 엔터티가 있는 것으로 간주되지만
 *                                           데이터베이스에 없는 경우에도 throw 된다
 */
fun <T, ID> CrudRepository<T, ID>.saveAll(vararg entities: T): Iterable<T> = saveAll(entities.toList())
