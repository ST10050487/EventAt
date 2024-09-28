package za.co.varsitycollage.st10050487.eventat


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.varsitycollage.st10050487.eventat.databinding.FragmentUpComingBinding

class UpComing : Fragment() {

    private var _binding: FragmentUpComingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpComingBinding.inflate(inflater, container, false)

        // Here you can set up your upcoming events logic, for now you can leave it blank
        // and move to setting up event handlers for the RecyclerView later

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}