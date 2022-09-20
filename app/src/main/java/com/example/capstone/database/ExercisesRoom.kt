package com.example.capstone.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capstone.entities.ExercisesData
import retrofit2.http.QueryName

@Dao
interface ExercisesDao {

    /**
     * Gets a list of LiveData to keep the RecyclerView Updated
     */
    @Query("SELECT * FROM exercises")
    fun getExercises(): List<ExercisesData>

    /**
     * Gets a specific exercise from the exercises table
     */
    @Query("SELECT * FROM exercises WHERE name = :queryName ")
    fun findExerciseWithName(queryName: String): List<ExercisesData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercises(exercisesData: ExercisesData)
}

@Database(entities = [ExercisesData::class], version = 1)
abstract class ExercisesDatabase : RoomDatabase() {
    abstract val exercisesDao: ExercisesDao
}

private lateinit var INSTANCE: ExercisesDatabase
fun getExercisesDataBase(context: Context): ExercisesDatabase {
    synchronized(ExercisesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context,
                ExercisesDatabase::class.java,
                "exerises_database"
            ).allowMainThreadQueries().build()
        }
        return INSTANCE
    }
}