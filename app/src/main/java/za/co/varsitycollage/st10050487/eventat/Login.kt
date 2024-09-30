package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class Login : AppCompatActivity() {
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button
    lateinit var signUpText: TextView
    lateinit var forgotPasswordText: TextView

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initializing the Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Initializing views
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        signUpText = findViewById(R.id.sign_up_text) // ID for Sign Up TextView
        forgotPasswordText = findViewById(R.id.forgot_password) // ID for Forgot Password TextView

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (isValidCredentials(email, password)) {
                loginUser(email, password)
            }
        }

        // Navigate to the Sign Up page
        signUpText.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        // Navigate to the Reset Password page
        forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Validating the Email and Password validation
    private fun isValidCredentials(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                emailInput.error = "Please enter an email"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailInput.error = "Enter a valid email"
                false
            }
            TextUtils.isEmpty(password) -> {
                passwordInput.error = "Please enter a password"
                false
            }
            password.length < 6 -> {
                passwordInput.error = "Password must be at least 6 characters"
                false
            }
            else -> true
        }
    }

    // A function to check if the user exists in the Firebase Realtime Database
    private fun loginUser(email: String, password: String) {
        // Querying the Firebase to find user by email
        val query = database.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val storedPassword = userSnapshot.child("password").value.toString()
                        if (storedPassword == password) {
                            // Displaying Successfully login message
                            Toast.makeText(this@Login, "Login successful!", Toast.LENGTH_SHORT).show()

                            // Saving the user's login information using shared preferences
                            saveUserLoginInfo(email)

                            // Moving to the GoogleMapsAPI activity with source as "Login"
                            val intent = Intent(this@Login, GoogleMapsAPI::class.java).apply {
                                putExtra("source", "Login")
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            // Login failed
                            emailInput.error = "Login Failed"
                            passwordInput.error = "Login Failed"
                        }
                    }
                } else {
                    // Login failed
                    emailInput.error = "Login Failed"
                    passwordInput.error = "Login Failed"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Login, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to save user login info using SharedPreferences
    private fun saveUserLoginInfo(email: String) {
        val sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserEmail", email)
        editor.apply()
    }
}


