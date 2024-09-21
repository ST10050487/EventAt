package za.co.varsitycollage.st10050487.eventat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DisplayBioMetric : AppCompatActivity() {
    private val manager by lazy { BioMetricPromptManager(this) }

    private lateinit var enrollLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.displaybiometric)  // Set the XML layout

        // Find the views from the XML layout
        val statusTextView = findViewById<TextView>(R.id.biometricStatusText)
        val authenticateButton = findViewById<Button>(R.id.authenticateButton)

        // Initialize the ActivityResultLauncher for enrolling biometrics
        enrollLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the result of the biometric enrollment activity
            println("Enroll result: $result")
        }

        // Set a click listener to the authenticate button to show the biometric prompt
        authenticateButton.setOnClickListener {
            manager.showBiometricPrompt(
                activity = this@DisplayBioMetric,
                title = "Biometric Authentication",
                subtitle = "Authenticate using your biometric data",
                description = "Please authenticate to proceed",
                negativeButtonText = "Use password"
            )
        }

        // Use lifecycleScope to launch a coroutine for collecting the biometric result
        lifecycleScope.launch {
            manager.promptResult.collect { biometricResult ->
                when (biometricResult) {
                    is BioMetricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        Toast.makeText(this@DisplayBioMetric, "Biometric authentication successful", Toast.LENGTH_SHORT).show()
                        statusTextView.text = "Authentication successful"
                    }
                    is BioMetricPromptManager.BiometricResult.AuthenticationFailed -> {
                        Toast.makeText(this@DisplayBioMetric, "Biometric authentication failed", Toast.LENGTH_SHORT).show()
                        statusTextView.text = "Authentication failed"
                    }
                    is BioMetricPromptManager.BiometricResult.FeatureUnavailable -> {
                        Toast.makeText(this@DisplayBioMetric, "Biometric authentication is not available", Toast.LENGTH_SHORT).show()
                        statusTextView.text = "Biometric authentication unavailable"
                    }
                    else -> {
                        statusTextView.text = "Waiting for authentication..."
                    }
                }
            }
        }

        // Check if biometric authentication is set up and launch enrollment if needed
        checkAndEnrollBiometrics()
    }

    private fun checkAndEnrollBiometrics() {
        if (Build.VERSION.SDK_INT >= 30) {
            // Perform a check if biometric authentication is not set up
            if (manager.isBiometricAuthenticationNotSet()) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                enrollLauncher.launch(enrollIntent)
            }
        }
    }
}

