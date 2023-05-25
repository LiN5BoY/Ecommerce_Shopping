package com.example.ecommerce_shopping.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_shopping.R
import com.example.ecommerce_shopping.databinding.FragmentAccountOptionBinding
import com.example.ecommerce_shopping.databinding.FragmentIntroductionBinding

class AccontOptionsFragment : Fragment(R.layout.fragment_account_option) {
    private lateinit var binding: FragmentAccountOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLoginrAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_accontOptionsFragment_to_loginFragment)
        }
        binding.buttonRegisterAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_accontOptionsFragment_to_registerFragment)
        }
    }

}