package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dack2026.utils.UserSessionManager

class TenantComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF269900),
                    background = Color(0xFFF5F5F5),
                    surface = Color.White
                )
            ) {
                TenantDashboardScreen(
                    onLogout = {
                        UserSessionManager.clearSession(this)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDashboardScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val tenantName = UserSessionManager.getFullName(context)
    var showPaymentQrDialog by remember { mutableStateOf(false) }
    var showReportIssueDialog by remember { mutableStateOf(false) }
    var isPaid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF269900))
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                (context as? android.app.Activity)?.finish()
                            },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Thoát (<-)", tint = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Cổng Khách Thuê Trọ", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Xin chào, $tenantName 👋", color = Color(0xFFC8E6C9), fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Đăng xuất", tint = Color.White)
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showReportIssueDialog = true },
                containerColor = Color(0xFF269900),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.AddComment, contentDescription = "Báo sự cố (+)")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. My Room Info Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF269900))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Phòng 101 - Dãy Trọ Thanh Nội", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text("Đ/c: Thôn Giả - Xã Nội Hoàng, Bắc Giang", fontSize = 13.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TenantDetailItem("📐 Diện tích", "25 m²")
                            TenantDetailItem("💰 Giá thuê", "1,500,000 đ")
                            TenantDetailItem("📌 Trạng thái", "Đang thuê", Color(0xFF2E7D32))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                Toast.makeText(context, "SĐT Chủ trọ: 0912.345.678 (Chủ trọ Nguyễn Văn A)", Toast.LENGTH_LONG).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gọi cho Chủ Trọ: 0912.345.678", color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }

            // 2. Utility Bill / Monthly Invoice Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🧾 Hóa Đơn Điện Nước Tháng 06/2026", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF269900))
                            Surface(
                                color = if (isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isPaid) "Đã thanh toán" else "Chưa thanh toán",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPaid) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        InvoiceLineItem("🏠 Tiền phòng", "1,500,000 đ")
                        InvoiceLineItem("⚡ Tiền điện (Chỉ số 0 -> 50 kWh @ 3,500 đ)", "175,000 đ")
                        InvoiceLineItem("💧 Tiền nước (Chỉ số 0 -> 10 m³ @ 10,000 đ)", "100,000 đ")
                        InvoiceLineItem("🧹 Phí dịch vụ & Vệ sinh", "100,000 đ")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Tổng Cộng Cần Thanh Toán:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("1,875,000 đ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isPaid) {
                            Button(
                                onClick = { showPaymentQrDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Thanh Toán Qua Mã QR Banking", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Hóa đơn đã được xác nhận thanh toán thành công", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đã Hoàn Tất Thanh Toán", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. My Lease Contract Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF269900))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("📄 Hợp Đồng Thuê Phòng Của Tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("• Thời hạn: 01/01/2026 đến 01/01/2027", fontSize = 13.sp, color = Color.DarkGray)
                        Text("• Tiền cọc giữ phòng: 1,500,000 đ", fontSize = 13.sp, color = Color.DarkGray)
                        Text("• Trạng thái hợp đồng: Hiệu lực (Còn 7 tháng)", fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // 4. Important Announcements / Notifications Feed
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFFE65100))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("📢 Thông Báo Từ Chủ Trọ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        AnnouncementItem("Hạn đóng tiền phòng", "Vui lòng hoàn tất thanh toán hóa đơn tháng 06 trước ngày 05/06/2026.", "10 phút trước")
                        AnnouncementItem("Về việc bảo trì camera", "Hệ thống camera an ninh dãy A sẽ được bảo dưỡng vào sáng thứ 7.", "Hôm qua")
                    }
                }
            }
        }
    }

    if (showPaymentQrDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentQrDialog = false },
            title = { Text("Mã QR Thanh Toán Chuyển Khoản", fontWeight = FontWeight.Bold, color = Color(0xFF269900)) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color(0xFF269900), modifier = Modifier.size(120.dp))
                            Text("MB BANK: 9999202699", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Số tiền: 1,875,000 đ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFC62828))
                    Text("Nội dung: Phong 101 Thang 06", fontSize = 12.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isPaid = true
                        showPaymentQrDialog = false
                        Toast.makeText(context, "Xác nhận thanh toán thành công! Chủ trọ sẽ nhận được thông báo.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
                ) {
                    Text("Xác Nhận Đã Chuyển Khoản", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentQrDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }

    if (showReportIssueDialog) {
        var issueText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showReportIssueDialog = false },
            title = { Text("Báo Sự Cố / Gửi Phản Hồi Cho Chủ Trọ", fontWeight = FontWeight.Bold, color = Color(0xFF269900)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nhập thông tin sự cố (hỏng bóng đèn, đường nước, wifi...):", fontSize = 13.sp, color = Color.DarkGray)
                    OutlinedTextField(
                        value = issueText,
                        onValueChange = { issueText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Mô tả chi tiết sự cố cần hỗ trợ...") },
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (issueText.isNotBlank()) {
                            showReportIssueDialog = false
                            Toast.makeText(context, "Đã gửi báo cáo sự cố đến chủ trọ!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
                ) {
                    Text("Gửi Báo Cáo", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportIssueDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun TenantDetailItem(label: String, value: String, valueColor: Color = Color.DarkGray) {
    Column {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun InvoiceLineItem(title: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 12.sp, color = Color.DarkGray)
        Text(amount, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}

@Composable
fun AnnouncementItem(title: String, content: String, time: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
            Text(time, fontSize = 11.sp, color = Color.Gray)
        }
        Text(content, fontSize = 12.sp, color = Color.DarkGray)
        HorizontalDivider(modifier = Modifier.padding(top = 6.dp), color = Color(0xFFF0F0F0))
    }
}
