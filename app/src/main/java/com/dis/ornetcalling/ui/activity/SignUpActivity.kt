package com.dis.ornetcalling.ui.activity
import com.dis.ornetcalling.architecture.MainViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btnSignUp.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(fullName, email, password)) {
                showLoading(true)
                viewModel.signUp(fullName, email, password)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.signUpResult.observe(this) { result ->
            showLoading(false)
            result.onSuccess { token ->
                SessionManager(this).saveUserSession(token.token,token.user_id,token.email)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Sign up failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(fullName: String, email: String, password: String): Boolean {
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            return false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters long"
            return false
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnSignUp.isEnabled = !isLoading
    }

    private fun saveToken(token: String) {
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().putString("user_token", "Token $token").apply()
    }
}