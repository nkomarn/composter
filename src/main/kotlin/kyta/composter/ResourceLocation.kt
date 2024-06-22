package kyta.composter

data class ResourceLocation(val namespace: String, val key: String) {
    constructor(key: String) : this("minecraft", key) // todo; parse colon syntax

    override fun toString(): String {
        return "$namespace:$key"
    }
}