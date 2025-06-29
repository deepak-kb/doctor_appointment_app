package com.example.doctorappointmentapp.Presentation.ViewModel

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.doctorappointmentapp.Data.*
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // ---------- USER DATA ----------
    private val _userData = MutableStateFlow(UserModel())
    val userData: StateFlow<UserModel> = _userData.asStateFlow()


    var currentUserId by mutableStateOf("")
        private set

    fun setUserId(userId: String) {
        currentUserId = userId
    }

    // Load user data from Firestore
    fun loadUserDetails(userId: String) {
        if (userId.isBlank()) {
            Log.d("FirestoreError", "Invalid userId: Cannot be blank")
            return
        }

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserModel::class.java)
                    user?.let {
                        _userData.value = it
                    }
                }
            }
            .addOnFailureListener {
                Log.e("FirestoreError", "Failed to load user data: ${it.message}")
            }
    }


    // Update user profile to Firestore with Base64 image
    fun updateUserProfile(
        userId: String,
        user: UserModel,
        profileBitmap: Bitmap?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val updatedUser = if (profileBitmap != null) {
            val base64Image = encodeBitmapToBase64(profileBitmap)
            user.copy(profileImageUrl = base64Image)
        } else {
            user
        }

        saveUserToFirestore(userId, updatedUser, onSuccess, onFailure)
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveUserToFirestore(
        userId: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                _userData.value = user
                onSuccess()
            }
            .addOnFailureListener {
                onFailure("Update failed: ${it.message}")
            }
    }

    // ---------- CATEGORY DATA ----------
    private val _category = MutableStateFlow<List<CategoryModel>>(emptyList())
    val category: StateFlow<List<CategoryModel>> = _category.asStateFlow()

    fun loadCategory() {
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(CategoryModel::class.java)?.let {
                        list.add(it)
                    }
                }
                _category.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error loading categories: ${error.message}")
            }
        })
    }

    // ---------- DOCTOR DATA ----------
    private val _doctors = MutableStateFlow<List<DoctorsModel>>(emptyList())
    val doctors: StateFlow<List<DoctorsModel>> = _doctors.asStateFlow()

    fun loadDoctors() {
        val ref = firebaseDatabase.getReference("Doctors")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<DoctorsModel>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(DoctorsModel::class.java)?.let {
                        list.add(it)
                    }
                }
                _doctors.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error loading doctors: ${error.message}")
            }
        })
    }

    // ---------- WISHLIST DATA ----------
    // Wishlist list of doctors
    private val _wishlist = MutableStateFlow<List<DoctorsModel>>(emptyList())
    val wishlist: StateFlow<List<DoctorsModel>> = _wishlist.asStateFlow()

    // Wishlist doctor IDs
    private val _wishlistedDoctorIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistedDoctorIds: StateFlow<Set<Int>> = _wishlistedDoctorIds.asStateFlow()


    fun loadWishlist(userId: String) {
        if (userId.isBlank()) {
            Log.e("FirebaseError", "Skipped loading wishlist: userId is blank")
            return
        }

        val ref = firebaseDatabase.getReference("wishlists").child(userId)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<DoctorsModel>()
                val idSet = mutableSetOf<Int>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(WishlistedDoctorModel::class.java)?.let { wishlistedDoctor ->
                        val model = wishlistedDoctor.toDoctorsModel()
                        list.add(model)
                        idSet.add(model.Id)
                    }
                }
                _wishlist.value = list
                _wishlistedDoctorIds.value = idSet
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error loading wishlist: ${error.message}")
            }
        })
    }


    fun addToWishlist(doctor: DoctorsModel, userId: String) {
        if (userId.isBlank()) {
            Log.e("FirebaseError", "Skipped adding to wishlist: userId is blank")
            return
        }

        val ref = firebaseDatabase.getReference("wishlists").child(userId).child(doctor.Id.toString())
        ref.setValue(doctor)
            .addOnSuccessListener {
                _wishlistedDoctorIds.value = _wishlistedDoctorIds.value + doctor.Id
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Failed to add to wishlist: ${e.message}")
            }
    }


    fun removeFromWishlist(doctorId: Int, userId: String) {
        if (userId.isBlank()) {
            Log.e("FirebaseError", "Skipped removing from wishlist: userId is blank")
            return
        }

        val ref = firebaseDatabase.getReference("wishlists").child(userId).child(doctorId.toString())
        ref.removeValue()
            .addOnSuccessListener {
                _wishlistedDoctorIds.value = _wishlistedDoctorIds.value - doctorId
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Failed to remove from wishlist: ${e.message}")
            }
    }


    fun toggleWishlist(doctor: DoctorsModel, userId: String) {
        if (_wishlistedDoctorIds.value.contains(doctor.Id)) {
            removeFromWishlist(doctor.Id, userId)
        } else {
            addToWishlist(doctor, userId)
        }
    }

}
