package me.haroldmartin.base91

import okio.Buffer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StreamingBase91Test {
    @Test
    fun testWriteByteArrays() {
        val byteArrayInputBuffer = Buffer()
        val byteArrayOutputStream = Buffer()
        val base91StreamEncoder = StreamingBase91Encoder(out = byteArrayOutputStream)
        byteArrayInputBuffer.writeUtf8("abcdefg")
        byteArrayInputBuffer.writeUtf8("higjklmn")
        base91StreamEncoder.write(byteArrayInputBuffer, byteArrayInputBuffer.size)
        base91StreamEncoder.close()
        val encoded: String = byteArrayOutputStream.readUtf8()
        val decoded: ByteArray = encoded.decodeBase91()
        assertEquals("abcdefghigjklmn", decoded.decodeToString())
    }

    @Test
    fun testRandomPaired() {
        val random = Random(RANDOM_SEED)
        for (i in 0..9999) {
            val bytes = ByteArray(random.nextInt(1000) + 100)
            random.nextBytes(bytes)

            val encodeInput = Buffer()
            val encodeOutput = Buffer()
            val base91StreamEncoder = StreamingBase91Encoder(out = encodeOutput)
            encodeInput.write(bytes)
            base91StreamEncoder.write(encodeInput, encodeInput.size)
            base91StreamEncoder.close()
            val encoded: String = encodeOutput.readUtf8()

            val decodeInput = Buffer()
            val decodeOutput = Buffer()
            val base91StreamDecoder = StreamingBase91Decoder(out = decodeOutput)
            decodeInput.writeUtf8(encoded)
            base91StreamDecoder.write(decodeInput, decodeInput.size)
            base91StreamDecoder.close()
            val decoded: ByteArray = decodeOutput.readByteArray()

            assertTrue(decoded.contentEquals(bytes))
        }
    }

    @Test
    fun testRandomOneshot() {
        val random = Random(RANDOM_SEED)
        for (i in 0..9999) {
            val bytes = ByteArray(random.nextInt(1000) + 100)
            random.nextBytes(bytes)

            val encodeInput = Buffer()
            val encodeOutput = Buffer()
            val base91StreamEncoder = StreamingBase91Encoder(out = encodeOutput)
            encodeInput.write(bytes)
            base91StreamEncoder.write(encodeInput, encodeInput.size)
            base91StreamEncoder.close()

            val decodeOutput = Buffer()
            val base91StreamDecoder = StreamingBase91Decoder(out = decodeOutput)
            base91StreamDecoder.write(encodeOutput, encodeOutput.size)
            base91StreamDecoder.close()
            val decoded: ByteArray = decodeOutput.readByteArray()

            assertTrue(decoded.contentEquals(bytes))
        }
    }

    companion object {
        private const val RANDOM_SEED: Long = 47
    }
}
