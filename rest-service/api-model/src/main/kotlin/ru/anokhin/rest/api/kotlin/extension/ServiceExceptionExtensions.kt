package ru.anokhin.rest.api.kotlin.extension

import ru.anokhin.core.ErrorCode
import ru.anokhin.core.ErrorCodeRepository
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.rest.api.model.ErrorResponse

private typealias ErrorModel = ru.anokhin.rest.api.model.Error

fun ServiceException.asErrorResponse(): ErrorResponse =
    ErrorResponse(
        error = ErrorModel(
            code = this.code.code,
            message = this.message ?: "null",
        )
    )

fun ErrorResponse.asServiceException(): ServiceException {
    val errorCode: ErrorCode = ErrorCodeRepository.findByCode(error.code)
        ?: error("Error code [${error.code}] was not found")
    val message: String? = error.message.takeIf { it != "null" }

    return ServiceException(message, errorCode)
        .also { it.stackTrace = emptyArray() }
}
