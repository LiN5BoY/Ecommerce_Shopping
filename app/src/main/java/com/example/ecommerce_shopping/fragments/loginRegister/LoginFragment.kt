package com.example.ecommerce_shopping.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.activities.ShoppingActivity
import com.example.ecommerce_shopping.databinding.FragmentLoginBinding
import com.example.ecommerce_shopping.util.Resource
import com.example.ecommerce_shopping.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

//使用依赖注入
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    //lateinit——之后再初始化
    private lateinit var binding : FragmentLoginBinding
    //by是Kotlin中的一个关键字,用于实现委托。
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                //密码中可能含有空格，故不使用trim
                val password = edPassworddLogin.text.toString()
                viewModel.login(email,password)
            }
        }
        // * lifecycle - 生命周期
        // * lifecycle 的主要作用是，让其他组件可以监听 Activity/Fragment 的生命周期，其好处如下
        // * 1、以前 Activity/Fragment 需要在不同的生命周期对一些组件做相应的操作，现在改为由组件自己处理，从而降低了耦合性
        // * 2、避免一些组件持有 Activity/Fragment 后不释放导致的内存泄漏问题
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.buttonLoginLogin.revertAnimation()
                        //requireActivity获取宿主Activity的引用
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.buttonLoginLogin.revertAnimation()
                    }
                    else -> Unit//void
                }
            }
        }
    }


}