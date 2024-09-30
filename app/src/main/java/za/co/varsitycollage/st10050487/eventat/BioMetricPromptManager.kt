package za.co.varsitycollage.st10050487.eventat

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class BioMetricPromptManager(
    private val activity: AppCompatActivity // Changed from MainActivity to AppCompatActivity
) {
    // Add this constructor
    constructor() : this(AppCompatActivity()) {
        // Initialization code if necessary
    }

    private val resultChannel = Channel<BiometricResult>()
    val promptResult = resultChannel.receiveAsFlow()

    fun showBiometricPrompt(
        activity: AppCompatActivity,  // Changed from MainActivity to AppCompatActivity
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String
    ) {
        val manager = BiometricManager.from(activity)

        // Set the appropriate authenticators based on SDK version
        val authenticator = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else {
            BIOMETRIC_STRONG
        }

        // Build the BiometricPrompt.PromptInfo
        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)

        // Set negative button text only if API level is below 30
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            promptInfoBuilder.setNegativeButtonText(negativeButtonText)
        } else {
            // For API 30 and above, allow device credential fallback (PIN, pattern, password)
            promptInfoBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        }

        val promptInfo = promptInfoBuilder.build()

        // Handle authentication based on the current biometric availability
        when (manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Perform biometric authentication or show the prompt
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            else -> Unit
        }

        // Setup BiometricPrompt
        val prompt = BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                resultChannel.trySend(BiometricResult.AuthenticationError(errString.toString()))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                resultChannel.trySend(BiometricResult.AuthenticationFailed)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
            }
        })

        // Trigger the authentication
        prompt.authenticate(promptInfo)
    }

    // Method to check if biometric authentication is not set
    fun isBiometricAuthenticationNotSet(): Boolean {
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }

    // Sealed interface for biometric results
    sealed interface BiometricResult {
        data object HardwareUnavailable : BiometricResult
        data object FeatureUnavailable : BiometricResult
        data class AuthenticationError(val error: String) : BiometricResult
        data object AuthenticationFailed : BiometricResult
        data object AuthenticationSuccess : BiometricResult
        data object AuthenticationNotSet : BiometricResult
    }
}