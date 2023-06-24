package com.example.initfirebaselearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.initfirebaselearning.databinding.FragmentItemSaveBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemSaveFragment : Fragment() {
    private lateinit var binding: FragmentItemSaveBinding
    //private val binding get() = _binding

    private val db = Firebase.firestore
    // private lateinit var auth: FirebaseAuth
    // private var userHashMap: HashMap<String, String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // Initialize Firebase Auth
        binding = FragmentItemSaveBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addBtn.setOnClickListener {
            //try{
            val name = binding.nameET.text.toString().trim()
            val address = binding.addressET.text.toString().trim()
            val pass = binding.passwordET.text.toString().trim()
            val userHashMap = hashMapOf(
                ItemSaveFragment.name to  name,
                ItemSaveFragment.address to address,
                ItemSaveFragment.password to pass
            )
            val userId:String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            //Timber.d("userId $userId details $userHashMap")
            db.collection("user").document(userId).set(userHashMap)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Successfully saved", Toast.LENGTH_SHORT)
                        .show()
                    clearField()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to save", Toast.LENGTH_SHORT)
                        .show()
                }
            // }
//            catch (e:java.lang.Exception){
//                Timber.d("Exception ${e.message}")
//            }
        }
        binding.signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().popBackStack(R.id.loginFragment,false)
        }
    }

    private fun doWork() {


    }

    private fun clearField() {
        binding.nameET.text.clear()
        binding.addressET.text?.clear()
        binding.passwordET.text?.clear()
    }

    companion object {
        const val name = "name"
        const val address = "address"
        const val password = "password"
    }
}