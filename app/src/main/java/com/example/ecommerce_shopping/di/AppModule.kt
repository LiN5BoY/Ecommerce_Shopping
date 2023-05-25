package com.example.ecommerce_shopping.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

}