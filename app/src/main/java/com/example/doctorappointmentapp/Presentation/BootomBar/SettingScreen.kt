package com.example.doctorappointmentapp.Presentation.BootomBar

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import com.example.doctorappointmentapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController, viewModel: MainViewModel,userId:String) {
    val userState by viewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserDetails(userId)
    }

    Text("Settings", fontSize = 22.sp, modifier = Modifier.padding( top = 30.dp, start = 20.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Image from Base64
        if (userState.profileImageUrl.isNotEmpty()) {
            val imageBytes = Base64.decode(userState.profileImageUrl, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userState.fullName,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingItem(title = "Profile", icon = Icons.Default.Person)
        SettingItem(title = "Payment Methods", icon = Icons.Default.ShoppingCart)
        SettingItem(title = "Favourite", icon = Icons.Default.Favorite)
        SettingItem(title = "Setting", icon = Icons.Default.Settings)
        SettingItem(title = "Help Center", icon = Icons.Default.Warning)
        SettingItem(title = "Privacy Policy", icon = Icons.Default.Info)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }  // Clear all backstack
                    }
                },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = colorResource(id = R.color.purple)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Logout", fontSize = 16.sp)
                }
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = colorResource(id = R.color.purple)
                )
            }
        }
    }
}

@Composable
fun SettingItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = colorResource(id = R.color.purple))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, fontSize = 16.sp)
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = colorResource(id = R.color.purple)
            )
        }
    }
}
