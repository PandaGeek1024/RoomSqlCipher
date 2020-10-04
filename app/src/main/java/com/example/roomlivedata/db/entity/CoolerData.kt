package com.example.roomlivedata.db.entity

import android.text.format.DateUtils
import androidx.room.*
import com.example.roomlivedata.db.WordRoomDatabase
import java.util.*

@Entity(
        tableName = WordRoomDatabase.TABLE_COOLERS_CACHE
)
internal class CoolerData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_ID)
    var coolerId: Long = 0

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_SERIAL)
    var coolerSerialNumber: String = ""

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_OEM_ID)
    var oemId = 0

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_BOTTLER_ID)
    var bottlerId = 0


    override fun toString(): String {
        return "CoolerData{" +
                "coolerId=" + coolerId +
                ", coolerSerialNumber='" + coolerSerialNumber + '\'' +
                ", oemId=" + oemId +
                ", bottlerId=" + bottlerId +
                '}'
    }
}