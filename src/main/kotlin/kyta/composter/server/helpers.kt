package kyta.composter.server

import kotlinx.coroutines.CoroutineScope

suspend fun <T> withContext(scope: CoroutineScope, block: suspend CoroutineScope.() -> T): T {
    return kotlinx.coroutines.withContext(scope.coroutineContext, block)
}
