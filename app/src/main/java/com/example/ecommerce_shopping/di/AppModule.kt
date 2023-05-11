package com.example.ecommerce_shopping.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    //FirebaseAuth.getInstance() 是用于获取 Firebase 身份验证服务的实例的静态方法。
    fun provideFirebaseAuth() = FirebaseAuth.getInstance();

}