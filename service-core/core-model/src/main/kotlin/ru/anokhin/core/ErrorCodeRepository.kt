package ru.anokhin.core

object ErrorCodeRepository {

    fun findByCode(code: String): ErrorCode? =
        getAllErrorCodes().find { it.code == code }

    private fun getAllErrorCodes(): List<ErrorCode> = buildList {
        addAll(
            ErrorCodes::class
                .nestedClasses
                .mapNotNull { it.objectInstance }
                .filterIsInstance<ErrorCode>()
        )
    }
}
