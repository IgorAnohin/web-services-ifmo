package ru.anokhin.rest.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class InnerError(

    /**
     * A more specific error code than was provided by the containing error.
     */
    val code: String,

    /**
     * An object containing more specific information than the current object about the error.
     */
    @SerialName("innererror")
    open val innerError: InnerError? = null,
)
