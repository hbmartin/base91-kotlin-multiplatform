package me.haroldmartin.base91

@Suppress("MagicNumber")
abstract class AbstractBase91Decoder(jsonMode: Boolean) {
    private val table = if (jsonMode) CodingTables.BASE91_JSON_DECODE else CodingTables.BASE91_DECODE
    private var dbq = 0
    private var dn = 0
    private var dv = -1

    abstract fun write(byte: Byte)

    fun decode(c: Char) {
        if (dv == -1) {
            dv = table[c.code].toInt()
        } else {
            dv += table[c.code] * 91
            dbq = dbq or (dv shl dn)
            dn += if (dv and 8191 > 88) 13 else 14
            do {
                write(dbq.toByte())
                dbq = dbq shr 8
                dn -= 8
            } while (dn > 7)
            dv = -1
        }
    }

    open fun finalize() {
        if (dv != -1) {
            write((dbq or (dv shl dn)).toByte())
        }
    }

    open fun reset() {
        dbq = 0
        dn = 0
        dv = -1
    }
}
