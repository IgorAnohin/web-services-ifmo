package ru.anokhin.jaxws.util

import jakarta.xml.bind.DatatypeConverter
import java.util.Date
import java.util.GregorianCalendar

class DateXmlAdapter private constructor() {

    fun marshal(date: Date?): String? = DatatypeConverter.printDateTime(
        GregorianCalendar.getInstance()
            .apply { time = date }
    )

    fun unmarshal(xmlDate: String?): Date? = DatatypeConverter.parseDate(xmlDate).time
}
