package com.example.doctorappointmentapp.Presentation.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.Presentation.Component.DoctorItem
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDoctorListScreen(
    navController: NavController,
    viewModel: MainViewModel,
    userId: String
) {
    val doctorList by viewModel.doctors.collectAsState(initial = emptyList())
    val wishlistedDoctorIds by viewModel.wishlistedDoctorIds.collectAsState()

    LaunchedEffect(Unit) {
            viewModel.loadDoctors()
            viewModel.loadWishlist(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Doctors") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (doctorList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No doctors loaded yet. Size = ${doctorList.size}")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(doctorList) { doctor ->
                    DoctorItem(
                        doctor = doctor,
                        userId = userId,
                        wishlistedIds = wishlistedDoctorIds.toList(),
                        onToggleWishlist = { clickedDoctor ->
                            viewModel.toggleWishlist(clickedDoctor, userId)
                        },
                        onClickMakeAppointment = { clickedDoctor ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("doctor", clickedDoctor)
                            navController.navigate("detail")
                        }
                    )
                }
            }
        }
    }
}
