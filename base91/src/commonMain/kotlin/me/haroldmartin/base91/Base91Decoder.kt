package me.haroldmartin.base91

class Base91Decoder(jsonMode: Boolean = false) : AbstractBase91Decoder(jsonMode) {
    private var didFinalize: Boolean = false
    private val result = mutableListOf<Byte>()

    fun decode(data: String): ByteArray {
        data.forEach { decode(it) }
        finalize()
        val result = readOutput()
        reset()
        return result
    }

    override fun write(byte: Byte) {
        result.add(byte)
    }

    @Throws(IllegalArgumentException::class)
    fun readOutput(): ByteArray {
        require(didFinalize) { "Must call finalize() before reading output" }
        return result.toByteArray()
    }

    override fun finalize() {
        super.finalize()
        didFinalize = true
    }

    override fun reset() {
        super.reset()
        didFinalize = false
        result.clear()
    }
}

fun String.decodeBase91(jsonMode: Boolean = false): ByteArray = Base91Decoder(jsonMode).decode(this)
