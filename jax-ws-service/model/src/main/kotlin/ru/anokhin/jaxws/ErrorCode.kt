package ru.anokhin.jaxws

sealed class ErrorCode(val code: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErrorCode
        if (code != other.code) return false
        return true
    }

    override fun hashCode(): Int = code.hashCode()

    override fun toString(): String = "ErrorCode(code='$code')"
}
