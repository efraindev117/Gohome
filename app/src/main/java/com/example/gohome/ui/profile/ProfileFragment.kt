package com.example.gohome.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gohome.R
import com.example.gohome.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUser()

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_qrFormFragment)

        }

    }

    private fun getUser() {
            binding.let { binding ->
                FirebaseAuth.getInstance().currentUser?.let { user ->
                    binding.userName.setText(user.displayName)
                    binding.txtEmail.setText(user.email)
                    Glide.with(this).load(user.photoUrl).centerCrop().into(binding.imgProfile)
                }
            }
    }
}