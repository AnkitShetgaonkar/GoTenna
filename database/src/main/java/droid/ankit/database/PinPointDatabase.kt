package droid.ankit.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PinPoint::class], version = 1)
abstract class PinPointDatabase : RoomDatabase() {
    abstract fun pinPointDao(): PinPointDao
}