package com.example.ecommerce_shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_shopping.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Hilt把Dagger 手动创建Component 改成了预定义的Component，且自动集成到Android应用程序的各个生命周期中。
//通过注解的方式@InstallIn(xxxComponent.class)进行绑定
@HiltViewModel
//@Inject支持构造函数、方法和字段注解，也可能使用于静态实例成员。可注解成员可以是任意修饰符（private,package-private,protected,public）。
//注入顺序：构造函数、字段，然后是方法。父类的字段和方法注入优先于子类的字段和方法，同一类中的字段和方法是没有顺序的。
class LoginViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth
): ViewModel() {
    //SharedFlow可以将已发送过的数据发送给新的订阅者，并且具有高的配置性。
    //1、是热数据流 ，及时没有接收者，也会发射数据
    //2、SharedFlow 是 StateFlow 的可配置性极高的泛化数据流。
    //3、可以有多个接收器，一个数据可以被多个接收
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email : String,password : String){
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(
            email,password
        ).addOnSuccessListener {
            //ViewModel 会有一个扩展属性 viewModelScope，其可以将协程绑定到 ViewModel 生命周期
            //即 ViewModel 销毁时，协程也会自动被取消
            viewModelScope.launch {
                it.user?.let {
                    _login.emit(Resource.Success(it))
                }
            }
        }.addOnFailureListener{
            viewModelScope.launch {
                _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun resetPassword(email : String){
        //ViewModel 会有一个扩展属性 viewModelScope，其可以将协程绑定到 ViewModel 生命周期
        //即 ViewModel 销毁时，协程也会自动被取消
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
            }
    }




}