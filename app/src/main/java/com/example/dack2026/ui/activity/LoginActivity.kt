package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.example.dack2026.data.entity.UserEntity
import com.example.dack2026.ui.viewmodel.AuthViewModel
import com.example.dack2026.utils.UserSessionManager

class LoginActivity : ComponentActivity() {
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
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { user ->
                        UserSessionManager.saveUserSession(this, user)
                        Toast.makeText(this, "Đăng nhập thành công (${user.fullName})", Toast.LENGTH_SHORT).show()
                        routeToRoleDashboard(user)
                    },
                    onRegisterClick = {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    },
                    onForgotPasswordClick = {
                        startActivity(Intent(this, ForgotPasswordActivity::class.java))
                    }
                )
            }
        }
    }

    private fun routeToRoleDashboard(user: UserEntity) {
        val intent = when (user.role) {
            UserEntity.ROLE_ADMIN -> Intent(this, AdminComposeActivity::class.java)
            UserEntity.ROLE_NGUOI_THUE -> Intent(this, TenantComposeActivity::class.java)
            else -> Intent(this, NhaTroComposeActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (UserEntity) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val loginResult by authViewModel.loginResult.observeAsState()
    val loginAttempted by authViewModel.loginAttempted.observeAsState(false)

    LaunchedEffect(loginResult, loginAttempted) {
        if (loginAttempted) {
            isLoading = false
            val user = loginResult
            if (user != null) {
                authViewModel.resetState()
                onLoginSuccess(user)
            } else {
                Toast.makeText(context, "Sai tên đăng nhập hoặc mật khẩu! Vui lòng thử lại.", Toast.LENGTH_LONG).show()
                authViewModel.resetState()
            }
        }
    }

    val handleLoginSubmit = {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        } else {
            keyboardController?.hide()
            isLoading = true
            authViewModel.login(username.trim(), password.trim())
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
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Illustration Container
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IllustrationGraphic()
            }

            Text(
                text = "Quản lý nhà trọ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
            )

            // 2. Form Input: Tên đăng nhập
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
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

            // 3. Form Input: Mật khẩu
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mật khẩu",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập mật khẩu...", color = Color.Gray, fontSize = 14.sp) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { handleLoginSubmit() }),
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

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Green Pill Button: Đăng nhập
            Button(
                onClick = handleLoginSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Blue Text Link: Quên mật khẩu?
            Text(
                text = "Quên mật khẩu?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .clickable { onForgotPasswordClick() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Grey Pill Button: Đăng kí tài khoản
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
            ) {
                Text("Đăng kí tài khoản", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun IllustrationGraphic() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = Color(0xFFF8BBD0).copy(alpha = 0.5f),
            radius = width * 0.42f,
            center = Offset(width * 0.5f, height * 0.5f)
        )

        val phoneWidth = width * 0.42f
        val phoneHeight = height * 0.65f
        val phoneLeft = (width - phoneWidth) / 2
        val phoneTop = (height - phoneHeight) / 2

        drawRoundRect(
            color = Color(0xFFC2185B),
            topLeft = Offset(phoneLeft, phoneTop),
            size = Size(phoneWidth, phoneHeight),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(phoneLeft + 4.dp.toPx(), phoneTop + 6.dp.toPx()),
            size = Size(phoneWidth - 8.dp.toPx(), phoneHeight - 12.dp.toPx()),
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )

        val gridRows = 3
        val gridCols = 3
        val itemSize = 10.dp.toPx()
        val spacing = 6.dp.toPx()
        val startX = phoneLeft + 12.dp.toPx()
        val startY = phoneTop + 16.dp.toPx()

        for (r in 0 until gridRows) {
            for (c in 0 until gridCols) {
                drawRoundRect(
                    color = if ((r + c) % 2 == 0) Color(0xFFE91E63) else Color(0xFFF48FB1),
                    topLeft = Offset(startX + c * (itemSize + spacing), startY + r * (itemSize + spacing)),
                    size = Size(itemSize, itemSize),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
            }
        }

        drawCircle(
            color = Color(0xFF424242),
            radius = 7.dp.toPx(),
            center = Offset(phoneLeft - 10.dp.toPx(), phoneTop + phoneHeight * 0.4f)
        )
        drawRoundRect(
            color = Color(0xFF1E88E5),
            topLeft = Offset(phoneLeft - 16.dp.toPx(), phoneTop + phoneHeight * 0.48f),
            size = Size(12.dp.toPx(), 22.dp.toPx()),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawCircle(color = Color(0xFFE91E63), radius = 4.dp.toPx(), center = Offset(phoneLeft - 22.dp.toPx(), phoneTop + 20.dp.toPx()))
        drawCircle(color = Color(0xFFC2185B), radius = 6.dp.toPx(), center = Offset(phoneLeft - 28.dp.toPx(), phoneTop + 40.dp.toPx()))
        drawCircle(color = Color(0xFFE91E63), radius = 4.dp.toPx(), center = Offset(phoneLeft + phoneWidth + 22.dp.toPx(), phoneTop + 30.dp.toPx()))
        drawCircle(color = Color(0xFFC2185B), radius = 5.dp.toPx(), center = Offset(phoneLeft + phoneWidth + 26.dp.toPx(), phoneTop + 55.dp.toPx()))
    }
}
