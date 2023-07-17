package ppalli.utils

import io.hypersistence.tsid.TSID

/**
 * [TSID] 생성 인스턴스
 */
private val tsID: TSID.Factory = TSID.Factory.INSTANCE

/**
 * 타임스탬프 기반 ID(TSID) 를 생성한다
 *
 * 아래의 목적을 위해 사용된다
 * 1. 보안 상 공개적으로 순차적으로 ID 값을 노출하기 어렵지만 생성 순서대로 정렬이 필요한 경우
 * 2. 타임스탬프 기반으로 ID 값을 생성해야 하지만 동시성 문제가 예상될 때
 * 3. UUID 보다 빠른 속도로 ID 값을 생성해야 할 때
 *
 * 이 메소드는 18 자의 Long 타입 값을 반환하며, 앞의 10 자는 타임스탬프 뒤의 7 자는 특정 알고리즘에 따른 랜덤 값이다 (순서보장)
 * 타임스탬프 값은 밀리초 단위로 생성된다
 *
 * **사용예시**:
 * ```kotlin
 * val id = generateTSID() // 리턴 값 예시: 468864671994227199
 * ```
 *
 * @return 생성된 TSID 를 반환한다
 */
fun generateTSID(): Long = tsID.generate().toLong()
