package com.example.ecommerce_shopping.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.data.User

import com.example.ecommerce_shopping.databinding.FragmentRegisterBinding
import com.example.ecommerce_shopping.util.RegisterValidation
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        //inflater：LayoutInflater 对象，用于将 XML 布局文件转换为对应的 View 对象。
        inflater: LayoutInflater,
        //container：ViewGroup 对象，表示 Fragment 所在的容器。
        container: ViewGroup?,
        //savedInstanceState：Bundle 对象，用于保存和恢复 Fragment 的状态信息。
        savedInstanceState: Bundle?
    ): View? {
        //initialization
        binding = FragmentRegisterBinding.inflate(inflater)
        //root：View 对象，表示 Fragment 所创建的视图。
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.apply {
            buttonRegister.setOnClickListener {
                val user = User(
                    //使用 trim() 函数来删除 EditText 或 TextView 中用户输入的前导和尾随空格。
                    edRegisterFirstName.text.toString().trim(),
                    edRegisterLastName.text.toString().trim(),
                    edRegisterEmail.text.toString().trim()
                )
                val password = edRegisterPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        // * lifecycle 的主要作用是，让其他组件可以监听 Activity/Fragment 的生命周期，其好处如下
        // * 1、以前 Activity/Fragment 需要在不同的生命周期对一些组件做相应的操作，现在改为由组件自己处理，从而降低了耦合性
        // * 2、避免一些组件持有 Activity/Fragment 后不释放导致的内存泄漏问题
        lifecycleScope.launchWhenStarted {
            //通过 collect() 收集数据，最终更新界面
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        //调用View的startAnimation()方法启动动画
                        binding.buttonRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test",it.message.toString())
                        //停止动画
                        binding.buttonRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.d(TAG,it.message.toString())
                        //ERROR时停止动画
                        binding.buttonRegister.revertAnimation()
                    }
                    //kotlin中的Unit相当于Java中的Void，并且可以写 return Unit
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            //通过 collect() 收集数据，最终更新界面
            viewModel.validation.collect { validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edRegisterEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edRegisterPassword.apply{
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }

}