package kyta.composter.entity.data

data class DataDescriptor<T>(
    val networkId: Int,
    val type: DataType,
    val initialValue: T,
)

data class DataValue<T>(
    val descriptor: DataDescriptor<T>,
    var value: T = descriptor.initialValue,
)

class SynchronizedEntityData {
    private val values = mutableMapOf<DataDescriptor<Any>, DataValue<Any>>()
    internal var dirty = true

    fun <T> getValue(descriptor: DataDescriptor<T>): T {
        return values[descriptor as DataDescriptor<Any>]!!.value as T
    }

    fun <T> setValue(descriptor: DataDescriptor<T>, value: T) {
        values[descriptor as DataDescriptor<Any>]!!.value = value as Any
        dirty = true
    }

    fun register(descriptor: DataDescriptor<Any>) {
        values[descriptor] = DataValue(descriptor)
    }
}

/*
fun <T> ByteBuf.writeMetaData(data: DataValue<T>, value: T) {
    val identifier = data.type.serialId shl 5 or (data.id and 31) and 0xFF

}
 */