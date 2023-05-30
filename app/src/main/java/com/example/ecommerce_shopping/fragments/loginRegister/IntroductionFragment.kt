package com.example.ecommerce_shopping.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.activities.ShoppingActivity
import com.example.ecommerce_shopping.databinding.FragmentIntroductionBinding
import com.example.ecommerce_shopping.viewmodel.IntroductionViewModel
import com.example.ecommerce_shopping.viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.ecommerce_shopping.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding : FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect {
                when(it){
                    SHOPPING_ACTIVITY -> {
                        //requireActivity获取宿主Activity的引用
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                    ACCOUNT_OPTIONS_FRAGMENT -> {
                        //操作Navigation框架
                        findNavController().navigate(it)
                    }

                    else ->Unit
                }
            }
        }

        binding.buttonStart.setOnClickListener{
            viewModel.startButtonClick()
            //操作Navigation框架
            findNavController().navigate(R.id.action_introductionFragment_to_accontOptionsFragment)
        }
    }
}