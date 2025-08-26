package com.example.pixel.repository

import com.example.pixel.repository.model.Items
import com.example.pixel.repository.model.UserInfo
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

interface StackOverflowRepository {
    suspend fun getUsers(): List<UserInfo>
}

class StackOverflowRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
) : StackOverflowRepository {

    override suspend fun getUsers(): List<UserInfo> {
        val gistJsonAdapter = moshi.adapter(Items::class.java)

        val request = Request.Builder()
            .url("https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&%20sort=reputation&site=stackoverflow")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return gistJsonAdapter.fromJson(response.body!!.source())?.items.orEmpty()
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object StackOverflowRepositoryModule {
    @Provides
    fun provideStackOverflowRepository(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): StackOverflowRepository {
        return StackOverflowRepositoryImpl(okHttpClient, moshi)
    }
}