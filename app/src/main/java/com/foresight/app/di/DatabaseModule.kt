package com.foresight.app.di

import android.content.Context
import com.foresight.app.data.local.ForesightDatabase
import com.foresight.app.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ForesightDatabase {
        return ForesightDatabase.create(context)
    }

    @Provides
    fun provideCategoryDao(db: ForesightDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideItemDao(db: ForesightDatabase): ItemDao = db.itemDao()

    @Provides
    fun provideAlertDao(db: ForesightDatabase): AlertDao = db.alertDao()

    @Provides
    fun provideCategoryFieldDao(db: ForesightDatabase): CategoryFieldDao = db.categoryFieldDao()

    @Provides
    fun provideItemCustomFieldDao(db: ForesightDatabase): ItemCustomFieldDao = db.itemCustomFieldDao()
}
