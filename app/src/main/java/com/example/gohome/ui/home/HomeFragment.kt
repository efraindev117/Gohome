package com.example.gohome.ui.home
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gohome.databinding.FragmentHomeBinding
import com.google.android.material.transition.MaterialSharedAxis

import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?):View{
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fun getUserData(){
            binding.let { binding ->
                FirebaseAuth.getInstance().currentUser?.let { user ->
                    //binding.tv.setText(user.email)
                   // binding.tvUserName.setText(user.displayName)
                    Glide.with(this).load(user.photoUrl)
                        .centerCrop()
                     //   .into(binding.ivUserName)
                }
            }
        }
        return binding.root
    }


}


