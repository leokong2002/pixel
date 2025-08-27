package com.example.pixel.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext appContext: Context
    ): NetworkMonitor{
        return LiveNetworkMonitor(appContext)
    }

    @Provides
    fun provideOkhttpClient(
        liveNetworkMonitor: NetworkMonitor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(NetworkMonitorInterceptor(liveNetworkMonitor))
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}
