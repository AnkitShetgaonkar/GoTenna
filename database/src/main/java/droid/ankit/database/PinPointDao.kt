package droid.ankit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface PinPointDao {
    @Insert(onConflict = REPLACE)
    fun saveAll(pinPoints: List<PinPoint>)

    @Query("SELECT * FROM pinpoint")
    fun loadAll(): LiveData<List<PinPoint>>
}