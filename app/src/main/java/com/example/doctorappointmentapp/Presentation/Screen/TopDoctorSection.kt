package com.example.doctorappointmentapp.Presentation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.R

@Composable
fun TopDoctorSection(
    doctors: List<DoctorsModel>,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier.padding(8.dp)
    ) {
        items(doctors) { doctor ->
            TopDoctorCard(doctor = doctor) {
                navController.currentBackStackEntry?.savedStateHandle?.set("doctor", doctor)
                navController.navigate("detail")
            }
        }
    }
}

@Composable
fun TopDoctorCard(doctor: DoctorsModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 190.dp, height = 280.dp)
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = doctor.Picture,
                contentDescription = doctor.Name,
                modifier = Modifier
                    .size(width = 155.dp, height = 155.dp)
                    .background(color = colorResource(id = R.color.lightPurple), shape = RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Fit
            )
            Text(text = doctor.Name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = doctor.Special, fontSize = 12.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                Text(modifier = Modifier.padding(start = 6.dp),text = doctor.Rating.toString(), fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = R.drawable.experience), contentDescription = null)
                Text(modifier = Modifier.padding(start = 6.dp),text = "${doctor.Expriense} Year", fontWeight = FontWeight.Bold)
            }
        }
    }
}
