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

@Database(
    entities = arrayOf(Word::class, CoolerData::class, Book::class),
    version = 4,
    exportSchema = false
)
abstract class WordRoomDatabase : RoomDatabase() {

    private class WordDatabaseCallback(
        private val scope: CoroutineScope? =null
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            db.setForeignKeyConstraintsEnabled(true)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
            println()
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(database.wordDao())
//                }
//            }
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
        const val COOLER_COLUMN_COOLER_ID = "name" //rename
        const val COOLER_COLUMN_LIGHTING_PRESET = "lightingPreset"

        const val DB_NAME = "word_database"
        const val encryptPassword = "123456789"


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
                database.execSQL(
                    "INSERT INTO " + TABLE_COOLERS_CACHE + " VALUES ( null, '123', 987, 456, 123)"
                )

            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                println("Migrate2_3........")
                database.execSQL(
                    "CREATE TABLE  " + TABLE_BOOK
                            + " (" + COOLER_COLUMN_ID + " INTEGER primary key autoincrement NOT NULL, "
                            + COOLER_COLUMN_COOLER_ID + " INTEGER NOT NULL, "
                            + COOLER_COLUMN_COOLER_SERIAL + " TEXT NOT NULL, "
                            + " FOREIGN KEY (" + COOLER_COLUMN_COOLER_ID + ") REFERENCES " + TABLE_COOLERS_CACHE + "(" + COOLER_COLUMN_ID + ") ON DELETE CASCADE )"
                )
                database.execSQL(
                    "INSERT INTO " + TABLE_BOOK + " VALUES ( null, 1, 987)"
                )
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                println("Migrate3_4........")
                database.execSQL("PRAGMA foreign_keys=OFF;")

                database.execSQL(
                    "CREATE TABLE  " + TABLE_COOLERS_CACHE + "_NEW"
                            + " (" + COOLER_COLUMN_ID + " INTEGER primary key autoincrement NOT NULL, "
                            + COOLER_COLUMN_COOLER_SERIAL + " TEXT NOT NULL, "
                            + COOLER_COLUMN_COOLER_OEM_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_COOLER_BOTTLER_ID + " INTEGER NOT NULL DEFAULT 0, "
                            + COOLER_COLUMN_LIGHTING_PRESET + " INTEGER NOT NULL DEFAULT 0 )"
                )
                database.execSQL("INSERT INTO " + TABLE_COOLERS_CACHE + "_NEW"
                        + " SELECT "
                        + COOLER_COLUMN_ID + ", "
                        + COOLER_COLUMN_COOLER_SERIAL + ", "
                        + COOLER_COLUMN_COOLER_OEM_ID + ", "
                        + COOLER_COLUMN_COOLER_BOTTLER_ID + ", "
                        + COOLER_COLUMN_LIGHTING_PRESET
                        + " FROM " + TABLE_COOLERS_CACHE + ";"
                )
                database.execSQL("ALTER TABLE  " + TABLE_COOLERS_CACHE
                        + " RENAME TO _OLD_COOLERS")
                database.execSQL("ALTER TABLE  " + TABLE_COOLERS_CACHE + "_NEW"
                        + " RENAME TO " + TABLE_COOLERS_CACHE)
                database.execSQL("DROP TABLE _OLD_COOLERS")


//                //try create a new book table to point to coolers table
//                database.execSQL(
//                    "CREATE TABLE  " + TABLE_BOOK + "_NEW"
//                            + " (" + COOLER_COLUMN_ID + " INTEGER primary key autoincrement NOT NULL, "
//                            + COOLER_COLUMN_COOLER_ID + " INTEGER NOT NULL, "
//                            + COOLER_COLUMN_COOLER_SERIAL + " TEXT NOT NULL, "
//                            + " FOREIGN KEY (" + COOLER_COLUMN_COOLER_ID + ") REFERENCES " + TABLE_COOLERS_CACHE + "(" + COOLER_COLUMN_ID + ") ON DELETE CASCADE )"
//                )
//                database.execSQL("INSERT INTO " + TABLE_BOOK + "_NEW"
//                        + " SELECT "
//                        + COOLER_COLUMN_ID + ", "
//                        + COOLER_COLUMN_COOLER_ID + ", "
//                        + COOLER_COLUMN_COOLER_SERIAL
//                        + " FROM " + TABLE_BOOK + ";"
//                )
//                database.execSQL("ALTER TABLE  " + TABLE_BOOK
//                        + " RENAME TO _OLD_BOOK")
//                database.execSQL("ALTER TABLE  " + TABLE_BOOK + "_NEW"
//                        + " RENAME TO " + TABLE_BOOK)
//                database.execSQL("DROP TABLE _OLD_BOOK")

                database.execSQL("PRAGMA foreign_keys=ON;")
            }
        }
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
                    "encrypted_word_database"
                )
                    .openHelperFactory(factory)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .addCallback(WordDatabaseCallback())
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }


}

