package droid.ankit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinpoint")
data class PinPoint (
    @PrimaryKey val id: Int,
    val description: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
)