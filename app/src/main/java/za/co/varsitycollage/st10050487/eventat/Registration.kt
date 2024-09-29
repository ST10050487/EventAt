package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration) // Make sure this references your XML layout

        // Initialize views
        val editTextFullName: EditText = findViewById(R.id.editTextFullName)
        val editTextSurname: EditText = findViewById(R.id.editTextSurname)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val buttonSignUp: Button = findViewById(R.id.buttonSignUp)
        val textViewSignIn: TextView = findViewById(R.id.you_have_ac)

        // Handle "Create account" button click
        buttonSignUp.setOnClickListener {
            val fullName = editTextFullName.text.toString().trim()
            val surname = editTextSurname.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validate input fields
            if (fullName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                // If all validations pass
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                // Proceed with account creation logic here (e.g., store in DB, call API)
            }
        }

        // Handle "Sign in" text click
        textViewSignIn.setOnClickListener {
            // Redirect to login screen or perform other logic
            Toast.makeText(this, "Redirecting to sign in", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    // Helper function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}