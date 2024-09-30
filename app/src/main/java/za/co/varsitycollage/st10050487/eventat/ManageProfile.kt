package za.co.varsitycollage.st10050487.eventat

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageProfile : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backArrow: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_profile)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Correct SharedPreferences name
        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)

        // Initialize views
        firstNameEditText = findViewById(R.id.first_name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        saveButton = findViewById(R.id.save_button)
        backArrow = findViewById(R.id.back_arrow)

        // Back Arrow click listener
        backArrow.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Fetch and display the current user profile information
        fetchUserProfile()

        // Save button click listener
        saveButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (validateInput(firstName, surname, email)) {
                // Update the data in Firebase
                updateUserProfile(firstName, surname, email)
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(firstName: String, surname: String, email: String): Boolean {
        return firstName.isNotEmpty() && surname.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun fetchUserProfile() {
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            val usersRef = database.child("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userId: String? = null

                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        if (email == loggedInUserEmail) {
                            userId = userSnapshot.key
                            firstNameEditText.setText(userSnapshot.child("fullName").getValue(String::class.java))
                            surnameEditText.setText(userSnapshot.child("surname").getValue(String::class.java))
                            emailEditText.setText(userSnapshot.child("email").getValue(String::class.java))
                            break
                        }
                    }

                    if (userId == null) {
                        Toast.makeText(this@ManageProfile, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ManageProfile, "Failed to fetch user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No logged-in user found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfile(firstName: String, surname: String, email: String) {
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            val usersRef = database.child("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (userSnapshot in snapshot.children) {
                        val emailInDb = userSnapshot.child("email").getValue(String::class.java)
                        if (emailInDb == loggedInUserEmail) {
                            userSnapshot.ref.child("fullName").setValue(firstName)
                            userSnapshot.ref.child("surname").setValue(surname)
                            userSnapshot.ref.child("email").setValue(email)
                                .addOnSuccessListener {
                                    // Display success message
                                    Toast.makeText(this@ManageProfile, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                    finish() // Close the activity
                                }
                                .addOnFailureListener {
                                    // Display failure message
                                    Toast.makeText(this@ManageProfile, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                }
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ManageProfile, "Failed to fetch users: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No logged-in user found", Toast.LENGTH_SHORT).show()
        }
    }
}







