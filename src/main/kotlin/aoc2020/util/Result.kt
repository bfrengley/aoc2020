package aoc2020.util

sealed class Result<out T, out E> {
    class Ok<T, E>(val value: T) : Result<T, E>()
    class Err<T, E>(val err: E) : Result<T, E>()

    fun isOk() = this is Ok
    fun isErr() = this is Err

    fun <T2> map(transformer: (T) -> T2): Result<T2, E> = when (this) {
        is Ok -> Ok(transformer(this.value))
        is Err -> Err(this.err)
    }

    fun <E2> mapError(transformer: (E) -> E2): Result<T, E2> = when (this) {
        is Err -> Err(transformer(this.err))
        is Ok -> Ok(this.value)
    }

    fun ok(): T? = when (this) {
        is Err -> null
        is Ok -> this.value
    }

    fun err(): E? = when (this) {
        is Err -> this.err
        is Ok -> null
    }

    override fun toString(): String = when (this) {
        is Ok -> "Ok(${this.value})"
        is Err -> "Err(${this.err})"
    }
}

typealias Ok<T, E> = Result.Ok<T, E>
typealias Err<T, E> = Result.Err<T, E>

fun <T, E> T?.okOr(err: E): Result<T, E> = when (this) {
    null -> Err(err)
    else -> Ok(this)
}

fun <T, E> T?.okOrElse(errFn: () -> E): Result<T, E> = when (this) {
    null -> Err(errFn())
    else -> Ok(this)
}
