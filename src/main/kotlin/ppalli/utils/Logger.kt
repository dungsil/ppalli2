package ppalli.utils

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Logger 인스턴스를 생성하는 함수
 *
 * 내부적으로 `kotlin-logging-jvm` 라이브러리를 사용한다
 *
 * **사용 예시**:
 * ```kotlin
 * private val log = createLogger {}
 * ```
 *
 * @param func 현재 클래스의 이름을 가져오기 위한 빈 함수 해당 함수를 이용해 클래스의 이름을 가져온다
 * @return 이름이 지정된 Logger 인스턴스를 반환한다 이 Logger 는 로깅 작업에 사용할 수 있다
 */
fun createLogger(func: () -> Unit) = KotlinLogging.logger(func)
