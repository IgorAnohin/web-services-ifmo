package ru.anokhin.core.exception

import jakarta.xml.ws.WebFault
import ru.anokhin.core.ErrorCode
import ru.anokhin.core.ErrorCodes

@WebFault(
    name = "ServiceException",
    faultBean = "ru.anokhin.core.ErrorCode"
)
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

    fun getFaultInfo(): ErrorCode = code
}
