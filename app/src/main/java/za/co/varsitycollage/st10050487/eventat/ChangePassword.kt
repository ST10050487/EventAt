package za.co.varsitycollage.st10050487.eventat

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePassword : AppCompatActivity() {

    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var saveButton: Button
    private lateinit var backArrow: ImageView
    private lateinit var currentPasswordTextView: TextView

    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Initialize views
        newPasswordInput = findViewById(R.id.new_password)
        confirmPasswordInput = findViewById(R.id.confirm_password)
        saveButton = findViewById(R.id.save_button)
        backArrow = findViewById(R.id.back_arrow)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Correct SharedPreferences name
        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)

        // Fetch and display the current password
        fetchCurrentPassword()

        // Handle the back arrow click to return to the previous screen (profile)
        backArrow.setOnClickListener {
            finish()
        }

        // Handle save button click
        saveButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                newPasswordInput.error = "Please enter a new password"
                confirmPasswordInput.error = "Please confirm the new password"
            } else if (newPassword.length < 6) {
                newPasswordInput.error = "Password must be at least 6 characters"
            } else if (newPassword != confirmPassword) {
                confirmPasswordInput.error = "Passwords do not match"
            } else {
                // Call a method to handle password change
                changePassword(newPassword)
            }
        }
    }

    private fun fetchCurrentPassword() {
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            val safeEmail = loggedInUserEmail.replace(".", "_")

            val usersRef = FirebaseDatabase.getInstance().getReference("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userId: String? = null

                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        if (email == loggedInUserEmail) {
                            userId = userSnapshot.key
                            val currentPassword = userSnapshot.child("password").getValue(String::class.java)
                            break
                        }
                    }

                    if (userId == null) {
                        Toast.makeText(this@ChangePassword, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChangePassword, "Failed to fetch current password: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No logged-in user found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changePassword(newPassword: String) {
        val loggedInUserEmail = sharedPreferences.getString("loggedInUserEmail", null)

        if (loggedInUserEmail != null) {
            val safeEmail = loggedInUserEmail.replace(".", "_")
            val usersRef = FirebaseDatabase.getInstance().getReference("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        if (email == loggedInUserEmail) {
                            userSnapshot.ref.child("password").setValue(newPassword)
                                .addOnSuccessListener {
                                    // Display success message
                                    Toast.makeText(this@ChangePassword, "Password changed successfully!", Toast.LENGTH_SHORT).show()

                                    // Clear the input fields
                                    newPasswordInput.text.clear()
                                    confirmPasswordInput.text.clear()
                                }
                                .addOnFailureListener {
                                    // Display failure message
                                    Toast.makeText(this@ChangePassword, "Failed to change password", Toast.LENGTH_SHORT).show()
                                }
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChangePassword, "Failed to fetch users: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}
