package kyta.composter.server.world.entity.data

data class DataDescriptor<T>(
    val networkId: Int,
    val type: DataType,
    val initialValue: T,
)

class SynchronizedEntityData {
    internal val values = mutableMapOf<DataDescriptor<*>, Any>()
    internal var dirty = true

    fun <T> getValue(descriptor: DataDescriptor<T>): T {
        return values[descriptor]?.let { it as T }
            ?: descriptor.initialValue
    }

    fun <T> setValue(descriptor: DataDescriptor<T>, value: T) {
        values[descriptor] = value as Any
        dirty = true
    }

    fun register(descriptor: DataDescriptor<out Any>) {
        values[descriptor] = descriptor.initialValue
    }
}

inline fun <T> SynchronizedEntityData.updateValue(descriptor: DataDescriptor<T>, block: (T) -> T) {
    setValue(descriptor, block.invoke(getValue(descriptor)))
}
