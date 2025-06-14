package com.example.doctorappointmentapp.Presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.doctorappointmentapp.Presentation.Navigation.AppNavigation
import com.example.doctorappointmentapp.Presentation.Screen.HomeScreen
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.example.doctorappointmentapp.ui.theme.DoctorAppointmentAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            DoctorAppointmentAppTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

