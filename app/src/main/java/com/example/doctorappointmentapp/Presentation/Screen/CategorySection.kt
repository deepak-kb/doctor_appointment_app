package com.example.doctorappointmentapp.Presentation.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.doctorappointmentapp.Data.CategoryModel
import com.example.doctorappointmentapp.R

@Composable
fun CategorySection(
    categoryList: List<CategoryModel>,
    onItemClick: (CategoryModel) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categoryList) { item ->
            CategoryItem(item = item, onClick = onItemClick)
        }
    }
}

@Composable
fun CategoryItem(
    item: CategoryModel,
    onClick: (CategoryModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .width(70.dp)
            .clickable { onClick(item) }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = colorResource(id = R.color.lightPurple)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.Picture,
                contentDescription = item.Name,
//                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.Name,
            color = colorResource(id = R.color.purple),
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}