package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import za.co.varsitycollage.st10050487.eventat.databinding.FragmentMyEventsBinding

class MyEventsFragment : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)

        // Set up the ViewPager and the TabLayout
        val adapter = EventsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Attach the TabLayout to the ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Saved"
                1 -> "Upcoming"
                else -> ""
            }
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
