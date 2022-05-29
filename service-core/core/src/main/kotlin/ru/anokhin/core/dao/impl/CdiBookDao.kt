package ru.anokhin.core.dao.impl

import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.TypedQuery
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaDelete
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import ru.anokhin.core.dao.BookDao
import ru.anokhin.core.model.jpa.Book

@Singleton
class CdiBookDao : BookDao {

    @PersistenceContext(unitName = "ru.anokhin.jaxws")
    lateinit var entityManager: EntityManager

    @Transactional
    override fun save(entity: Book): Book = entityManager.merge(entity)

    @Transactional
    override fun findById(id: Long): Book? = entityManager.find(Book::class.java, id)

    @Transactional
    override fun findByCondition(
        params: Map<String, Any>,
        conditions: (CriteriaBuilder, CriteriaQuery<Book>, Root<Book>) -> Predicate,
    ): List<Book> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Book> = cb.createQuery(Book::class.java)
        val root: Root<Book> = cq.from(Book::class.java)
        cq.where(conditions(cb, cq, root))

        val query: TypedQuery<Book> = entityManager.createQuery(cq)
        params.forEach { (key, value) -> query.setParameter(key, value) }

        return query.resultList
    }

    @Transactional
    override fun remove(id: Long): Boolean {
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

        return updated > 0
    }
}
