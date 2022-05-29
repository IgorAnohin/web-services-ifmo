package ru.anokhin.rest.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(

    /**
     * One of a server-defined set of error codes.
     */
    val code: String,

    /**
     * A human-readable representation of the error.
     */
    val message: String,

    /**
     * The target of the error.
     */
    val target: String? = null,

    /**
     * An array of details about specific errors that led to this reported error.
     */
    val details: List<ErrorModel> = emptyList(),

    /**
     * An object containing more specific information than the current object about the error.
     */
    @SerialName("innererror")
    val innerError: InnerError? = null,
)
