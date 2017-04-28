package uy.kohesive.iac.model.aws.cloudtrail.utils

import com.amazonaws.util.DateUtils
import com.joestelmach.natty.Parser
import java.util.*

data class DateTime(
    val year: Int,
    val month: Int,
    val date: Int,
    val hrs: Int,
    val min: Int
) {

    companion object {
        fun parse(dateStr: String) = Calendar.getInstance().apply {
            time = parseDate(dateStr)
        }.let { calendar ->
            DateTime(
                year  = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                date  = calendar.get(Calendar.DAY_OF_MONTH),
                hrs   = calendar.get(Calendar.HOUR_OF_DAY),
                min   = calendar.get(Calendar.MINUTE)
            )
        }

        fun parseDate(dateStr: String): Date {
            try {
                return DateUtils.parseRFC822Date(dateStr)
            } catch (t: Throwable) {
                try {
                    return DateUtils.parseISO8601Date(dateStr)
                } catch (t: Throwable) {
                    return Parser().parse(dateStr).flatMap { group ->
                        group.dates
                    }.firstOrNull() ?: throw IllegalArgumentException("Unable to parse date $dateStr")
                }
            }
        }
    }

    fun getISO8601Date(): String = DateUtils.formatISO8601Date(Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, date)
        set(Calendar.HOUR_OF_DAY, hrs)
        set(Calendar.MINUTE, min)
    }.time)

}


