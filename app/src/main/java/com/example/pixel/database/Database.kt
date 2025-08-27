package com.example.pixel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pixel.database.dao.FollowedUserDao
import com.example.pixel.database.model.FollowedUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Database(entities = [FollowedUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun followedUserDao(): FollowedUserDao
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): FollowedUserDao {
        return db.followedUserDao()
    }
}
