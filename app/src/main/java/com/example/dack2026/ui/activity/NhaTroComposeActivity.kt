package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dack2026.data.entity.NhaTroEntity
import com.example.dack2026.ui.viewmodel.NhaTroViewModel
import com.example.dack2026.utils.UserSessionManager

class NhaTroComposeActivity : ComponentActivity() {
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
                MainAppScaffold()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    var showAdminMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                                if (currentRoute != "home") {
                                    if (!navController.popBackStack()) {
                                        navController.navigate("home")
                                    }
                                } else {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                    (context as? android.app.Activity)?.finish()
                                }
                            },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại (<-)", tint = Color.White)
                        }

                        if (currentRoute == "home") {
                            IconButton(
                                onClick = { showAdminMenu = true },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu Admin", tint = Color.White)
                            }
                        }
                    }

                    Text(
                        text = when (currentRoute) {
                            "home" -> "Nhà Trọ Việt - Trang Chủ"
                            "tenants" -> "Quản Lý Khách Thuê"
                            "qrcode" -> "Mã QR Phòng Trọ"
                            "contracts" -> "Quản Lý Hợp Đồng"
                            "notifications" -> "Hệ Thống Thông Báo"
                            "invoices" -> "Quản Lý Hóa Đơn"
                            "transactions" -> "Quản Lý Thu - Chi"
                            "statistics" -> "Thống Kê Doanh Thu"
                            else -> "Quản Lý Nhà Trọ"
                        },
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = {
                        Toast.makeText(context, "Không có thông báo mới", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Thông báo", tint = Color.White)
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "Trang chủ",
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
                    )
                    BottomNavItem(
                        icon = Icons.Default.People,
                        label = "Khách thuê",
                        selected = currentRoute == "tenants",
                        onClick = { navController.navigate("tenants") { popUpTo("home") } }
                    )
                    // QR Code Center Prominent Button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF269900))
                            .clickable { navController.navigate("qrcode") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "QR Code",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    BottomNavItem(
                        icon = Icons.Default.Description,
                        label = "Hợp đồng",
                        selected = currentRoute == "contracts",
                        onClick = { navController.navigate("contracts") { popUpTo("home") } }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Notifications,
                        label = "Thông báo",
                        selected = currentRoute == "notifications",
                        onClick = { navController.navigate("notifications") { popUpTo("home") } }
                    )
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("tenants") { TenantsScreen(navController) }
            composable("qrcode") { QRCodeScreen(navController) }
            composable("contracts") { ContractsScreen(navController) }
            composable("notifications") { NotificationsScreen(navController) }
            composable("invoices") { InvoicesScreen(navController) }
            composable("transactions") { TransactionsScreen(navController) }
            composable("statistics") { StatisticsScreen(navController) }
        }
    }

    if (showAdminMenu) {
        AdminMenuDialog(
            onDismiss = { showAdminMenu = false },
            onInvoices = { showAdminMenu = false; navController.navigate("invoices") },
            onTransactions = { showAdminMenu = false; navController.navigate("transactions") },
            onStatistics = { showAdminMenu = false; navController.navigate("statistics") },
            onLogout = {
                showAdminMenu = false
                UserSessionManager.clearSession(context)
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) Color(0xFF269900) else Color.Gray
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = tint)
    }
}

