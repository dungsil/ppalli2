package ppalli.utils

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.util.StdDateFormat

/**
 * JSON 직렬화/역직렬화 유틸리티
 *
 *
 */
object JsonUtils {
  /**
   * JSON 직렬화/역직렬화를 위한 공통 인스턴스
   *
   * @return IM 에서 알맞게 사용되게 커스텀 된 [ObjectMapper] 인스턴스
   * @see ObjectMapper
   */
  val instance: ObjectMapper = ObjectMapper()
    .findAndRegisterModules()
    .apply { dateFormat = StdDateFormat.instance } // ISO 8601 형식으로 날짜 직렬화
    .apply { propertyNamingStrategy = LOWER_CAMEL_CASE } // camelCase 로 키 (속성) 를 직렬화
    .apply { setDefaultPropertyInclusion(NON_NULL) } // null 값은 Json 에 포함되지 않음
    .apply { configure(FAIL_ON_UNKNOWN_PROPERTIES, false) } // 알 수 없는 속성은 무시
    .apply { configure(WRITE_DATES_AS_TIMESTAMPS, false) } // 날짜를 타임스탬프로 직렬화하지 않음
    .apply { configure(WRITE_DURATIONS_AS_TIMESTAMPS, false) } // 기간을 타임스탬프로 직렬화하지 않음

  /**
   * 객체를 JSON 문자열로 직렬화한다
   *
   * @param obj 직렬화할 객체
   * @return 직렬화된 JSON 문자열
   */
  fun <T : Any> toJson(obj: T): String = instance.writeValueAsString(obj)

  /**
   * JSON 문자열을 객체로 역직렬화한다
   *
   * NOTE: JVM 런타임 내에서 타입 소거 후에도 문제가 발생하지 않도록 인라인 클래스(inline class)로 정의해
   * 컴파일 시 각 호출 별로 타입을 검사하여 안정성을 높인다.
   *
   * @param json 역직렬화할 JSON 문자열
   * @return 역직렬화된 객체
   */
  inline fun <reified T : Any> fromJson(json: CharSequence): T =
    instance.readValue(json.toString(), object : TypeReference<T>() {})
}
