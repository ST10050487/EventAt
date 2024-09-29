package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Get references to the input fields and button
        val newPassword: EditText = findViewById(R.id.Password)
        val confirmPassword: EditText = findViewById(R.id.confirm_password)
        val resetButton: Button = findViewById(R.id.send_code_button)

        // Handle "Reset password" button click
        resetButton.setOnClickListener {
            val password = newPassword.text.toString()
            val verificationCode = confirmPassword.text.toString()

            // Check if the password and code are not empty
            if (password.isEmpty()) {
                newPassword.error = "Please enter a new password"
            } else if (verificationCode.isEmpty()) {
                confirmPassword.error = "Please enter the verification code"
            } else {
                // Here you would typically validate the verification code and reset the password
                // For example, make a network request to the server to reset the password

                // For now, simulate successful password reset
                Toast.makeText(this, "Password has been reset successfully!", Toast.LENGTH_SHORT).show()

                // Optionally, navigate back to the login screen or another activity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Optionally close the current activity
            }
        }
    }
}