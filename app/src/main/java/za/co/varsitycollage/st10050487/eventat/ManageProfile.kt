package za.co.varsitycollage.st10050487.eventat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ManageProfile: AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_profile)

        // Initialize views
        nameEditText = findViewById(R.id.name_edit_text)
        birthdateEditText = findViewById(R.id.birthdate_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        saveButton = findViewById(R.id.save_button)
        backArrow = findViewById(R.id.back_arrow)

        // Back Arrow click listener to return to the profile page
        backArrow.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Save button click listener
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val birthdate = birthdateEditText.text.toString()
            val email = emailEditText.text.toString()

            if (validateInput(name, birthdate, email)) {
                // Perform save action
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close this activity and return to the profile page
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validation for name, birthdate, and email
    private fun validateInput(name: String, birthdate: String, email: String): Boolean {
        return name.isNotEmpty() && birthdate.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}