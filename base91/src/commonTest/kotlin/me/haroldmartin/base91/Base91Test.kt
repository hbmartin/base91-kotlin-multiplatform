package me.haroldmartin.base91

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class Base91Test {
    private val encodeDecodes: Map<String, String> = mapOf(
        "test" to "fPNKd",
        "Never odd or even\n" to "_O^gp@J`7RztjblLA#_1eHA",
        "May a moody baby doom a yam?\n" to "8D9Kc)=/2\$WzeFui#G9Km+<{VT2u9MZil}[A",
        "" to "",
        "a" to "GB",
    )

    @Test
    fun testDecode() {
        for (entry in encodeDecodes.entries) {
            assertEquals(entry.key, entry.value.decodeBase91().decodeToString())
        }
    }

    @Test
    fun testEncode() {
        for (entry in encodeDecodes.entries) {
            assertEquals(entry.value, entry.key.encodeToByteArray().encodeBase91())
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testRandomWithJson() {
        val random = Random(RANDOM_SEED)
        var encodedSize = 0
        var base64Size = 0
        var plainSize = 0
        var worstEncodingRatio = Double.MIN_VALUE
        var bestEncodingRatio = Double.MAX_VALUE
        for (i in 0..9999) {
            val bytes = ByteArray(random.nextInt(1000) + 100)
            random.nextBytes(bytes)
            val encode: String = bytes.encodeBase91()
            val encodeJson: String = bytes.encodeBase91(jsonMode = true)
            val decode: ByteArray = encode.decodeBase91()
            val decodeJson: ByteArray = encodeJson.decodeBase91(jsonMode = true)
            val base64 = Base64.Default.encode(bytes)

            assertFalse(encode.contains("'"))
            assertFalse(encodeJson.contains("\""))
            assertTrue(decode.contentEquals(bytes))
            assertTrue(decodeJson.contentEquals(bytes))

            plainSize += bytes.size
            encodedSize += encode.length
            base64Size += base64.length
            val encodingRatio = encode.length.toDouble() / bytes.size
            worstEncodingRatio = max(worstEncodingRatio, encodingRatio)
            bestEncodingRatio = min(bestEncodingRatio, encodingRatio)
        }

        val encodingRatio = encodedSize.toDouble() / plainSize
        val base64Ratio = base64Size.toDouble() / plainSize
        println("encoding ratio: $encodingRatio")
        println("base64 ratio: $base64Ratio}")
        println("worst encoding ratio: $worstEncodingRatio")
        println("best encoding ratio: $bestEncodingRatio")
        assertTrue(encodingRatio >= BEST_CASE_RATIO)
        assertTrue(encodingRatio <= WORST_CASE_RATIO)
        assertTrue(encodingRatio <= base64Ratio)
    }

    companion object {
        private const val WORST_CASE_RATIO = 1.2308
        private const val BEST_CASE_RATIO = 1.1429
        private const val RANDOM_SEED: Long = 4711
    }
}
