package za.co.varsitycollage.st10050487.eventat

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class EventsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2 // We have two tabs: Saved and Upcoming
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Saved() // First tab is Saved
            1 -> UpComing() // Second tab is Upcoming
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}