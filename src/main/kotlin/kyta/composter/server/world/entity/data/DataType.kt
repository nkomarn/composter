package kyta.composter.server.world.entity.data

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
