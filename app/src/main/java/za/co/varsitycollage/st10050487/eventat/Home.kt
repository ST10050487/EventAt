package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import za.co.varsitycollage.st10050487.eventat.Fragments.CreateEvent
import za.co.varsitycollage.st10050487.eventat.Fragments.HomeScreen
import za.co.varsitycollage.st10050487.eventat.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Loading the HomeScreen fragment as the default one
        replaceFragment(HomeScreen())

        binding.Menu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeBtn-> {
                    replaceFragment(HomeScreen())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.MyEventsBtn -> {
                    replaceFragment(CreateEvent())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.CreateEventsBtn -> {
                    replaceFragment(CreateEvent())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.ProfileBtn -> {
                    replaceFragment(CreateEvent())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransient = fragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.fragmentContainer, fragment)
        fragmentTransient.commit()
    }
}