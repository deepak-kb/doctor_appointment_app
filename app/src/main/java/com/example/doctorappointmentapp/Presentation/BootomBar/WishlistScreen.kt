package com.example.doctorappointmentapp.Presentation.BootomBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.Presentation.Component.DoctorItem
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    navController: NavController,
    viewModel: MainViewModel,
    userId: String
) {
    LaunchedEffect(Unit) {
        viewModel.loadWishlist(userId)
    }

    val wishlistedFromServer by viewModel.wishlist.collectAsState(initial = emptyList())
    val wishlistedIds = remember(wishlistedFromServer) { wishlistedFromServer.map { it.Id } }


    if (wishlistedFromServer.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("You have no doctors in your wishlist.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items<DoctorsModel>(wishlistedFromServer) { doctor ->
                DoctorItem(
                    doctor = doctor,
                    userId = userId,
                    wishlistedIds = wishlistedIds,
                    onToggleWishlist = { clickedDoctor ->
                        viewModel.removeFromWishlist(clickedDoctor.Id, userId)
                        viewModel.loadWishlist(userId)
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

