package com.example.roomlivedata.db.entity

import android.text.format.DateUtils
import androidx.room.*
import com.example.roomlivedata.db.WordRoomDatabase
import java.util.*

@Entity(
        tableName = WordRoomDatabase.TABLE_COOLERS_CACHE
//    ,
//        foreignKeys = [ForeignKey(
//                entity = OutletData::class,
//                parentColumns = [WordRoomDatabase.OUTLET_COLUMN_ID],
//                childColumns = [WordRoomDatabase.COOLER_COLUMN_OUTLET_ID],
//                onDelete = ForeignKey.SET_NULL
//        )],
//        indices = [Index(
//                name = "coolerNameIdx6",
//                value = [WordRoomDatabase.COOLER_COLUMN_COOLER_ID],
//                unique = true
//        ), Index(
//                name = "coolerSerialIdx6",
//                value = [WordRoomDatabase.COOLER_COLUMN_COOLER_SERIAL],
//                unique = true
//        )
//            ,
//            Index(
//                name = "coolerOutletIdx6",
//                value = [WordRoomDatabase.COOLER_COLUMN_OUTLET_ID]
//        )
//        ]
)
internal class CoolerData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_ID)
    var coolerId: Long = 0

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_ID)
    var scsCoolerName: String = ""

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_SERIAL)
    var coolerSerialNumber: String = ""

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_OEM_ID)
    var oemId = 0

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_COOLER_BOTTLER_ID)
    var bottlerId = 0

    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_LIGHTING_PRESET)
    var lightingPreset = 0



//    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_PRODUCT_TYPE_ID)
//    private var productTypeId: String? = null
//
//    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_TEMPERATURE_ALARM_SET)
//    var isTemperatureAlarmSet = false


//    @ColumnInfo(name = WordRoomDatabase.COOLER_COLUMN_HAS_BEEN_TRACKED)
//    var coolerHasBeenTracked = false



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CoolerData
        return scsCoolerName == that.scsCoolerName
    }

    override fun hashCode(): Int {
        return scsCoolerName.hashCode()
    }

    override fun toString(): String {
        return "CoolerData{" +
                "coolerId=" + coolerId +
                ", coolerSerialNumber='" + coolerSerialNumber + '\'' +
                ", oemId=" + oemId +
                ", bottlerId=" + bottlerId +
//                ", outletId='" + outletId + '\'' +
//                ", lightingPreset=" + lightingPreset +
//                ", log=" + log +
//                ", openServiceRequestCount=" + openServiceRequestCount +
//                ", unreadNotificationsCount=" + unreadNotificationsCount +
//                ", coolerDataItemMap=" + coolerDataItemMap +
//                ", productTypeId=" + productTypeId +
//                ", temperatureAlarmSet=" + isTemperatureAlarmSet +
                '}'
    }
}