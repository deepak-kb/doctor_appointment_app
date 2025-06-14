package com.example.doctorappointmentapp.Presentation.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctorappointmentapp.Presentation.BootomBar.BottomBar
import com.example.doctorappointmentapp.Presentation.BootomBar.ProfileScreen
import com.example.doctorappointmentapp.Presentation.BootomBar.SettingScreen
import com.example.doctorappointmentapp.Presentation.BootomBar.WishlistScreen
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.example.doctorappointmentapp.R

@Composable
fun HomeScreen(userId: String, navController: NavController, viewModel: MainViewModel) {

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it },
//                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).padding(10.dp)) {
            when (selectedIndex) {
                0 -> HomeContent(userId, navController, viewModel)
                1 -> WishlistScreen(navController, viewModel, userId)
                2 -> ProfileScreen(navController)
                3 -> SettingScreen(navController)
            }
        }


    }
}

@Composable
fun HomeContent(
    userId: String,
    navController: NavController,
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val categoryList by viewModel.category.collectAsState(initial = emptyList())
    val doctorList by viewModel.doctors.collectAsState(initial = emptyList())


    LaunchedEffect(true) {
        viewModel.loadCategory()
        viewModel.loadDoctors()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)

    ) {
        // Top Greeting
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 48.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Hi, Deepak Kumar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text("How Are You Today?", color = Color.Black)
            }
            Image(
                painter = painterResource(id = R.drawable.bell_icon),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
        }

        // Banner
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text("Searching....", fontStyle = FontStyle.Italic)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp)
        )

        // Doctor Speciality Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Doctor Speciality", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("See all", color = Color(0xFF6A1B9A)) // darkPurple
        }

        // Category List
        if (categoryList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            CategorySection(categoryList) {
                Log.d("Clicked Category", it.Name)
            }
        }

        // Top Doctor Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Top Doctor", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "See all", color = Color(0xFF6A1B9A), modifier = Modifier.clickable {
                navController.navigate("top_doctor_list")
            })
        }

        // Doctor List
        if (doctorList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            TopDoctorSection(
                doctors = doctorList,
                navController = navController
            )
        }

    }

}