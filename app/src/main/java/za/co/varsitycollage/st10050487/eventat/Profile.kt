package za.co.varsitycollage.st10050487.eventat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import za.co.varsitycollage.st10050487.eventat.databinding.FragmentProfileBinding

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Initialize SharedPreferences to retrieve logged-in user data
        sharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        // Load and display user data (first name and surname)
        if (loggedInUserEmail != null) {
            fetchUserData(loggedInUserEmail)
        }

        // Handle the "Change Password" button click
        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(requireContext(), ChangePassword::class.java)
            startActivity(intent) // Navigate to ChangePassword activity
        }

        // Handle the "Manage Profile" button click
        binding.manageProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), ManageProfile::class.java)
            startActivity(intent) // Navigate to ManageProfile activity
        }

        // Handle the "Multi-Factor Authentication" button click
        binding.multiFactorButton.setOnClickListener {
            val intent = Intent(requireContext(), DisplayBioMetric::class.java)
            startActivity(intent) // Navigate to Multi-Factor Authentication activity
        }

        return binding.root
    }

    private fun fetchUserData(loggedInUserEmail: String) {
        val query = database.orderByChild("email").equalTo(loggedInUserEmail)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val fullName = userSnapshot.child("fullName").getValue(String::class.java) ?: ""
                        val surname = userSnapshot.child("surname").getValue(String::class.java) ?: ""

                        // Set the full name and surname on the UI
                        binding.userName.text = "$fullName $surname"
                    }
                } else {
                    // Handle if no user data found
                    binding.userName.text = "User not found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error case
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
