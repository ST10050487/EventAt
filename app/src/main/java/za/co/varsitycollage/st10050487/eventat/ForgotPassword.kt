package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Get references to the UI elements
        val emailField: EditText = findViewById(R.id.email_field)
        val sendCodeButton: Button = findViewById(R.id.send_code_button)

        // Handle "Send Code" button click
        sendCodeButton.setOnClickListener {
            val email = emailField.text.toString().trim()

            // Check if the email field is empty
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show()
            }
            // Validate if the entered text is a valid email format
            else if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            // Proceed if the email is valid
            else {
                // Here, you would typically send a request to your server
                // to send a verification code to the entered email.

                // Display a success message
                Toast.makeText(this, "Verification code sent to $email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
