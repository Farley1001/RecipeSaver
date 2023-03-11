package com.farware.recipesaver.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.farware.recipesaver.RecipeApp
import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDatabase
import com.farware.recipesaver.feature_recipe.data.repository.DataStoreRepositoryImpl
import com.farware.recipesaver.feature_recipe.data.repository.FirebaseRepositoryImpl
import com.farware.recipesaver.feature_recipe.data.repository.RecipeRepositoryImpl
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import com.farware.recipesaver.feature_recipe.domain.use_cases.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.data_store.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.firebase.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientByIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientByNameUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientsUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.InsertIngredientReturnIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.GetMeasureByIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.GetMeasuresUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.measure.InsertMeasureReturnIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.AddRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.DeleteRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.GetRecipeIngredientsByRecipeIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.AddStepUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.DeleteStepUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.step.GetStepsByRecipeIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.AddTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.DeleteTipUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.tip.GetTipsByRecipeIdUseCase
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): RecipeApp {
        return app as RecipeApp
    }

    @Singleton
    @Provides
    fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator {
        return appNavigatorImpl as AppNavigator
    }

    @Singleton
    @Provides
    fun provideRandomString(): String{
        return "Hey look a random string!!!!!GINKGO"
    }

    /*lateinit var recipeDatabase: RecipeDatabase*/


    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application): RecipeDatabase {
        return Room.databaseBuilder(
            app,
            RecipeDatabase::class.java,
            RecipeDatabase.DATABASE_NAME
        ).createFromAsset("database/recipes_db.db")         //.fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideRecipeRepository(db: RecipeDatabase): RecipeRepository {
        return RecipeRepositoryImpl(db.recipeDao)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(): DataStoreRepository {
        return DataStoreRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideRecipeUseCases(
        repository: RecipeRepository
    ): RecipeUseCases {
        return RecipeUseCases(
            getRecipes = GetRecipesUseCase(repository),
            deleteRecipe = DeleteRecipeUseCase(repository),
            addRecipe = AddRecipeUseCase(repository),
            getRecipe = GetRecipeUseCase(repository),
            saveRecipe = InsertRecipeUseCase(repository),
            getCategories = GetCategoriesUseCase(repository),
            deleteCategory = DeleteCategoryUseCase(repository),
            addCategory = AddCategoryUseCase(repository),
            getCategory = GetCategoryUseCase(repository),
            searchRecipesOnCategory = SearchRecipesOnCategoryUseCase(repository),
            searchRecipesOnName = SearchRecipesOnNameUseCase(repository),
            searchRecipesOnIngredients = SearchRecipesOnIngredientsUseCase(repository),
            searchRecipesOnDirections = SearchRecipesOnDirectionsUseCase(repository),
            insertConversion = InsertConversionUseCase(repository),
            insertAllConversions = InsertAllConversionsUseCase(repository),
            insertMeasure = InsertMeasureUseCase(repository),
            insertAllMeasures = InsertAllMeasuresUseCase(repository),
            insertRecipeReturnId =  InsertRecipeReturnIdUseCase(repository),
        )
    }

    @Provides
    fun provideStepUseCases(
        repository: RecipeRepository
    ): StepUseCases {
        return StepUseCases(
            getStepsByRecipeId = GetStepsByRecipeIdUseCase(repository),
            addStep = AddStepUseCase(repository),
            deleteStep = DeleteStepUseCase(repository)
        )
    }

    @Provides
    fun provideTipUseCases(
        repository: RecipeRepository
    ): TipUseCases {
        return TipUseCases(
            getTipsByRecipeId = GetTipsByRecipeIdUseCase(repository),
            addTip = AddTipUseCase(repository),
            deleteTip = DeleteTipUseCase(repository)
        )
    }

    @Provides
    fun provideRecipeIngredientUseCases(
        repository: RecipeRepository
    ): RecipeIngredientUseCases {
        return RecipeIngredientUseCases(
            getRecipeIngredientsByRecipeId = GetRecipeIngredientsByRecipeIdUseCase(repository),
            addRecipeIngredient = AddRecipeIngredientUseCase(repository),
            deleteRecipeIngredient = DeleteRecipeIngredientUseCase(repository)
        )
    }

    @Provides
    fun provideIngredientUseCases(
        repository: RecipeRepository
    ): IngredientUseCases {
        return IngredientUseCases(
            getIngredients = GetIngredientsUseCase(repository),
            getIngredientById = GetIngredientByIdUseCase(repository),
            getIngredientByName = GetIngredientByNameUseCase(repository),
            insertIngredientReturnId = InsertIngredientReturnIdUseCase(repository)
        )
    }

    @Provides
    fun provideMeasureUseCases(
        repository: RecipeRepository
    ): MeasureUseCases {
        return MeasureUseCases(
            getMeasures = GetMeasuresUseCase(repository),
            getMeasureById = GetMeasureByIdUseCase(repository),
            insertMeasureReturnId = InsertMeasureReturnIdUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseUseCases(
        repository: FirebaseRepository
    ): FirebaseUseCases {
        return FirebaseUseCases(
            getFirebaseMeasures = GetFirebaseMeasuresUseCase(repository),
            getFirebaseConversions = GetFirebaseConversionsUseCase(repository),
            getFirebaseIngredients = GetFirebaseIngredientsUseCase(repository),
            createUserWithEmailAndPassword = CreateUserWithEmailAndPasswordUseCase(repository),
            signInWithEmailAndPassword = SignInWithEmailAndPasswordUseCase(repository),
            signInWithGoogle = SignInWithGoogleUseCase(repository),
            signOut = SignOutUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreUseCases(
        repository: DataStoreRepository
    ): DataStoreUseCases {
        return DataStoreUseCases(
            getAllPreferences = GetAllPreferencesUseCase(repository),
            updateAllPreferences = UpdateAllPreferencesUseCase(repository),
            updateDisplayEmail = UpdateDisplayEmailUseCase(repository),
            updateDisplayName = UpdateDisplayNameUseCase(repository),
            updateDisplayEmailAndName = UpdateDisplayEmailAndNameUseCase(repository),
            getUseDynamicColor = GetUseDynamicColorUseCase(repository),
            updateUseDynamicColor = UpdateUseDynamicColor(repository),
            updateMeasureType = UpdateMeasureTypeUseCase(repository),
            updateMeasureUnit = UpdateMeasureUnitUseCase(repository),
        )
    }

}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

private fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}