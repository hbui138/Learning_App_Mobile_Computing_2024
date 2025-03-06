package com.example.learningapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ButtonComponent(
    label: String,
    navController: NavController,
    destination: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { navController.navigate(destination) }, // Use navController for navigation
        modifier = modifier
            .width(200.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, style = TextStyle(fontSize = 18.sp))
    }
}