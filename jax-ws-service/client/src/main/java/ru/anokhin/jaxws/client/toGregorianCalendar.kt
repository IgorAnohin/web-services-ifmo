package ru.anokhin.jaxws.client

import java.time.LocalDate
import java.util.Date
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

private val DATATYPE_FACTORY: DatatypeFactory = DatatypeFactory.newInstance()

fun Date.toGregorianCalendar(): XMLGregorianCalendar = GregorianCalendar()
    .apply { timeInMillis = toInstant().toEpochMilli() }
    .let(DATATYPE_FACTORY::newXMLGregorianCalendar)

fun LocalDate.toGregorianCalendar(): XMLGregorianCalendar = GregorianCalendar()
    .apply { timeInMillis = atStartOfDay().toInstant(java.time.ZoneOffset.UTC).toEpochMilli() }
    .let(DATATYPE_FACTORY::newXMLGregorianCalendar)