// 1. Trang Chủ / Danh Sách Nhà Trọ (Fully Optimized 60 FPS)
@Composable
fun HomeScreen(navController: NavController, viewModel: NhaTroViewModel = viewModel()) {
    val nhaTroList by viewModel.allNhaTro.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedForEdit by remember { mutableStateOf<NhaTroEntity?>(null) }
    var selectedForDelete by remember { mutableStateOf<NhaTroEntity?>(null) }
    val context = LocalContext.current

    val filteredList = remember(nhaTroList, searchQuery) {
        nhaTroList.filter {
            it.tenNhaTro.contains(searchQuery, ignoreCase = true) ||
                    it.diaChi.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Tìm kiếm nhà trọ theo tên hoặc địa chỉ...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF269900)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF269900),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Search)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(title = "Hóa đơn", icon = Icons.Default.Receipt) {
                    context.startActivity(Intent(context, HoaDonActivity::class.java))
                }
                QuickActionButton(title = "Thu - Chi", icon = Icons.Default.AccountBalanceWallet) {
                    context.startActivity(Intent(context, ThuChiActivity::class.java))
                }
                QuickActionButton(title = "Thống kê", icon = Icons.Default.BarChart) {
                    context.startActivity(Intent(context, ThongKeActivity::class.java))
                }
            }

            Text(
                text = "Danh sách dãy trọ (${filteredList.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không tìm thấy nhà trọ nào", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredList, key = { it.id }) { nhaTro ->
                        NhaTroCardItem(
                            nhaTro = nhaTro,
                            onPhongClick = {
                                val intent = Intent(context, PhongActivity::class.java).apply {
                                    putExtra("nhaTroId", nhaTro.id)
                                    putExtra("nhaTroName", nhaTro.tenNhaTro)
                                }
                                context.startActivity(intent)
                            },
                            onEdit = { selectedForEdit = nhaTro },
                            onDelete = { selectedForDelete = nhaTro }
                        )
                    }
                }
            }
        }

        // Functional Floating Action Button (+)
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFF269900),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm nhà trọ", tint = Color.White)
        }
    }

    if (showAddDialog) {
        AddNhaTroDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { ten, diaChi, moTa, gia ->
                viewModel.insert(NhaTroEntity(tenNhaTro = ten, diaChi = diaChi, moTa = moTa, giaMacDinh = gia))
                showAddDialog = false
                Toast.makeText(context, "Đã thêm nhà trọ $ten", Toast.LENGTH_SHORT).show()
            }
        )
    }

    selectedForEdit?.let { nhaTro ->
        EditNhaTroDialog(
            nhaTro = nhaTro,
            onDismiss = { selectedForEdit = null },
            onSave = { updated ->
                viewModel.update(updated)
                selectedForEdit = null
                Toast.makeText(context, "Đã cập nhật nhà trọ", Toast.LENGTH_SHORT).show()
            }
        )
    }

    selectedForDelete?.let { nhaTro ->
        AlertDialog(
            onDismissRequest = { selectedForDelete = null },
            title = { Text("Xóa Nhà Trọ", fontWeight = FontWeight.Bold, color = Color.Red) },
            text = { Text("Bạn có chắc chắn muốn xóa ${nhaTro.tenNhaTro}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.delete(nhaTro)
                        selectedForDelete = null
                        Toast.makeText(context, "Đã xóa nhà trọ", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedForDelete = null }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun QuickActionButton(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF269900), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        }
    }
}

@Composable
fun NhaTroCardItem(
    nhaTro: NhaTroEntity,
    onPhongClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF269900))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(nhaTro.tenNhaTro, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("Đ/c: ${nhaTro.diaChi}", fontSize = 13.sp, color = Color.Gray)
                    }
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color(0xFF1976D2))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color(0xFFD32F2F))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("💰 Giá mặc định: ${nhaTro.giaMacDinh.toInt()} đ", fontSize = 13.sp, color = Color.DarkGray)
                    Text("📝 Mô tả: ${nhaTro.moTa.ifEmpty { "Không có" }}", fontSize = 12.sp, color = Color.Gray)
                }
                Button(
                    onClick = onPhongClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Phòng trọ →", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// 2. Khách Thuê Tab
@Composable
fun TenantsScreen(navController: NavController) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF269900), modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quản lý Khách Thuê", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quản lý toàn bộ thông tin, CCCD và lịch sử thuê phòng.", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { context.startActivity(Intent(context, KhachThueActivity::class.java)) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Mở Quản Lý Khách Thuê", color = Color.White)
            }
        }

        // (+) Floating Action Button on Tenants Tab
        FloatingActionButton(
            onClick = { context.startActivity(Intent(context, KhachThueActivity::class.java)) },
            containerColor = Color(0xFF269900),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm khách thuê", tint = Color.White)
        }
    }
}

