package com.example.doctorappointmentapp.Presentation.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.R

@Composable
fun DoctorItem(
    doctor: DoctorsModel,
    userId: String,
    wishlistedIds: List<Int>,                            // IDs currently wishlisted
    onToggleWishlist: (DoctorsModel) -> Unit,             // pass the clicked doctor
    onClickMakeAppointment: (DoctorsModel) -> Unit        // now accepts a DoctorsModel
) {
    val isWishlisted = remember(wishlistedIds) { wishlistedIds.contains(doctor.Id) }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                // --- Doctor Image ---
                Image(
                    painter = rememberAsyncImagePainter(doctor.Picture),
                    contentDescription = "Doctor Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0BBE4)) // light purple background
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // “Professional Doctor” tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFE0BBE4), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tick),
                            contentDescription = null,
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Professional Doctor",
                            fontSize = 13.sp,
                            color = Color(0xFF6A1B9A),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Name
                    Text(
                        text = doctor.Name,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Specialization
                    Text(
                        text = doctor.Special,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Rating (stars)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(doctor.Rating.toInt()) {
                            Icon(
                                imageVector = Icons.Default.Favorite, // use a star icon if you have one
                                contentDescription = null,
                                tint = Color(0xFFFFC107), // amber
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doctor.Rating.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // --- Wishlist Heart Icon ---
                IconButton(
                    onClick = { onToggleWishlist(doctor) },
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 4.dp)
                ) {
                    if (isWishlisted) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Remove from Wishlist",
                            tint = Color.Red
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Add to Wishlist",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // --- “Make Appointment” Button ---
            Button(
                onClick = { onClickMakeAppointment(doctor) },  // Pass doctor
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Make Appointment", color = Color(0xFF6A1B9A), fontSize = 16.sp)
            }
        }
    }
}
