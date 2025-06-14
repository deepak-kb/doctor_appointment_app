package com.example.doctorappointmentapp.Presentation.Screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.Presentation.ViewModel.MainViewModel
import com.example.doctorappointmentapp.R

@Composable
fun DetailScreen(
    doctor: DoctorsModel,
    viewModel: MainViewModel,
    userId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val wishlistedDoctorIds by viewModel.wishlist.collectAsState(initial = emptyList())
    val isWishlisted = remember(wishlistedDoctorIds) {
        wishlistedDoctorIds.any { it.Id == doctor.Id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colorResource(id = R.color.purple))

    ) {
        Box(
            modifier = Modifier
                .padding(top = 55.dp)
                .fillMaxWidth()
                .height(320.dp)
                .background(colorResource(id = R.color.purple))
                .weight(.4f)
        ) {
            AsyncImage(
                model = doctor.Picture,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )

            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_white),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = {
                    viewModel.toggleWishlist(doctor, userId)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isWishlisted) R.drawable.fav_bold else R.drawable.favorite_white
                    ),
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
                .weight(.6f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(doctor.Name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(doctor.Special, fontSize = 16.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.location), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(doctor.Address, color = colorResource(id = R.color.purple))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                InfoBox(title = "Patients", value = doctor.Patiens)
                InfoBox(title = "Experience", value = "${doctor.Expriense} Years")
                InfoBox(title = "Rating", value = "⭐ ${doctor.Rating}")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Biography", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(doctor.Biography, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionIcon(R.drawable.website, "Website") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(doctor.Site))
                    context.startActivity(intent)
                }
                ActionIcon(R.drawable.message, "Message") {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${doctor.Mobile}"))
                    intent.putExtra("sms_body", "the SMS txt")
                    context.startActivity(intent)
                }
                ActionIcon(R.drawable.call, "Call") {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${doctor.Mobile}"))
                    context.startActivity(intent)
                }
                ActionIcon(R.drawable.direction, "Direction") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(doctor.Location))
                    context.startActivity(intent)
                }
                ActionIcon(R.drawable.share, "Share") {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, doctor.Name)
                    intent.putExtra(Intent.EXTRA_TEXT, "${doctor.Name} ${doctor.Address} ${doctor.Mobile}")
                    context.startActivity(Intent.createChooser(intent, "Choose one"))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Appointment Booked", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple))
            ) {
                Text("Make Appointment")
            }
        }
    }
}

@Composable
fun InfoBox(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 14.sp, color = Color.Black)
        Text(value, fontSize = 16.sp, color = colorResource(id = R.color.purple), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActionIcon(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.lightPurple))
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = colorResource(id = R.color.purple)
            )
        }
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
