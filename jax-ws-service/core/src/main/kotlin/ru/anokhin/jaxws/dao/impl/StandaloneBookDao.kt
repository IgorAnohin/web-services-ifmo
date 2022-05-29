package ru.anokhin.jaxws.dao.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityTransaction
import jakarta.persistence.TypedQuery
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaDelete
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import ru.anokhin.jaxws.dao.BookDao
import ru.anokhin.jaxws.exception.DaoException
import ru.anokhin.jaxws.model.jpa.Book

class StandaloneBookDao : BookDao {

    lateinit var entityManager: EntityManager

    override fun save(entity: Book): Book = inTransaction { entityManager.merge(entity) }

    override fun findById(id: Long): Book = inTransaction {
        entityManager.find(Book::class.java, id)
            ?: run { throw DaoException("Entity Book(id=$id) could not be found") }
    }

    override fun findByCondition(
        params: Map<String, Any>,
        conditions: (CriteriaBuilder, CriteriaQuery<Book>, Root<Book>) -> Predicate,
    ): List<Book> = inTransaction {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Book> = cb.createQuery(Book::class.java)
        val root: Root<Book> = cq.from(Book::class.java)
        cq.where(conditions(cb, cq, root))

        val query: TypedQuery<Book> = entityManager.createQuery(cq)
        params.forEach { (key, value) -> query.setParameter(key, value) }

        return@inTransaction query.resultList
    }

    override fun remove(id: Long): Boolean = inTransaction {
        val cb = entityManager.criteriaBuilder
        val cd: CriteriaDelete<Book> = cb.createCriteriaDelete(Book::class.java)
        val root: Root<Book> = cd.from(Book::class.java)
        cd.where(
            cb.equal(
                root.get<Long>("id"),
                cb.parameter(Long::class.java, "id")
            )
        )

        val updated: Int = entityManager
            .createQuery(cd)
            .setParameter("id", id)
            .executeUpdate()

        return@inTransaction updated > 0
    }

    private inline fun <T> inTransaction(crossinline closure: () -> T): T {
        val transaction: EntityTransaction = entityManager.transaction

        transaction.begin()
        val result: T = try {
            closure()
        } catch (ex: Exception) {
            transaction.rollback()
            throw if (ex is DaoException) ex else DaoException("Exception happened during transactional execution", ex)
        }
        transaction.commit()

        return result
    }
}
