package me.haroldmartin.base91

class Base91Encoder(jsonMode: Boolean = false) : AbstractBase91Encoder(jsonMode) {
    private var didFinalize: Boolean = false
    private val result = mutableListOf<Byte>()

    fun encode(data: ByteArray): String {
        data.forEach { encode(it) }
        finalize()
        val result = readOutput()
        reset()
        return result
    }

    override fun write(byte: Byte) {
        result.add(byte)
    }

    @Throws(IllegalArgumentException::class)
    fun readOutput(): String {
        require(didFinalize) { "Must call finalize() before reading output" }
        return result.toByteArray().decodeToString()
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

fun ByteArray.encodeBase91(jsonMode: Boolean = false): String = Base91Encoder(jsonMode).encode(this)
