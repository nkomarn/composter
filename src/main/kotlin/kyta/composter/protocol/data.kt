package kyta.composter.protocol

fun Double.asAbsoluteInt(): Int {
    return (this * 32.0).toInt()
}

fun Float.asRotation(): Int {
    return (this * 256F / 360F).toInt()
}