// 3. QR Code Tab (Quét & Tạo mã thanh toán chuyển khoản VietQR)
@Composable
fun QRCodeScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("BankPrefs", android.content.Context.MODE_PRIVATE) }
    
    var bankName by remember { mutableStateOf(prefs.getString("bankName", "MB Bank") ?: "MB Bank") }
    var accountNumber by remember { mutableStateOf(prefs.getString("accountNumber", "0987654321") ?: "0987654321") }
    var accountHolder by remember { mutableStateOf(prefs.getString("accountHolder", "NGUYEN VAN CHU TRO") ?: "NGUYEN VAN CHU TRO") }
    val amount by remember { mutableStateOf("2500000") }
    val roomName by remember { mutableStateOf("Phòng 101") }

    var showConfigDialog by remember { mutableStateOf(false) }
    var showPaymentCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color(0xFF269900), modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("QR Thanh Toán Tiền Trọ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF269900))
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("🏦 Ngân hàng: $bankName", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        Text("💳 STK: $accountNumber", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Text("👤 Chủ tài khoản: $accountHolder", fontSize = 13.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Phòng: $roomName", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Số tiền: ${amount.toDoubleOrNull()?.let { "%,.0f đ".format(it) } ?: amount}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nội dung CK: Thanh toan $roomName",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showConfigDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cấu Hình STK & Ngân Hàng Chủ Trọ", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showPaymentCard = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900)),
            modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hiển Thị Mã QR Chuyển Khoản Nhanh", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    if (showConfigDialog) {
        var tempBank by remember { mutableStateOf(bankName) }
        var tempAcc by remember { mutableStateOf(accountNumber) }
        var tempHolder by remember { mutableStateOf(accountHolder) }

        AlertDialog(
            onDismissRequest = { showConfigDialog = false },
            title = { Text("Cấu Hình Tài Khoản Chủ Trọ", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(value = tempBank, onValueChange = { tempBank = it }, label = { Text("Tên Ngân Hàng (VD: MB, VCB, Techcombank)") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = tempAcc, onValueChange = { tempAcc = it }, label = { Text("Số Tài Khoản (STK)") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = tempHolder, onValueChange = { tempHolder = it }, label = { Text("Tên Chủ Tài Khoản (VIET HOA)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        bankName = tempBank
                        accountNumber = tempAcc
                        accountHolder = tempHolder.uppercase()
                        prefs.edit().putString("bankName", bankName).putString("accountNumber", accountNumber).putString("accountHolder", accountHolder).apply()
                        showConfigDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
                ) {
                    Text("Lưu Lại", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfigDialog = false }) { Text("Hủy") }
            }
        )
    }

    if (showPaymentCard) {
        val qrUrl = "https://img.vietqr.io/image/$bankName-$accountNumber-compact2.png?amount=$amount&addInfo=Thanh%20toan%20tien%20phong%20$roomName&accountName=$accountHolder"

        AlertDialog(
            onDismissRequest = { showPaymentCard = false },
            title = { Text("Mã QR Thanh Toán VietQR", fontWeight = FontWeight.Bold, color = Color(0xFF269900)) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Khách thuê quét mã bằng app ngân hàng sẽ tự động điền số tiền và STK!", fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    coil.compose.AsyncImage(
                        model = qrUrl,
                        contentDescription = "VietQR Code",
                        modifier = Modifier.size(240.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("STK: $accountNumber ($bankName)", fontWeight = FontWeight.Bold)
                    Text("Chủ TK: $accountHolder", fontSize = 13.sp)
                    Text("Số tiền: ${amount.toDoubleOrNull()?.let { "%,.0f đ".format(it) } ?: amount}", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 16.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPaymentCard = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
                ) {
                    Text("Đã Xác Nhận", color = Color.White)
                }
            }
        )
    }
}

// 4. Hợp Đồng Tab
@Composable
fun ContractsScreen(navController: NavController) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF269900), modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quản lý Hợp Đồng Thuê Phòng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Theo dõi thời hạn, tiền cọc và điều khoản hợp đồng.", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { context.startActivity(Intent(context, HopDongActivity::class.java)) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Mở Quản Lý Hợp Đồng", color = Color.White)
            }
        }

        // (+) Floating Action Button on Contracts Tab
        FloatingActionButton(
            onClick = { context.startActivity(Intent(context, HopDongActivity::class.java)) },
            containerColor = Color(0xFF269900),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm hợp đồng", tint = Color.White)
        }
    }
}

// 5. Thông Báo Tab
@Composable
fun NotificationsScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            NotificationCard(title = "Hóa đơn đến hạn", message = "Phòng 102 chưa thanh toán hóa đơn tháng 06/2026.", time = "10 phút trước", type = "warning")
        }
        item {
            NotificationCard(title = "Hợp đồng sắp hết hạn", message = "Hợp đồng phòng 204 sẽ hết hạn vào ngày 30/06/2026.", time = "2 giờ trước", type = "info")
        }
        item {
            NotificationCard(title = "Thanh toán thành công", message = "Phòng 101 đã thanh toán 1,800,000 đ tiền phòng.", time = "Hôm qua", type = "success")
        }
    }
}

