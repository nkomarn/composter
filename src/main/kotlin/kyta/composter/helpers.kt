package kyta.composter

import kotlinx.coroutines.CoroutineScope

suspend fun <T> withContext(scope: CoroutineScope, block: suspend CoroutineScope.() -> T): T {
    return kotlinx.coroutines.withContext(scope.coroutineContext, block)
}

fun Double.asAbsoluteInt(): Int {
    return (this * 32.0).toInt()
}

fun Float.asRotation(): Float {
    return this * 256F / 360F
}