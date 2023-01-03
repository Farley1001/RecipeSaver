package com.farware.recipesaver.feature_recipe.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farware.recipesaver.feature_recipe.data.entities.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*

@Database(
    entities = [
        RecipeEntity::class,
        CategoryEntity::class,
        CategoryColorEntity::class,
        ConversionEntity::class,
        IngredientEntity::class,
        MeasureEntity::class,
        RecipeIngredientEntity::class,
        StepEntity::class,
        TipEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {
        const val DATABASE_NAME: String = "recipes_db"

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        /* ****************************************************************
                 ALL DATABASE SEEDING IS DONE IN AppModule.kt
                     MIGRATIONS SHOULD GO THERE AS WELL
        ***************************************************************** */

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        //.createFromAsset("recipes_db.db")
        //.addMigrations(MIGRATION_1_2)


    }
}