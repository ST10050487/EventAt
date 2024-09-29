package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmailVerification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        // Get references to the UI elements
        val backArrow: ImageView = findViewById(R.id.back_arrow)
        val goBackButton: Button = findViewById(R.id.go_back_button)

        // Handle back arrow click - Go back to the previous screen (e.g., Forgot Password or Login)
        backArrow.setOnClickListener {
            val intent = Intent(this, Login::class.java)  // Assuming you want to go back to the Login screen
            startActivity(intent)
            finish()  // Finishes current activity and goes back to the previous one
        }

        // Handle "Go Back" button click
        goBackButton.setOnClickListener {
            // You can navigate to any activity, like Login or Forgot Password
            val intent = Intent(this, Login::class.java)  // Assuming you want to go back to the Login screen
            startActivity(intent)
            finish()  // Optionally finish this activity if you don't want it in the back stack

            // Show a message
            Toast.makeText(this, "Returning to Login", Toast.LENGTH_SHORT).show()
        }
    }
}
