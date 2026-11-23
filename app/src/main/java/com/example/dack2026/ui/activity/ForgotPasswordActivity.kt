package com.example.dack2026.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dack2026.ui.viewmodel.AuthViewModel

class ForgotPasswordActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF66BB6A),
                    background = Color(0xFFF5F5F5),
                    surface = Color.White
                )
            ) {
                ForgotPasswordScreen(
                    authViewModel = authViewModel,
                    onSuccess = {
                        Toast.makeText(this, "Đã cập nhật mật khẩu mới!", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val updatePasswordResult by authViewModel.updatePasswordResult.observeAsState()

    LaunchedEffect(updatePasswordResult) {
        updatePasswordResult?.let { success ->
            isLoading = false
            if (success) {
                onSuccess()
            } else {
                Toast.makeText(context, "Không tìm thấy tên đăng nhập trong hệ thống", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val handleSubmit = {
        if (username.trim().isEmpty() || newPassword.trim().isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu mới", Toast.LENGTH_SHORT).show()
        } else {
            keyboardController?.hide()
            isLoading = true
            authViewModel.forgotPassword(username.trim(), newPassword.trim())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại (<-)",
                        tint = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Quên / Đổi Mật Khẩu",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            Icon(
                imageVector = Icons.Default.LockReset,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Nhập tên đăng nhập và mật khẩu mới để cập nhật tài khoản",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // 1. Username Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tên đăng nhập",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập tên đăng nhập...", color = Color.Gray, fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    trailingIcon = {
                        if (username.isNotEmpty()) {
                            IconButton(onClick = { username = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF66BB6A),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. New Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mật khẩu mới",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập mật khẩu mới...", color = Color.Gray, fontSize = 14.sp) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { handleSubmit() }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF66BB6A),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Submit Button
            Button(
                onClick = handleSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Cập nhật mật khẩu", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Back Button
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
            ) {
                Text("Quay lại", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
