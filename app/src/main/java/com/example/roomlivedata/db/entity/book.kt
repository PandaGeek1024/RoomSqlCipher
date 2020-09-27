package com.example.roomlivedata.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.roomlivedata.db.WordRoomDatabase

@Entity(
    tableName = WordRoomDatabase.TABLE_BOOK,
    foreignKeys = [ForeignKey(
        entity = CoolerData::class,
        parentColumns = [WordRoomDatabase.COOLER_COLUMN_ID],
        childColumns = [WordRoomDatabase.COOLER_COLUMN_COOLER_ID],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.NO_ACTION
    )]
)
class Book {
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
}