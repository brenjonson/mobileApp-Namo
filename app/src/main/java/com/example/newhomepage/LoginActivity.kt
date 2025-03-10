package com.example.newhomepage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.newhomepage.repositories.AuthRepository
import com.example.newhomepage.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var forgotPasswordText: TextView

    private lateinit var authRepository: AuthRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize repository and session manager
        authRepository = AuthRepository()
        sessionManager = SessionManager(this)

        // ตรวจสอบว่าผู้ใช้เข้าสู่ระบบอยู่หรือไม่
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        // ค้นหา view elements
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // ตั้งค่า listener ให้กับปุ่มเข้าสู่ระบบ
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // ตรวจสอบว่าช่องกรอกข้อมูลไม่ว่างเปล่า
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // เรียกใช้ API สำหรับเข้าสู่ระบบ
            lifecycleScope.launch {
                try {
                    val result = authRepository.loginUser(email, password)

                    result.onSuccess { (token, user) ->
                        // บันทึกข้อมูล token และข้อมูลผู้ใช้
                        sessionManager.saveAuthToken(token)
                        sessionManager.saveUser(user)

                        Toast.makeText(this@LoginActivity, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    }.onFailure { exception ->
                        Toast.makeText(this@LoginActivity, "เข้าสู่ระบบไม่สำเร็จ: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "เกิดข้อผิดพลาด: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ตั้งค่า listener สำหรับลิงก์ลงทะเบียน
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ตั้งค่า listener สำหรับลิงก์ลืมรหัสผ่าน
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "คุณสมบัติ 'ลืมรหัสผ่าน' จะมาเร็วๆ นี้", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}