@Composable
fun NotificationCard(title: String, message: String, time: String, type: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (type) {
                            "warning" -> Color(0xFFFFEBEE)
                            "success" -> Color(0xFFE8F5E9)
                            else -> Color(0xFFE3F2FD)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (type) {
                        "warning" -> Icons.Default.Warning
                        "success" -> Icons.Default.CheckCircle
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    tint = when (type) {
                        "warning" -> Color(0xFFC62828)
                        "success" -> Color(0xFF2E7D32)
                        else -> Color(0xFF1565C0)
                    }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(2.dp))
                Text(message, fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(time, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun InvoicesScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, HoaDonActivity::class.java))
    }
}

@Composable
fun TransactionsScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, ThuChiActivity::class.java))
    }
}

@Composable
fun StatisticsScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, ThongKeActivity::class.java))
    }
}

@Composable
fun AdminMenuDialog(
    onDismiss: () -> Unit,
    onInvoices: () -> Unit,
    onTransactions: () -> Unit,
    onStatistics: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Menu Quản Trị (Admin)", fontWeight = FontWeight.Bold, color = Color(0xFF269900)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminMenuItem(icon = Icons.Default.Receipt, title = "Quản lý Hóa đơn Điện nước", onClick = onInvoices)
                AdminMenuItem(icon = Icons.Default.AccountBalanceWallet, title = "Quản lý Thu - Chi", onClick = onTransactions)
                AdminMenuItem(icon = Icons.Default.BarChart, title = "Thống kê Doanh thu", onClick = onStatistics)
                HorizontalDivider()
                AdminMenuItem(icon = Icons.AutoMirrored.Filled.Logout, title = "Đăng xuất tài khoản", onClick = onLogout, tint = Color.Red)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = Color.Gray)
            }
        }
    )
}

@Composable
fun AdminMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = Color(0xFF269900)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AddNhaTroDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Double) -> Unit
) {
    var ten by remember { mutableStateOf("") }
    var diaChi by remember { mutableStateOf("") }
    var moTa by remember { mutableStateOf("") }
    var gia by remember { mutableStateOf("1500000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Dãy Trọ Mới", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = ten,
                    onValueChange = { ten = it },
                    label = { Text("Tên dãy trọ / Nhà trọ") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = diaChi,
                    onValueChange = { diaChi = it },
                    label = { Text("Địa chỉ") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô tả / Ghi chú") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = gia,
                    onValueChange = { gia = it },
                    label = { Text("Giá phòng mặc định (VNĐ)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ten.isNotBlank() && diaChi.isNotBlank()) {
                        onAdd(ten, diaChi, moTa, gia.toDoubleOrNull() ?: 1500000.0)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
            ) {
                Text("Thêm", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}

@Composable
fun EditNhaTroDialog(
    nhaTro: NhaTroEntity,
    onDismiss: () -> Unit,
    onSave: (NhaTroEntity) -> Unit
) {
    var ten by remember { mutableStateOf(nhaTro.tenNhaTro) }
    var diaChi by remember { mutableStateOf(nhaTro.diaChi) }
    var moTa by remember { mutableStateOf(nhaTro.moTa) }
    var gia by remember { mutableStateOf(nhaTro.giaMacDinh.toInt().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sửa Thông Tin Dãy Trọ", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = ten, onValueChange = { ten = it }, label = { Text("Tên dãy trọ") }, singleLine = true)
                OutlinedTextField(value = diaChi, onValueChange = { diaChi = it }, label = { Text("Địa chỉ") }, singleLine = true)
                OutlinedTextField(value = moTa, onValueChange = { moTa = it }, label = { Text("Mô tả") }, singleLine = true)
                OutlinedTextField(value = gia, onValueChange = { gia = it }, label = { Text("Giá phòng mặc định (VNĐ)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ten.isNotBlank() && diaChi.isNotBlank()) {
                        onSave(nhaTro.copy(tenNhaTro = ten.trim(), diaChi = diaChi.trim(), moTa = moTa.trim(), giaMacDinh = gia.toDoubleOrNull() ?: nhaTro.giaMacDinh))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF269900))
            ) {
                Text("Cập Nhật", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}
