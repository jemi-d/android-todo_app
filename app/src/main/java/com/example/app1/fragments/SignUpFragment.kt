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
import com.example.app1.databinding.SignUpFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = SignUpFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvent()
    }

    private fun init(view: View){
        println("signUp comes")
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private  fun registerEvent(){

        binding.signInText.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.signupBtn.setOnClickListener {
            val email = binding.emailField.text.toString().trim()
            val password = binding.passField.text.toString().trim()
            val cPassword = binding.retypePassField.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty() && cPassword.isNotEmpty()){
                if(password == cPassword){
                    binding.progressBarSignUp.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signUpFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, it.exception?.toString(), Toast.LENGTH_SHORT).show()
                        }

                        binding.progressBarSignUp.visibility = View.GONE
                    }
                }else{
                    Toast.makeText(context, "Password and conform password not same", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Empty Field not applicable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}