package com.farware.recipesaver.di

import android.app.Application
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farware.recipesaver.RecipeApp
import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDatabase
import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDatabase.Companion.getInstance
import com.farware.recipesaver.feature_recipe.data.repository.DataStoreRepositoryImpl
import com.farware.recipesaver.feature_recipe.data.repository.FirebaseRepositoryImpl
import com.farware.recipesaver.feature_recipe.data.repository.RecipeRepositoryImpl
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.repository.DataStoreRepository
import com.farware.recipesaver.feature_recipe.domain.repository.FirebaseRepository
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import com.farware.recipesaver.feature_recipe.domain.use_cases.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.data_store.*
import com.farware.recipesaver.feature_recipe.domain.use_cases.firebase.*
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        ).fallbackToDestructiveMigration()
            //.createFromAsset("recipes_db.db ")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        GlobalScope.launch {
                            val dao = getInstance(app.applicationContext).recipeDao

                            // insert initial category colors
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Purple",
                                    lightThemeColor = Color(0xFFD500F9).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF8E24AA).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            /*dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Purple",
                                    lightThemeColor = Color(0xFFBA68C8).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF7E57C2).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Deep Purple",
                                    lightThemeColor = Color(0xFFB388FF).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF512DA8).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Indigo",
                                    lightThemeColor = Color(0xFF536DFE).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF303F9F).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )*/
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Light Indigo",
                                    lightThemeColor = Color(0xFF8C9EFF).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF3F51B5).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            /*dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Blue",
                                    lightThemeColor = Color(0xFF2979FF).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF1565C0).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Blue",
                                    lightThemeColor = Color(0xFF42A5F5).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF0277BD).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            ) */
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Light Blue",
                                    lightThemeColor = Color(0xFF00B0FF).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF1976D2).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                       /*     dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Cyan",
                                    lightThemeColor = Color(0xFF0097A7).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF006064).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )*/
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Light Cyan",
                                    lightThemeColor = Color(0xFF00BCD4).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF00838F).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
