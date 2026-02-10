package kyta.composter.world.entity

import kyta.composter.server.world.entity.data.DataDescriptor
import kyta.composter.server.world.entity.data.DataType
import kyta.composter.server.world.entity.data.updateValue

private val DATA_COMMON_FLAGS = DataDescriptor<Number>(0, DataType.BYTE, 0)

var Entity.crouching: Boolean
    get() = getFlag(EntityFlag.CROUCHING)
    set(value) = setFlag(EntityFlag.CROUCHING, value)

fun Entity.getFlag(flag: EntityFlag): Boolean {
    return ((synchronizedData.getValue(DATA_COMMON_FLAGS).toInt() shr flag.mask) and 1) == 1
}

fun Entity.setFlag(flag: EntityFlag, value: Boolean) {
    synchronizedData.updateValue(DATA_COMMON_FLAGS) {
        if (value) {
            it.toInt() or (1 shl flag.ordinal)
        } else {
            it.toInt() and (1 shl flag.ordinal).inv()
        }
    }
}

enum class EntityFlag(internal val mask: Int) {
    ON_FIRE(0x01),
    CROUCHING(0x02),
    RIDING_ENTITY(0x04),
    UNKNOWN(0x08), // todo; wiki says this is sprinting, but that's for beta 1.8
    USING_ITEM(0x10),
}
