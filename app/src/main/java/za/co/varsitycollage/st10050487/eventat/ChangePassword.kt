package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangePassword: AppCompatActivity() {

    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Initialize views
        newPasswordInput = findViewById(R.id.new_password)
        confirmPasswordInput = findViewById(R.id.confirm_password)
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox)
        saveButton = findViewById(R.id.save_button)
        backArrow = findViewById(R.id.back_arrow)

        // Handle the back arrow click to return to the previous screen (profile)
        backArrow.setOnClickListener {
            finish() // This will close the current activity and go back to the previous one.
        }

        // Handle save button click
        saveButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Call a method to handle password change
                changePassword(newPassword)
            }
        }
    }

    private fun changePassword(newPassword: String) {
        // This is where you would implement the logic for changing the password
        // This might involve saving the password to a database, Firebase, etc.
        // For now, we can just display a success message
        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()
    }
}