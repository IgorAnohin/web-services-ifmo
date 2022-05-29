package ru.anokhin.core

object ErrorCodes {

    object Books001UnknownError : ErrorCode("BOOKS-001")

    object Books002NameIsBlank : ErrorCode("BOOKS-002")

    object Books003AuthorsListIsEmpty : ErrorCode("BOOKS-003")

    object Books004AuthorsIsBlank : ErrorCode("BOOKS-004")

    object Books005PublisherIsBlank : ErrorCode("BOOKS-005")

    object Books006PageCountIsNotPositive : ErrorCode("BOOKS-006")

    object Books007EntityNotFound : ErrorCode("BOOKS-007")
}
