package me.haroldmartin.base91

@Suppress("MagicNumber")
abstract class AbstractBase91Encoder(jsonMode: Boolean) {
    private val table = if (jsonMode) CodingTables.BASE91_JSON_ENCODE else CodingTables.BASE91_ENCODE
    private var ebq = 0
    private var en = 0

    abstract fun write(byte: Byte)

    fun encode(b: Byte) {
        ebq = ebq or (b.toInt() and 255 shl en)
        en += 8
        if (en > 13) {
            var ev = ebq and 8191
            if (ev > 88) {
                ebq = ebq shr 13
                en -= 13
            } else {
                ev = ebq and 16383
                ebq = ebq shr 14
                en -= 14
            }
            write(table[ev % 91])
            write(table[ev / 91])
        }
    }

    open fun finalize() {
        if (en > 0) {
            write(table[ebq % 91])
            if (en > 7 || ebq > 90) {
                write(table[ebq / 91])
            }
        }
    }

    open fun reset() {
        ebq = 0
        en = 0
    }
}
