package com.example.dack2026.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dack2026.data.entity.UserEntity
import com.example.dack2026.ui.viewmodel.AuthViewModel

class RegisterActivity : ComponentActivity() {
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
                RegisterScreen(
                    authViewModel = authViewModel,
                    onSuccess = {
                        Toast.makeText(this, "Đăng ký tài khoản thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show()
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
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserEntity.ROLE_CHU_TRO) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val registerResult by authViewModel.registerResult.observeAsState()

    LaunchedEffect(registerResult) {
        registerResult?.let { success ->
            isLoading = false
            if (success) {
                onSuccess()
            } else {
                Toast.makeText(context, "Tên đăng nhập đã tồn tại trong cơ sở dữ liệu!", Toast.LENGTH_SHORT).show()
            }
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
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "Đăng ký tài khoản",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            // 1. Full Name Field
            CustomInputField(
                label = "Họ và tên",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Nhập họ và tên đầy đủ..."
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Phone Field
            CustomInputField(
                label = "Số điện thoại",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Nhập số điện thoại...",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Username Field
            CustomInputField(
                label = "Tên đăng nhập",
                value = username,
                onValueChange = { username = it },
                placeholder = "Nhập tên đăng nhập..."
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Password Field
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
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

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Public Registration Roles: 🏠 Chủ trọ or 👤 Người thuê
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Chọn vai trò người dùng:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoleOptionChip(
                        title = "🏠 Chủ nhà trọ",
                        selected = selectedRole == UserEntity.ROLE_CHU_TRO,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedRole = UserEntity.ROLE_CHU_TRO }
                    )
                    RoleOptionChip(
                        title = "👤 Người thuê trọ",
                        selected = selectedRole == UserEntity.ROLE_NGUOI_THUE,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedRole = UserEntity.ROLE_NGUOI_THUE }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 6. Submit Button
            Button(
                onClick = {
                    if (fullName.trim().isEmpty() || phone.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        val user = UserEntity(
                            username = username.trim(),
                            password = password.trim(),
                            fullName = fullName.trim(),
                            phone = phone.trim(),
                            role = selectedRole
                        )
                        authViewModel.register(user)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Đăng ký", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7. Back Button
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
            ) {
                Text("Quay lại Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
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
}

@Composable
fun RoleOptionChip(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFFE8F5E9) else Color.White
        ),
        border = if (selected) BorderStroke(2.dp, Color(0xFF66BB6A)) else BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color(0xFF2E7D32) else Color.DarkGray
            )
        }
    }
}
