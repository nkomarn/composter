package kyta.composter.entity.data

import io.netty.buffer.ByteBuf

enum class DataType(val serialId: Int) {
    BYTE(0),
    BOOLEAN(0),
    SHORT(1),
    INTEGER(2),
    FLOAT(3),
    STRING(4),
    ITEM_STACK(5),
    POSITION(6),
}

fun DataType.write(output: ByteBuf, value: Any) {
    when (this) {
        DataType.BYTE -> output.writeByte(value as Int)
        DataType.BOOLEAN -> output.writeBoolean(value as Boolean)
        DataType.SHORT -> output.writeShort(value as Int)
        else -> throw UnsupportedOperationException()
    }
}