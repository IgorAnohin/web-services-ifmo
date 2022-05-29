package ru.anokhin.jaxws.exception

import jakarta.xml.ws.WebFault
import ru.anokhin.jaxws.ErrorCode
import ru.anokhin.jaxws.ErrorCodes

@WebFault(
    name = "ServiceException",
    faultBean = "ru.anokhin.jaxws.ErrorCode"
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
