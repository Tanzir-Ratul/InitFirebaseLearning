package com.example.initfirebaselearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.initfirebaselearning.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // Initialize Firebase Auth
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.registerBtn.setOnClickListener {

            firebaseAuth(
                binding.email.text.toString().trim(),
                binding.passwordET.text.toString().trim()
            )
        }
    }

    private fun firebaseAuth(email: String?, password: String?) {
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Please verify your email", Toast.LENGTH_SHORT
                        ).show()
                        clearField()
                        updateUI()
                    }
                        ?.addOnFailureListener {
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        }


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    private fun updateUI() {

        findNavController().navigate(R.id.action_registrationFragment_to_itemSaveFragment)
    }

    private fun clearField() {
        binding.nameET.text.clear()
        binding.addressET.text?.clear()
        binding.email.text?.clear()
        binding.passwordET.text?.clear()
    }


}