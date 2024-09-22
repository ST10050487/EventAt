package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.eventat.Fragments.InfoEvent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add the InfoEvent fragment to the activity
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                add(R.id.fragment_info_event, InfoEvent())
                commit()
            }
        }
    }
}