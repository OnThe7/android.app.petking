package io.lowapple.app.android.petking.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCaseNonParam<R>(private val dispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(dispatcher) {
                execute().let { Result.success(it) }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}