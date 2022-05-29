package ru.anokhin.jaxws.dao

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import ru.anokhin.jaxws.model.jpa.Book

interface BookDao {

    fun save(entity: Book): Book

    fun findById(id: Long): Book

    fun findByCondition(
        params: Map<String, Any>,
        conditions: (CriteriaBuilder, CriteriaQuery<Book>, Root<Book>) -> Predicate,
    ): List<Book>

    fun remove(id: Long): Boolean
}
