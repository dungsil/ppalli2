package ppalli.utils

import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("NonAsciiCharacters")
@DisplayName("JsonUtils 유닛 테스트")
internal class JsonUtilsTest {
  private class JsonObj(val name: String, val age: Int, val displayName: String)

  private val obj = JsonObj(name = "Chance Tidwell", age = 15, displayName = "청춘예찬")
  private val json = """{"name":"Chance Tidwell","age":15,"displayName":"청춘예찬"}"""

  @Test
  fun `객체를 직렬화하면 올바르게 JSON을 반환해야 함`() {
    assertEquals(json, JsonUtils.toJson(obj))
  }

  @Test
  fun `JSON을 역직렬화하면 올바르게 객체를 반환해야함`() {
    val transformedObject = JsonUtils.fromJson<JsonObj>(json)

    assertEquals(obj.name, transformedObject.name)
    assertEquals(obj.age, transformedObject.age)
    assertEquals(obj.displayName, transformedObject.displayName)
  }
}
