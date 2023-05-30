package com.example.ecommerce_shopping.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.util.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
//Hilt把Dagger 手动创建Component 改成了预定义的Component，且自动集成到Android应用程序的各个生命周期中。
//通过注解的方式@InstallIn(xxxComponent.class)进行绑定
@HiltViewModel
//@Inject支持构造函数、方法和字段注解，也可能使用于静态实例成员。可注解成员可以是任意修饰符（private,package-private,protected,public）。
//注入顺序：构造函数、字段，然后是方法。父类的字段和方法注入优先于子类的字段和方法，同一类中的字段和方法是没有顺序的。
class IntroductionViewModel @Inject constructor(
    //SharedPreferences对象本身只能获取数据而不支持存储和修改
    //存储修改是通过SharedPreferences.edit()获取的内部接口Editor对象实现
    private val sharedPreference : SharedPreferences,
    private val firebaseAuth: FirebaseAuth
)
    : ViewModel(){

        private val _navigate = MutableStateFlow(0)
        //StateFlow 是一个状态容器式可观察数据流，可以向其收集器发出当前状态更新和新状态更新。
        val navigate : StateFlow<Int> = _navigate

        //Kotlin语言中使用"companion object"修饰静态方法
        companion object{
            const val SHOPPING_ACTIVITY = 23
            const val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accontOptionsFragment
        }

    init {
            val isButtonClicked = sharedPreference.getBoolean(INTRODUCTION_KEY,false)
            val user = firebaseAuth.currentUser

            if(user != null){
                viewModelScope.launch {
                    _navigate.emit(SHOPPING_ACTIVITY)
                }
            }else if(isButtonClicked){
                viewModelScope.launch {
                    _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
                }
            }else{
                Unit
            }
    }

    fun startButtonClick(){
        sharedPreference.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }

}