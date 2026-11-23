package com.example.dack2026.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import com.example.dack2026.data.entity.UserEntity
import com.example.dack2026.ui.viewmodel.AuthViewModel
import com.example.dack2026.ui.viewmodel.HoaDonViewModel
import com.example.dack2026.ui.viewmodel.NhaTroViewModel
import com.example.dack2026.ui.viewmodel.PhongViewModel
import com.example.dack2026.ui.viewmodel.ThongKeViewModel
import com.example.dack2026.utils.UserSessionManager

class AdminComposeActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val nhaTroViewModel: NhaTroViewModel by viewModels()
    private val phongViewModel: PhongViewModel by viewModels()
    private val hoaDonViewModel: HoaDonViewModel by viewModels()
    private val thongKeViewModel: ThongKeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF2E7D32),
                    background = Color(0xFFF5F5F5),
                    surface = Color.White
                )
            ) {
                AdminDashboardScreen(
                    authViewModel = authViewModel,
                    nhaTroViewModel = nhaTroViewModel,
                    phongViewModel = phongViewModel,
                    hoaDonViewModel = hoaDonViewModel,
                    thongKeViewModel = thongKeViewModel,
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
fun AdminDashboardScreen(
    authViewModel: AuthViewModel,
    nhaTroViewModel: NhaTroViewModel,
    phongViewModel: PhongViewModel,
    hoaDonViewModel: HoaDonViewModel,
    thongKeViewModel: ThongKeViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val usersList by authViewModel.allUsers.observeAsState(emptyList())
    val nhaTroList by nhaTroViewModel.allNhaTro.observeAsState(emptyList())
    val phongList by phongViewModel.allPhong.observeAsState(emptyList())
    val context = LocalContext.current

    var showAddUserDialog by remember { mutableStateOf(false) }
    var selectedUserForEdit by remember { mutableStateOf<UserEntity?>(null) }
    var selectedUserForDelete by remember { mutableStateOf<UserEntity?>(null) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B5E20))
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Trở về (<-)", tint = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD54F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Hệ Thống Admin Full", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Admin: ${UserSessionManager.getFullName(context)}", color = Color(0xFFA5D6A7), fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Đăng xuất", tint = Color.White)
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ManageAccounts, contentDescription = null) },
                    label = { Text("Tài khoản", fontSize = 11.sp) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.HomeWork, contentDescription = null) },
                    label = { Text("Nhà & Phòng", fontSize = 11.sp) },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                    label = { Text("Hóa đơn", fontSize = 11.sp) },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                    label = { Text("Thống kê", fontSize = 11.sp) },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (selectedTab) {
                        0 -> showAddUserDialog = true
                        1 -> context.startActivity(Intent(context, PhongActivity::class.java))
                        2 -> context.startActivity(Intent(context, HoaDonActivity::class.java))
                        3 -> context.startActivity(Intent(context, ThuChiActivity::class.java))
                    }
                },
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = when (selectedTab) {
                        0 -> Icons.Default.PersonAdd
                        1 -> Icons.Default.AddHome
                        2 -> Icons.Default.PostAdd
                        else -> Icons.Default.AddCard
                    },
                    contentDescription = "Thêm mới (+)"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            when (selectedTab) {
                0 -> AdminUsersTab(
                    users = usersList,
                    onEditUser = { selectedUserForEdit = it },
                    onDeleteUser = { selectedUserForDelete = it }
                )
                1 -> AdminPropertiesTab(
                    nhaTroList = nhaTroList,
                    phongList = phongList,
                    onOpenLandlordView = {
                        context.startActivity(Intent(context, NhaTroComposeActivity::class.java))
                    }
                )
                2 -> AdminInvoicesTab(
                    hoaDonViewModel = hoaDonViewModel,
                    onOpenInvoiceActivity = {
                        context.startActivity(Intent(context, HoaDonActivity::class.java))
                    }
                )
                3 -> AdminStatisticsTab(
                    thongKeViewModel = thongKeViewModel,
                    onOpenStatsActivity = {
                        context.startActivity(Intent(context, ThongKeActivity::class.java))
                    }
                )
            }
        }
    }

    if (showAddUserDialog) {
        AdminAddUserDialog(
            onDismiss = { showAddUserDialog = false },
            onAddUser = { username, password, fullName, phone, role ->
                authViewModel.register(
                    UserEntity(
                        username = username,
                        password = password,
                        fullName = fullName,
                        phone = phone,
                        role = role
                    )
                )
                showAddUserDialog = false
                Toast.makeText(context, "Đã thêm tài khoản $username", Toast.LENGTH_SHORT).show()
            }
        )
    }

    selectedUserForEdit?.let { user ->
        AdminEditRoleDialog(
            user = user,
            onDismiss = { selectedUserForEdit = null },
            onSave = { newRole ->
                authViewModel.updateRole(user.id, newRole)
                selectedUserForEdit = null
                Toast.makeText(context, "Đã đổi vai trò cho ${user.username} thành $newRole", Toast.LENGTH_SHORT).show()
            }
        )
    }

    selectedUserForDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUserForDelete = null },
            title = { Text("Xóa tài khoản", fontWeight = FontWeight.Bold, color = Color.Red) },
            text = { Text("Bạn có chắc chắn muốn xóa tài khoản '${user.username}' (${user.fullName})?") },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.deleteUser(user)
                        selectedUserForDelete = null
                        Toast.makeText(context, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedUserForDelete = null }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
}

