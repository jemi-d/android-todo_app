package com.example.app1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.app1.R
import com.example.app1.databinding.SignInFragmentBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = SignInFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        loginFun()
    }

    private fun init(view: View){
        auth = FirebaseAuth.getInstance()
        navControl = Navigation.findNavController(view)
    }

    private fun loginFun(){

        binding.signUpText.setOnClickListener {
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.signInBtn.setOnClickListener {
            val email = binding.emailField1.text.toString().trim()
            val password = binding.passField1.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()){
                binding.progressBarSignIn.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                    OnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(context,"Login Successfully", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.homeFragment)
                        }else{
                            Toast.makeText(context,it.exception?.toString(), Toast.LENGTH_SHORT).show()
                        }
                        binding.progressBarSignIn.visibility = View.GONE
                    }
                )

            }else{
                Toast.makeText(context,"Empty fields not applicable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}