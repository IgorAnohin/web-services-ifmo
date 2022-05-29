package ru.anokhin.jaxws.service

import jakarta.jws.WebMethod
import jakarta.jws.WebParam
import java.util.Date
import ru.anokhin.jaxws.exception.ServiceException
import ru.anokhin.jaxws.model.dto.BookSoapDto

interface BookSoapService {

    @Throws(ServiceException::class)
    @WebMethod(operationName = "create")
    fun create(
        @WebParam(name = "name") name: String,
        @WebParam(name = "authors") authors: List<String>,
        @WebParam(name = "publisher") publisher: String,
        @WebParam(name = "publicationDate") publicationDate: Date,
        @WebParam(name = "pageCount") pageCount: Int,
    ): BookSoapDto

    @Throws(ServiceException::class)
    @WebMethod(operationName = "findById")
    fun findById(
        @WebParam(name = "id") id: Long,
    ): BookSoapDto

    @Throws(ServiceException::class)
    @WebMethod(operationName = "findByFilter")
    fun findByFilter(
        @WebParam(name = "name") name: String? = null,
        @WebParam(name = "authors") author: String? = null,
        @WebParam(name = "publisher") publisher: String? = null,
        @WebParam(name = "publicationDateFrom") publicationDateFrom: Date? = null,
        @WebParam(name = "publicationDateTo") publicationDateTo: Date? = null,
        @WebParam(name = "pageCountFrom") pageCountFrom: Int? = null,
        @WebParam(name = "pageCountTo") pageCountTo: Int? = null,
    ): List<BookSoapDto>

    @Throws(ServiceException::class)
    @WebMethod(operationName = "update")
    fun update(
        @WebParam(name = "id") id: Long,
        @WebParam(name = "name") name: String,
        @WebParam(name = "authors") authors: List<String>,
        @WebParam(name = "publisher") publisher: String,
        @WebParam(name = "publicationDate") publicationDate: Date,
        @WebParam(name = "pageCount") pageCount: Int,
    ): BookSoapDto

    @Throws(ServiceException::class)
    @WebMethod(operationName = "deleteById")
    fun deleteById(
        @WebParam(name = "id") id: Long,
    ): Boolean
}
