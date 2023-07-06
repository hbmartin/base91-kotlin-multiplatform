package me.haroldmartin.base91

import okio.Buffer
import okio.BufferedSink
import okio.Sink
import okio.Timeout

class StreamingBase91Encoder(
    jsonMode: Boolean = false,
    private val out: BufferedSink,
) : AbstractBase91Encoder(jsonMode), Sink {
    override fun write(source: Buffer, byteCount: Long) {
        source.readByteArray(byteCount).forEach { encode(it) }
    }

    override fun write(byte: Byte) {
        out.writeByte(byte.toInt())
    }

    override fun close() {
        finalize()
        out.close()
    }

    override fun flush() = Unit

    override fun timeout(): Timeout = Timeout.NONE
}
