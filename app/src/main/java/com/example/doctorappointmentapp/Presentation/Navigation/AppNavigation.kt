
package com.example.doctorappointmentapp.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.Presentation.BootomBar.SettingScreen
import com.example.doctorappointmentapp.Presentation.Screen.HomeScreen
import com.example.doctorappointmentapp.Presentation.AuthScreens.LoginScreen
import com.example.doctorappointmentapp.Presentation.AuthScreens.SignupScreen
import com.example.doctorappointmentapp.Presentation.AuthScreens.SplashScreen
import com.example.doctorappointmentapp.Presentation.BootomBar.ProfileScreen
import com.example.doctorappointmentapp.Presentation.Screen.TopDoctorListScreen
import com.example.doctorappointmentapp.Presentation.BootomBar.WishlistScreen
import com.example.doctorappointmentapp.Presentation.Screen.DetailScreen
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()
    val userId: String=mainViewModel.currentUserId


    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController,mainViewModel) }
        composable("login") { LoginScreen(navController,mainViewModel) }
        composable("signup") { SignupScreen(navController) }

        composable("home") { HomeScreen(navController, mainViewModel,userId) }
        composable("detail") {
            val doctor = navController.previousBackStackEntry?.savedStateHandle?.get<DoctorsModel>("doctor")
            doctor?.let {
                DetailScreen(it, mainViewModel,userId,{ navController.popBackStack() } )
            }
        }
        composable("top_doctor_list") { TopDoctorListScreen(navController, mainViewModel, userId) }

        composable("wishlist") { WishlistScreen( navController,mainViewModel,userId) }
        composable("setting") { SettingScreen(navController,mainViewModel,userId) }
        composable("profile") { ProfileScreen(navController,mainViewModel,userId) }
    }
}