// Tab 0: Account Management (Optimized for 60 FPS)
@Composable
fun AdminUsersTab(
    users: List<UserEntity>,
    onEditUser: (UserEntity) -> Unit,
    onDeleteUser: (UserEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf("ALL") }

    val filteredUsers = remember(users, searchQuery, selectedRoleFilter) {
        users.filter { user ->
            (selectedRoleFilter == "ALL" || user.role == selectedRoleFilter) &&
                    (user.fullName.contains(searchQuery, ignoreCase = true) ||
                            user.username.contains(searchQuery, ignoreCase = true) ||
                            user.phone.contains(searchQuery, ignoreCase = true))
        }
    }

    val adminCount = remember(users) { users.count { it.role == UserEntity.ROLE_ADMIN } }
    val landlordCount = remember(users) { users.count { it.role == UserEntity.ROLE_CHU_TRO } }
    val tenantCount = remember(users) { users.count { it.role == UserEntity.ROLE_NGUOI_THUE } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Summary Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminCountBadge("Tổng số", users.size, Color(0xFF1E88E5), Modifier.weight(1f))
            AdminCountBadge("👑 Admin", adminCount, Color(0xFFF57C00), Modifier.weight(1f))
            AdminCountBadge("🏠 Chủ trọ", landlordCount, Color(0xFF2E7D32), Modifier.weight(1f))
            AdminCountBadge("👤 Khách", tenantCount, Color(0xFF8E24AA), Modifier.weight(1f))
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Tìm kiếm tài khoản theo tên/username/SĐT...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2E7D32)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2E7D32),
                unfocusedBorderColor = Color.LightGray
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Search)
        )

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(selected = selectedRoleFilter == "ALL", onClick = { selectedRoleFilter = "ALL" }, label = { Text("Tất cả (${users.size})") })
            FilterChip(selected = selectedRoleFilter == UserEntity.ROLE_ADMIN, onClick = { selectedRoleFilter = UserEntity.ROLE_ADMIN }, label = { Text("👑 Admin") })
            FilterChip(selected = selectedRoleFilter == UserEntity.ROLE_CHU_TRO, onClick = { selectedRoleFilter = UserEntity.ROLE_CHU_TRO }, label = { Text("🏠 Chủ trọ") })
            FilterChip(selected = selectedRoleFilter == UserEntity.ROLE_NGUOI_THUE, onClick = { selectedRoleFilter = UserEntity.ROLE_NGUOI_THUE }, label = { Text("👤 Khách") })
        }

        if (filteredUsers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy tài khoản nào", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    AdminUserCardItem(
                        user = user,
                        onEditUser = { onEditUser(user) },
                        onDeleteUser = { onDeleteUser(user) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminCountBadge(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text("$count", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun AdminUserCardItem(
    user: UserEntity,
    onEditUser: () -> Unit,
    onDeleteUser: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            when (user.role) {
                                UserEntity.ROLE_ADMIN -> Color(0xFFFFF3E0)
                                UserEntity.ROLE_CHU_TRO -> Color(0xFFE8F5E9)
                                else -> Color(0xFFF3E5F5)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (user.role) {
                            UserEntity.ROLE_ADMIN -> "👑"
                            UserEntity.ROLE_CHU_TRO -> "🏠"
                            else -> "👤"
                        },
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(user.fullName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        RoleBadgeChip(role = user.role)
                    }
                    Text("User: @${user.username} • SĐT: ${user.phone.ifEmpty { "N/A" }}", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Row {
                IconButton(onClick = onEditUser) {
                    Icon(Icons.Default.EditNote, contentDescription = "Sửa quyền", tint = Color(0xFF1976D2))
                }
                IconButton(onClick = onDeleteUser) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color(0xFFD32F2F))
                }
            }
        }
    }
}

@Composable
fun RoleBadgeChip(role: String) {
    val (label, bg, text) = when (role) {
        UserEntity.ROLE_ADMIN -> Triple("ADMIN", Color(0xFFFFECB3), Color(0xFFE65100))
        UserEntity.ROLE_CHU_TRO -> Triple("CHỦ TRỌ", Color(0xFFC8E6C9), Color(0xFF1B5E20))
        else -> Triple("KHÁCH THUÊ", Color(0xFFE1BEE7), Color(0xFF4A148C))
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

// Tab 1: Properties & Rooms
@Composable
fun AdminPropertiesTab(
    nhaTroList: List<com.example.dack2026.data.entity.NhaTroEntity>,
    phongList: List<com.example.dack2026.data.entity.PhongEntity>,
    onOpenLandlordView: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🏠 Tổng quan Hệ thống Dãy Trọ & Phòng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Total Dãy trọ: ${nhaTroList.size}", fontSize = 14.sp)
                Text("• Total Phòng trọ: ${phongList.size}", fontSize = 14.sp)
                Text("• Phòng đang thuê: ${phongList.count { it.trangThai == "Đang thuê" }}", fontSize = 14.sp, color = Color(0xFF2E7D32))
                Text("• Phòng còn trống: ${phongList.count { it.trangThai != "Đang thuê" }}", fontSize = 14.sp, color = Color(0xFFE65100))
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenLandlordView,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Mở Chế Độ Quản Lý Dãy Trọ →", color = Color.White)
                }
            }
        }
    }
}

