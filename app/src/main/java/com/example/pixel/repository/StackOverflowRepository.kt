package com.example.pixel.repository

import android.text.Html
import com.example.pixel.repository.model.FormattedUserInfo
import com.example.pixel.repository.model.Items
import com.example.pixel.repository.model.UserInfoListData
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

interface StackOverflowRepository {
    val userInfoListDataFlow: MutableStateFlow<UserInfoListData>

    suspend fun getUsers()
}

class StackOverflowRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
) : StackOverflowRepository {

    override val userInfoListDataFlow = MutableStateFlow(UserInfoListData())

    override suspend fun getUsers() {
        val gistJsonAdapter = moshi.adapter(Items::class.java)

        val request = Request.Builder()
            .url("https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&%20sort=reputation&site=stackoverflow")
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                when {
                    !response.isSuccessful -> {
                        throw IOException("Get Users - Failed to get users ${response.code}")
                    }
                    else -> {
                        val userInfoList = gistJsonAdapter.fromJson(response.body!!.source())?.items.orEmpty()
                        if (userInfoList.isEmpty()) throw IOException("Get Users - Successful but received an empty list")
                        val formattedUserInfoList = userInfoList.map { userInfo ->
                            FormattedUserInfo(
                                accountId = userInfo.accountId,
                                totalReputation = userInfo.totalReputation,
                                imageUrl = userInfo.imageUrl,
                                formattedDisplayName = Html.fromHtml(userInfo.displayName, Html.FROM_HTML_MODE_LEGACY).toString(),
                            )
                        }
                        userInfoListDataFlow.emit(
                            UserInfoListData(
                                isLoading = false,
                                hasError = formattedUserInfoList.isEmpty(),
                                formattedUserInfoList = formattedUserInfoList
                            )
                        )
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            userInfoListDataFlow.emit(
                UserInfoListData(
                    isLoading = false,
                    hasError = true,
                    formattedUserInfoList = emptyList()
                )
            )
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