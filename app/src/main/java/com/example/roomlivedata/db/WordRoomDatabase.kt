package com.example.roomlivedata.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomlivedata.db.dao.WordDao
import com.example.roomlivedata.db.entity.Book
import com.example.roomlivedata.db.entity.CoolerData
import com.example.roomlivedata.db.entity.Word
import kotlinx.coroutines.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = arrayOf(Word::class, CoolerData::class, Book::class), version = 3, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word(word = "Hello")
            wordDao.insert(word)
            word = Word(word = "World!")
            wordDao.insert(word)

            // TODO: Add your own words!
            word = Word(word = "TODO!")
            wordDao.insert(word)
        }
    }

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        const val TABLE_BOOK = "book_table"
        const val TABLE_COOLERS_CACHE =
            "coolers" //rename as device or controller.  In future we may track more than coolers
        const val COOLER_COLUMN_ID = "_id"
        const val COOLER_COLUMN_COOLER_SERIAL = "serial"
        const val COOLER_COLUMN_COOLER_OEM_ID = "oemId" //rename to oem_id
        const val COOLER_COLUMN_COOLER_BOTTLER_ID = "bottlerId" //rename to bottler_id
        const val COOLER_COLUMN_COOLER_BRAND_ID = "brandId" //rename to brand_id
        const val COOLER_COLUMN_COOLER_ID = "name" //rename
        const val COOLER_COLUMN_COOLER_FRIENDLY_NAME = "friendlyName" //rename to user_defined_name
        const val COOLER_COLUMN_LAST_TEMPERATURE = "lastTemperature"
        const val COOLER_COLUMN_LAST_TEMPERATURE_TIME_UTC = "lastTemperatureTimeUtc"
        const val COOLER_COLUMN_LAST_TEMPERATURE_UNITS = "lastTemperatureUnits"
        const val COOLER_COLUMN_BOTTLER_ASSET_NUMBER = "bottlerAssetNumber"
        const val COOLER_COLUMN_OEM_SERIAL_NUMBER = "oemSerialNumber" //rename to oem_serial_number
        const val COOLER_COLUMN_PROGRESS_PERCENTAGE = "progressPercentage" //move
        const val COOLER_COLUMN_LAST_COMPLETED_STATE = "lastCompletedState" //move
        const val COOLER_COLUMN_LAST_RSSI = "lastRssi" //move
        const val COOLER_COLUMN_VISIT_STARTED = "visitStarted"
        const val COOLER_COLUMN_LAST_PROGRESS_MESSAGE = "lastProgressMessage" //move
        const val COOLER_COLUMN_LAST_PROGRESS_WAS_ERROR = "lastProgressWasError" //move
        const val COOLER_COLUMN_LAST_PROGRESS_TIME_UTC = "lastProgressTimeUtc" //move
        const val COOLER_COLUMN_LAST_COOLER_EVENT_RAW_TIMESTAMP = "lastCoolerEventRawTimestamp"
        const val COOLER_COLUMN_LAST_COOLER_STATISTIC_RAW_TIMESTAMP =
            "lastCoolerStatisticRawTimestamp"
        const val COOLER_COLUMN_LAST_SCAN_TIME_UTC = "lastScanTimeUtc"
        const val COOLER_COLUMN_LAST_SCAN_TIME_TZOFFSET = "lastScanTimeTzOffset"
        const val COOLER_COLUMN_IN_RANGE_START_TIME_UTC = "inRangeStartTimeUtc"
        const val COOLER_COLUMN_IN_RANGE_START_TIME_TZOFFSET = "inRangeStartTimeTzOffset"
        const val COOLER_COLUMN_LAST_COMPLETED_TIME_UTC = "lastCompletedTimeUtc"
        const val COOLER_COLUMN_LAST_COMPLETED_TIME_TZOFFSET = "lastCompletedTimeTzOffset"
        const val COOLER_COLUMN_LAST_STATUS_UPDATE_TIME_UTC = "lastStatusUpdateUtc"
        const val COOLER_COLUMN_LAST_STATUS_UPDATE_TIME_TZOFFSET = "lastStatusUpdateTzOffset"
        const val COOLER_COLUMN_LAST_LOCATION_UPDATE_UTC = "lastLocationUpdateUtc"
        const val COOLER_COLUMN_LAST_LOCATION_UPDATE_TZOFFSET = "lastLocationUpdateTzOffset"
        const val COOLER_COLUMN_LAST_CONNECTION_START_TIME_UTC = "lastConnectionStartTimeUtc"
        const val COOLER_COLUMN_LAST_CONNECTION_START_TIME_TZOFFSET =
            "lastConnectionStartTimeTzOffset"
        const val COOLER_COLUMN_MODEL_ID = "coolerModelId" //rename to model_id
        const val COOLER_COLUMN_LAST_STORE_HOURS_SYNC_UTC = "lastStoreHoursSyncTimeUtc"
        const val COOLER_COLUMN_LAST_LOCAL_STORE_HOURS_UPDATE_UTC =
            "lastLocalStoreHoursUpdateTimeUtc"
        const val COOLER_COLUMN_LAST_STATUS = "status"
        const val COOLER_COLUMN_PRODUCT_ID = "productId"
        const val COOLER_COLUMN_PRODUCT_REVISION = "productRevision"
        const val COOLER_COLUMN_PROTOCOL_VERSION = "protocolVersion"
        private const val COOLER_COLUMN_DELETED = "deleted" //removed
        const val COOLER_COLUMN_CONTROLLER_CODE_REVISION = "controllerCodeRevision"
        const val COOLER_COLUMN_LIGHTING_PRESET = "lightingPreset"

        const val DB_NAME = "word_database"
        const val encryptPassword = "123456789"

        fun getDatabase(
            context: Context
        ): WordRoomDatabase {

            val state = SQLCipherUtils.getDatabaseState(context, DB_NAME)
            if (state == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encrypt(context, DB_NAME, encryptPassword)
            }
            return INSTANCE ?: synchronized(this) {
                val passphrase = SQLiteDatabase.getBytes(encryptPassword.toCharArray())
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    DB_NAME
                ).openHelperFactory(factory)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                println("Migrate........")
                database.execSQL(
                    "CREATE TABLE  " + TABLE_COOLERS_CACHE
                            + " (" + COOLER_COLUMN_ID + " INTEGER primary key autoincrement NOT NULL, "
                            + COOLER_COLUMN_COOLER_SERIAL + " TEXT NOT NULL, "
                            + COOLER_COLUMN_COOLER_OEM_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_COOLER_BOTTLER_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_LIGHTING_PRESET + " INTEGER NOT NULL DEFAULT 0 )"
                )
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                println("Migrate2_3........")
                database.execSQL("PRAGMA foreign_keys=off;")
                database.execSQL(
                    "CREATE TABLE  " + TABLE_BOOK
                            + " (" + COOLER_COLUMN_ID + " INTEGER primary key autoincrement NOT NULL, "
                            + COOLER_COLUMN_COOLER_ID + " TEXT NOT NULL, "
                            + COOLER_COLUMN_COOLER_SERIAL + " TEXT NOT NULL, "
                            + COOLER_COLUMN_COOLER_OEM_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_COOLER_BOTTLER_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_LIGHTING_PRESET + " INTEGER NOT NULL DEFAULT 0, "
                            + " FOREIGN KEY (" + COOLER_COLUMN_COOLER_ID + ") REFERENCES " + TABLE_COOLERS_CACHE + "(" + COOLER_COLUMN_ID + ") ON DELETE CASCADE )"
                )
                database.execSQL("PRAGMA foreign_keys=on;")
            }
        }


    }


}

