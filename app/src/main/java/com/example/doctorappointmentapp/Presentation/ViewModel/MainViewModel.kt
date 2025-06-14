package com.example.doctorappointmentapp.Presentation.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.Data.CategoryModel
import com.example.doctorappointmentapp.Data.DoctorsModel
import com.example.doctorappointmentapp.Data.WishlistedDoctorModel
import com.example.doctorappointmentapp.Data.toDoctorsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _category = MutableStateFlow<List<CategoryModel>>(emptyList())
    val category: StateFlow<List<CategoryModel>> = _category.asStateFlow()

    private val _doctors = MutableStateFlow<List<DoctorsModel>>(emptyList())
    val doctors: StateFlow<List<DoctorsModel>> = _doctors.asStateFlow()

    private val _wishlist = MutableStateFlow<List<DoctorsModel>>(emptyList())
    val wishlist: StateFlow<List<DoctorsModel>> = _wishlist.asStateFlow()

    var wishlistedDoctorIds by mutableStateOf(setOf<Int>())
        private set

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

    fun loadWishlist(userId: String) {
        viewModelScope.launch {
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
                    wishlistedDoctorIds = idSet
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error loading wishlist: ${error.message}")
                }
            })
        }
    }

    fun addToWishlist(doctor: DoctorsModel, userId: String) {
        val ref = firebaseDatabase.getReference("wishlists").child(userId).child(doctor.Id.toString())
        ref.setValue(doctor)
            .addOnSuccessListener {
                wishlistedDoctorIds = wishlistedDoctorIds + doctor.Id
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Failed to add to wishlist: ${e.message}")
            }
    }

    fun removeFromWishlist(doctorId: Int, userId: String) {
        val ref = firebaseDatabase.getReference("wishlists").child(userId).child(doctorId.toString())
        ref.removeValue()
            .addOnSuccessListener {
                wishlistedDoctorIds = wishlistedDoctorIds - doctorId
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Failed to remove from wishlist: ${e.message}")
            }
    }

    fun toggleWishlist(doctor: DoctorsModel, userId: String) {
        if (wishlistedDoctorIds.contains(doctor.Id)) {
            removeFromWishlist(doctor.Id, userId)
        } else {
            addToWishlist(doctor, userId)
        }
    }
}
