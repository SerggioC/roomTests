package com.sergiocruz.roomtests.model.queries

import androidx.room.*
import com.sergiocruz.roomtests.model.Address
import com.sergiocruz.roomtests.model.User
import java.util.*

data class UserWithAddress(

    @Embedded
    val user: User,

    @Relation(
        parentColumn = User.userIDKey,
        entityColumn = Address.addressIdKey
    )
    val address: List<Address>
)

@Entity(
    tableName = "garden_plantings",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["wordID"], childColumns = ["plant_id"])
    ],
    indices = [Index("plant_id")]
)
data class GardenPlanting(
    @ColumnInfo(name = "plant_id") val plantId: String,

    /**
     * Indicates when the [Plant] was planted. Used for showing notification when it's time
     * to harvest the plant.
     */
    @ColumnInfo(name = "plant_date") val plantDate: Calendar = Calendar.getInstance(),

    /**
     * Indicates when the [Plant] was last watered. Used for showing notification when it's
     * time to water the plant.
     */
    @ColumnInfo(name = "last_watering_date")
    val lastWateringDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wordID")
    var gardenPlantingId: Long = 0
}