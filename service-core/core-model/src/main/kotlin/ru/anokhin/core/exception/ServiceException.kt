package ru.anokhin.core.exception

import ru.anokhin.core.ErrorCode
import ru.anokhin.core.ErrorCodes

class ServiceException : Exception {

    @Suppress("MemberVisibilityCanBePrivate")
    val code: ErrorCode

    constructor(message: String?, code: ErrorCode, cause: Throwable?) : super(message, cause) {
        this.code = code
    }

    constructor(message: String?, code: ErrorCode) : super(message) {
        this.code = code
    }

    constructor(code: ErrorCode, cause: Throwable?) : super(cause) {
        this.code = code
    }

    constructor(code: ErrorCode) : this(code = code, cause = null)

    constructor(cause: Throwable) : this(code = ErrorCodes.Books001UnknownError, cause)
}
