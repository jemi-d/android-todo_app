package com.example.app1.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.app1.R
import com.example.app1.databinding.SplashFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = SplashFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                if(auth.currentUser != null){
                    navControl.navigate(R.id.homeFragment)
                }else{
                    navControl.navigate(R.id.signUpFragment)
                }
            },2000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}