/*                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Teal",
                                    lightThemeColor = Color(0xFF00897B).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF00695C).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Light Teal",
                                    lightThemeColor = Color(0xFF26A69A).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF00796B).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Green",
                                    lightThemeColor = Color(0xFF388E3C).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF1B5E20).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )*/
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Green",
                                    lightThemeColor = Color(0xFF4CAF50).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF33691E).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            /*dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Light Green",
                                    lightThemeColor = Color(0xFF8BC34A).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF2E7D32).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Lime",
                                    lightThemeColor = Color(0xFFAEEA00).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFF6A7800).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Dark Purple2",
                                    lightThemeColor = Color(0xFFA100BD).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFFF8ACFF).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Purple2",
                                    lightThemeColor = Color(0xFF8B3D9B).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFFF6ADFF).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )*/
                            dao.insertCategoryColor(
                                CategoryColor(
                                    name = "Deep Purple",
                                    lightThemeColor = Color(0xFF7046B8).toArgb(),
                                    onLightThemeColor = Color(0xFF000000).toArgb(),
                                    darkThemeColor = Color(0xFFD4BBFF).toArgb(),
                                    onDarkThemeColor = Color(0xFFFFFFFF).toArgb(),
                                    timeStamp = System.currentTimeMillis()
                                )
                            )

                            // insert initial category
                            dao.insertCategory(
                                Category(
                                    name = "Breakfast",
                                    colorId = 1,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategory(
                                Category(
                                    name = "Lunch",
                                    colorId = 2,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategory(
                                Category(
                                    name = "Dinner",
                                    colorId = 3,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategory(
                                Category(
                                    name = "Dessert",
                                    colorId = 4,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategory(
                                Category(
                                    name = "Snacks",
                                    colorId = 5,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertCategory(
                                Category(
                                    name = "Cakes and Pies",
                                    colorId = 6,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )

                            // insert initial recipe
                            dao.insertRecipe(
                                Recipe(
                                    categoryId = 1,
                                    name = "Pizza",
                                    description = "A great pizza the whole family will love",
                                    prepTime = 20,
                                    cookTime = 30,
                                    favorite = true,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertRecipe(
                                Recipe(
                                    categoryId = 2,
                                    name = "Toaster PB and J",
                                    description = "The kids are sure to love this easy to make lunch recipe.",
                                    prepTime = 10,
                                    cookTime = 5,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertRecipe(
                                Recipe(
                                    categoryId = 3,
                                    name = "Brown Sugar Salmon",
                                    description = "Take this dish to your next BBQ, serve with coleslaw.",
                                    prepTime = 30,
                                    cookTime = 35,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertRecipe(
                                Recipe(
                                    categoryId = 4,
                                    name = "White Chocolate Mousse Pie",
                                    description = "You will be the hit of the party.",
                                    favorite = true,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                            dao.insertRecipe(
                                Recipe(
                                    categoryId = 5,
                                    name = "Rice Krispies Treats",
                                    description = "This is a great after school snack to keep around the house.",
                                    timeStamp = System.currentTimeMillis()
                                )
                            )

                            // insert initial recipe steps
                            dao.insertStep(
                                Step(
                                    recipeId = 1,
                                    stepNumber = 1,
                                    text = "In a mixing bowl combine flour, sugar, salt. " +
                                            "In a measuring cup add the water(105 degrees) and yeast then add to the dry ingredients. " +
                                            "Start mixing and then add the oil. Add small amounts of flour until dough is not sticky. " +
                                            "Place in an oiled bowl, cover and let rise for 1-2 hours."
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 1,
                                    stepNumber = 2,
                                    text = "Punch down the dough and let rest for 5 min.  Press the dough into your desired shape."
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 1,
                                    stepNumber = 3,
                                    text = "Preheat the oven to 525 degrees or the highest setting on your oven." +
                                            "Spread the pizza sauce evenly across the dough leaving about a 1/4 inch edge. Cover with shredded mozzarella to your liking." +
                                            "Add any toppings you like and cover with shredded monetary jack and cheddar."
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 1,
                                    stepNumber = 4,
                                    text = "Bake for 5 minuets on the lower rack and 5 more minuets on the upper rack, this will help cook the top of the pizza better."
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 2,
                                    stepNumber = 1,
                                    text = "Preheat oven to 350 degrees. In a small sauce pan add the butter, mustard powder, garlic, olive oil and worcestershire sauce " +
                                            "and bring to a simmer."
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 2,
                                    stepNumber = 2,
                                    text = "Chicken breasts can be cut into 2 inch pieces or left whole. Place in a bowl and thoroughly coat " +
                                            "with the mixture. Dredge the coated chicken in the crushed fried onions and place in a baking dish." +
                                            "Bake 30-35 minuets until fried onions are a crispy brown color"
                                )
                            )
                            dao.insertStep(
                                Step(
                                    recipeId = 3,
                                    stepNumber = 1,
                                    text = "Have not finished this recipe."
                                )
                            )

                            // insert a tip
                            dao.insertTip(
                                Tip(
                                    recipeId = 1,
                                    tipNumber = 1,
                                    text = "Add the toppings you like, keep the meats closer to the bottom and veggies on top."
                                )
                            )
                            dao.insertTip(
                                Tip(
                                    recipeId = 3,
                                    tipNumber = 1,
                                    text = "finnish this recipe and add a tip!!!"
                                )
                            )

                            // insert initial ingredients(master list on firebase)
                            dao.insertIngredient(
                                Ingredient(
                                    ingredientId = 312,
                                    type = "Spices and Herbs",
                                    name = "Salt"
                                )
                            )
                            dao.insertIngredient(
                                Ingredient(
                                    ingredientId = 336,
                                    type = "Sugar and Sugar Products",
                                    name = "Sugar"
                                )
                            )
                            dao.insertIngredient(
                                Ingredient(
                                    ingredientId = 3,
                                    type = "Cereals and Pulses",
                                    name = "All Purpose Flour"
                                )
                            )
                            dao.insertIngredient(
                                Ingredient(
                                    ingredientId = 373,
                                    type = "Other Ingredients",
                                    name = "Yeast"
                                )
                            )
                            dao.insertIngredient(
                                Ingredient(
                                    ingredientId = 357,
                                    type = "Other Ingredients",
                                    name = "Vegetable Oil"
                                )
                            )


                            // insert initial recipe_ingredients
                            dao.insertRecipeIngredient(
                                RecipeIngredient(
                                    recipeId = 1,
                                    ingredientId = 312,
                                    measureId = 0,
                                    amount = "1",
                                    text = "1 tsp Salt"
                                )
                            )
                            dao.insertRecipeIngredient(
                                RecipeIngredient(
                                    recipeId = 1,
                                    ingredientId = 336,
                                    measureId = 0,
                                    amount = "1",
                                    text = "1 tsp Sugar"
                                )
                            )
                            dao.insertRecipeIngredient(
                                RecipeIngredient(
                                    recipeId = 1,
                                    ingredientId = 3,
                                    measureId = 2,
                                    amount = "3",
                                    text = "3 cups All Purpose Flour"
                                )
                            )
                            dao.insertRecipeIngredient(
                                RecipeIngredient(
                                    recipeId = 1,
                                    ingredientId = 373,
                                    measureId = 0,
                                    amount = "2 1/4",
                                    text = "2 1/4 tsp Yeast"
                                )
                            )
                            dao.insertRecipeIngredient(
                                RecipeIngredient(
                                    recipeId = 1,
                                    ingredientId = 357,
                                    measureId = 1,
                                    amount = "2",
                                    text = "2 tbls Vegetable Oil"
                                )
                            )

                            dao.getRecipes(onlyFavorites = false )
                            dao.getCategories()
                        }
                    }
                }
            })
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
            insertAllMeasures = InsertAllMeasuresUseCase(repository)
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