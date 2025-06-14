package com.example.doctorappointmentapp.Presentation.AuthScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctorappointmentapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Signup", fontSize = 30.sp,
            color = colorResource(id = R.color.purple),
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number") })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (firstName!=""|| lastName!=""|| mobile!=""|| address!=""|| email!=""|| password!="") {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val uid = it.user?.uid ?: ""
                            val user = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "mobile" to mobile,
                                "address" to address,
                                "email" to email
                            )
                            db.collection("users").document(uid).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login")
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Signup Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),

            ) {
            Text("Signup", fontSize = 20.sp)
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Login")
        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun SignupScreenPreview() {
//    val navControllerFake = rememberNavController()
//    SignupScreen(navControllerFake)
//}

