package dk.e5pro5.foregroundservice

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime
import java.util.*
import android.util.Log

class StepsDBHelper(context: Context) : SQLiteOpenHelper(context,"StepsDatabase.db", null, 1)
{
   val TABLE_STEPS_SUMMARY= "StepsSummary"
    val STEPS_COUNT=        "stepscount"
    val CREATION_DATE=      "creationdate"
    val ID =                "id"

    val CREATE_TABLE_STEPS_SUMMARY = "CREATE TABLE "+
            TABLE_STEPS_SUMMARY + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    CREATION_DATE + " TEXT,"+ STEPS_COUNT + "INTEGER"+")"

    override fun onCreate(db:SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_STEPS_SUMMARY)
    }

    fun createStepsEntry(): Boolean  {
        var isDateAlreadyPresent = false
        var createSuccessful = false
        var currentDateStepCounts = 0

        val todayDate = LocalDateTime.now().toString()

        /*
        val mCalendar = Calendar.getInstance()
        val todayDate= mCalendar.get(Calendar.MONTH)

        +(mCalendar.get(Calendar.DAY_OF_MONTH)+1)+"/"+mCalendar.get(Calendar.YEAR)

        val todayDate = String.valueOf(
            mCalendar
                .get(Calendar.MONTH)
        ) + "/" +
                String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH) + 1) + "/" +
                String.valueOf(mCalendar.get(Calendar.YEAR))
*/

        val selectQuery = ("SELECT " + STEPS_COUNT
                + " FROM " + TABLE_STEPS_SUMMARY + " WHERE "
                + CREATION_DATE + " = '" + todayDate + "'")

        try {
            val db = this.readableDatabase
            val c = db.rawQuery(selectQuery, null)
            if (c.moveToFirst()) {
                do {
                    isDateAlreadyPresent = true
                    currentDateStepCounts = c.getInt(c.getColumnIndex(STEPS_COUNT))
                } while (c.moveToNext())
            }
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("DB1:",e.printStackTrace().toString())
        }

        try {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(CREATION_DATE, todayDate)
            if (isDateAlreadyPresent) {
                values.put(
                    STEPS_COUNT,
                    ++currentDateStepCounts
                )
                val row = db.update(
                    TABLE_STEPS_SUMMARY,
                    values, "$CREATION_DATE = '$todayDate'",
                    null
                )
                if (row == 1) {
                    createSuccessful = true
                }
                db.close()
            } else {
                values.put(STEPS_COUNT, 1)
                val row = db.insert(TABLE_STEPS_SUMMARY, null, values)
                if (row.toString() != "-1") {
                    createSuccessful = true
                }
                db.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("DB2:",e.printStackTrace().toString())
        }
        return createSuccessful
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
    }


    fun readStepsEntries() : ArrayList<DateStepsModel>
    {
        val mStepCountList=ArrayList<DateStepsModel>()

        val selectQuery = "SELECT * FROM " + TABLE_STEPS_SUMMARY

        try {
            val db = this.readableDatabase

            val c = db.rawQuery(selectQuery, null)
            if (c.moveToFirst()) {
                do {
                    val mDateStepsModel = DateStepsModel("",0)
                    mDateStepsModel.mDate = c.getColumnIndex(CREATION_DATE).toString()
                    mDateStepsModel.mStepCount = c.getColumnIndex(STEPS_COUNT)
                    mStepCountList.add(DateStepsModel(mDateStepsModel.mDate,mDateStepsModel.mStepCount))
                } while (c.moveToNext())
            }
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mStepCountList
    }
}