// Tab 2: Invoices
@Composable
fun AdminInvoicesTab(
    hoaDonViewModel: HoaDonViewModel,
    onOpenInvoiceActivity: () -> Unit
) {
    val invoiceList by hoaDonViewModel.allHoaDon.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🧾 Quản Lý Toàn Bộ Hóa Đơn", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Tổng số hóa đơn: ${invoiceList.size}", fontSize = 14.sp)
                Text("• Đã thanh toán: ${invoiceList.count { it.trangThai == "Đã thanh toán" }}", fontSize = 14.sp, color = Color(0xFF2E7D32))
                Text("• Chưa thanh toán: ${invoiceList.count { it.trangThai != "Đã thanh toán" }}", fontSize = 14.sp, color = Color.Red)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenInvoiceActivity,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Chi Tiết Hóa Đơn →", color = Color.White)
                }
            }
        }
    }
}

// Tab 3: Finance & Statistics
@Composable
fun AdminStatisticsTab(
    thongKeViewModel: ThongKeViewModel,
    onOpenStatsActivity: () -> Unit
) {
    val rev by thongKeViewModel.totalRevenue.observeAsState(0.0)
    val thu by thongKeViewModel.totalThu.observeAsState(0.0)
    val chi by thongKeViewModel.totalChi.observeAsState(0.0)

    LaunchedEffect(Unit) {
        thongKeViewModel.loadStatistics()
    }

    val profit = (rev ?: 0.0) + (thu ?: 0.0) - (chi ?: 0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Báo Cáo Tài Chính Hệ Thống", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                Spacer(modifier = Modifier.height(12.dp))
                Text("💰 Doanh thu Hóa đơn: ${rev?.toInt()} đ", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text("📥 Thu khác: ${thu?.toInt()} đ", fontSize = 15.sp, color = Color(0xFF2E7D32))
                Text("📤 Tổng chi: ${chi?.toInt()} đ", fontSize = 15.sp, color = Color.Red)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("💎 Lợi Nhuận Thuần: ${profit.toInt()} đ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onOpenStatsActivity,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Xem Báo Cáo Đầy Đủ →", color = Color.White)
                }
            }
        }
    }
}

// Dialog Add User
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddUserDialog(
    onDismiss: () -> Unit,
    onAddUser: (String, String, String, String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserEntity.ROLE_CHU_TRO) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Tài Khoản Mới", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Họ và tên") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Next))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Next), singleLine = true)
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Next))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mật khẩu") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = androidx.compose.ui.text.input.ImeAction.Done))
                Text("Chọn Vai Trò:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilterChip(selected = role == UserEntity.ROLE_CHU_TRO, onClick = { role = UserEntity.ROLE_CHU_TRO }, label = { Text("Chủ trọ") })
                    FilterChip(selected = role == UserEntity.ROLE_NGUOI_THUE, onClick = { role = UserEntity.ROLE_NGUOI_THUE }, label = { Text("Khách") })
                    FilterChip(selected = role == UserEntity.ROLE_ADMIN, onClick = { role = UserEntity.ROLE_ADMIN }, label = { Text("Admin") })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank() && fullName.isNotBlank()) {
                        onAddUser(username.trim(), password.trim(), fullName.trim(), phone.trim(), role)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Lưu", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}

// Dialog Edit User Role
@Composable
fun AdminEditRoleDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selectedRole by remember { mutableStateOf(user.role) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Phân Quyền Cho User", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tài khoản: ${user.username} (${user.fullName})", fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Chọn vai trò mới:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = selectedRole == UserEntity.ROLE_CHU_TRO, onClick = { selectedRole = UserEntity.ROLE_CHU_TRO }, label = { Text("🏠 Chủ trọ") })
                    FilterChip(selected = selectedRole == UserEntity.ROLE_NGUOI_THUE, onClick = { selectedRole = UserEntity.ROLE_NGUOI_THUE }, label = { Text("👤 Khách") })
                    FilterChip(selected = selectedRole == UserEntity.ROLE_ADMIN, onClick = { selectedRole = UserEntity.ROLE_ADMIN }, label = { Text("👑 Admin") })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(selectedRole) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Cập Nhật", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}
