package ru.anokhin.rest

import jakarta.enterprise.inject.spi.BeanManager
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlin.properties.Delegates
import ru.anokhin.core.service.BookService

@Singleton
class Beans @Inject constructor(
    beanManager: BeanManager,
    bookService: BookService,
) {

    init {
        BEAN_MANAGER = beanManager
        BOOK_SERVICE = bookService
    }

    companion object {

        var BEAN_MANAGER: BeanManager by Delegates.notNull()
            private set

        var BOOK_SERVICE: BookService by Delegates.notNull()
            private set
    }
}
