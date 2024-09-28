package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.varsitycollage.st10050487.eventat.databinding.FragmentProfileBinding

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}