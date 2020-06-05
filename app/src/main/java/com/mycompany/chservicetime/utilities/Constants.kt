import com.mycompany.chservicetime.utilities.getFormatHourMinuteInt
import com.mycompany.chservicetime.utilities.getFormatHourMinuteString

/**
 * Constants used throughout the app.
 */
const val DATABASE_NAME = "CHServiceTime.db"

const val PREFERENCE_FILE_NAME = "CHPreference"

const val BEGIN_HOUR_DAY = 0
const val BEGIN_MINUTE_DAY = 0
const val END_HOUR_DAY = 23
const val END_MINUTE_DAY = 59

val BEGIN_TIME_DAY_STRING = getFormatHourMinuteString(BEGIN_HOUR_DAY, BEGIN_MINUTE_DAY)
val END_TIME_DAY_STRING = getFormatHourMinuteString(END_HOUR_DAY, END_MINUTE_DAY)

val BEGIN_TIME_DAY_INT = getFormatHourMinuteInt(BEGIN_HOUR_DAY, BEGIN_MINUTE_DAY)
val END_TIME_DAY_INT = getFormatHourMinuteInt(END_HOUR_DAY, END_MINUTE_DAY)