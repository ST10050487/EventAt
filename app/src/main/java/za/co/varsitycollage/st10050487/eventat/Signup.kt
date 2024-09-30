package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Ensure this points to your layout file name

        // Initialize views
        val buttonSignUp: Button = findViewById(R.id.button_sign_up)
        val buttonGoogle: LinearLayout = findViewById(R.id.button_google)
        val logInTextView: TextView = findViewById(R.id.log_in)

        // Disable the Google button and set a message
        buttonGoogle.isEnabled = false
        buttonGoogle.setOnClickListener {
            Toast.makeText(this, "Google sign-in is currently disabled", Toast.LENGTH_SHORT).show()
        }

// Handle "Sign Up" button click
        buttonSignUp.setOnClickListener {
            // Perform sign-up action or navigate to the sign-up activity
            val intent = Intent(this, Registration::class.java) // Replace with your SignUpActivity
            startActivity(intent)
        }

// Handle "Log In" text click
        logInTextView.setOnClickListener {
            // Navigate to the login activity
            val intent = Intent(this, Login::class.java) // Replace with your LoginActivity
            startActivity(intent)
        }
    }
}
