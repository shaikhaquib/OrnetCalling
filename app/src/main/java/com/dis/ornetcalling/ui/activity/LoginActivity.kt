package com.dis.ornetcalling.ui.activity

import com.dis.ornetcalling.architecture.MainViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dis.ornetcalling.sharedPref.SessionManager
import com.dis.ornetcalling.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(applicationContext)

        if (sessionManager.isSessionActive()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                showLoading(true)
                viewModel.login(email, password)
            }
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            showLoading(false)
            result.onSuccess { res ->
                sessionManager.saveUserSession(res.token, res.user_id, res.email)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
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
        return true
    }

    private fun showLoading(isLoading: Boolean) {
       // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
        binding.btnSignUp.isEnabled = !isLoading
    }

    private fun saveToken(token: String) {
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().putString("user_token", "Token $token").apply()
    }
    private fun getToken(): String {
        val token = SessionManager(this).getUserToken()
        return token?:""
    }
}