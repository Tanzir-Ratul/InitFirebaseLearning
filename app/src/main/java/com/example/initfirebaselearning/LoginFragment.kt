package com.example.initfirebaselearning

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.initfirebaselearning.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        onClick()
    }

     override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI()
        }
    }

    private fun onClick() {
        binding.loginBtn.setOnClickListener {
            authLogin()
        }

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.forgetPassTV.setOnClickListener{
            val dialog = Dialog(requireContext())
            val forgetPassword = LayoutInflater.from(context).inflate(R.layout.forget_password,null)
            dialog.setContentView(forgetPassword)
            dialog.setCancelable(true)
            dialog.create()
            dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.8).toInt(),
                (resources.displayMetrics.heightPixels * 0.8).toInt()
            )
           val email = forgetPassword.findViewById<EditText>(R.id.emailEt)
           val reset = forgetPassword.findViewById<Button>(R.id.resetBtn)
            reset.setOnClickListener {
                if(email.text.toString().trim().isNotEmpty()) {
                    auth.sendPasswordResetEmail((email.text.toString().trim()))
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Please check your email",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Timber.d("email: ${email} ${it.message}")
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                }            }
            dialog.show()


        }
    }

    private fun authLogin() {
        auth.signInWithEmailAndPassword(
            binding.emailET.text.toString().trim(),
            binding.passwordET.text.toString().trim()
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    if (auth.currentUser?.isEmailVerified == true) {
                        val user = auth.currentUser
                        updateUI()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    private fun updateUI() {
        findNavController().navigate(R.id.action_loginFragment_to_itemSaveFragment)
    }

}