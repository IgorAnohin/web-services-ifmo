package ru.anokhin.jaxws.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDate
import ru.anokhin.jaxws.util.LocalDateXmlAdapter

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class BookSoapDto {

    var id: Long? = null

    var name: String? = null

    var authors: List<String> = emptyList()

    var publisher: String? = null

    @XmlJavaTypeAdapter(LocalDateXmlAdapter::class)
    var publicationDate: LocalDate? = null

    var pageCount: Int? = null
}
