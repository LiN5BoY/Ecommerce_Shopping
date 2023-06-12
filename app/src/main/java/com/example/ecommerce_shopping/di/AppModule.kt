package com.example.ecommerce_shopping.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.ecommerce_shopping.firebase.FirebaseCommon
import com.example.ecommerce_shopping.util.Constants.INTRODUCTION_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /*
    Provide 依赖注入
    Singleton模式是一种软件设计模式 ，可确保一个类只有一个实例，并且该类提供了对其的全局访问点。
    每当多个类或客户端请求该类时，它们都会获得该类的相同实例。
    这个Singleton类可能负责实例化其自身，或者您可以将对象创建委托给factory类。
     */
    @Provides
    @Singleton
    //FirebaseAuth.getInstance() 是用于获取 Firebase 身份验证服务的实例的静态方法。
    fun provideFirebaseAuth() = FirebaseAuth.getInstance();


    @Provides
    @Singleton
    fun provideFirebaseFireStoreDatabase() = Firebase.firestore


    @Provides
    fun provideIntroductionSP(
        application : Application
        ) = application.getSharedPreferences(INTRODUCTION_SP,MODE_PRIVATE)
    // SharedPreferences存储是一种轻量级的数据存储方式
    // 它屏蔽了对底层文件的操作，通过为程序开发人员提供简单的编程接口，实现以简单的方式对数据进行永久保存。这种方式主要对少量
    // 的数据进行保存

    @Provides
    @Singleton
    fun provideFirebaseCommon(
        firebaseAuth : FirebaseAuth,
        firestore : FirebaseFirestore,
    ) = FirebaseCommon(firestore,firebaseAuth)

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference
}