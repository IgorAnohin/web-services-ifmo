package ru.anokhin.jaxws.util

import jakarta.xml.bind.annotation.adapters.XmlAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

class LocalDateXmlAdapter : XmlAdapter<Date, LocalDate>() {

    override fun unmarshal(value: Date): LocalDate = LocalDateTime.from(value.toInstant()).toLocalDate()

    override fun marshal(value: LocalDate): Date = Date.from(value.atStartOfDay().toInstant(ZoneOffset.UTC))
}
