// LearnScreen.kt
package com.example.learningapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningapp.components.ButtonComponent

@Composable
fun WriteScreen(navController: NavController) {
    var selectedLetter by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Select a Letter",
            style = TextStyle(fontSize = 24.sp, color = Color.Black),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Display Letters (A-Z) in a dynamic horizontal row
        val letters = ('A'..'Z').toList() // List of letters from A to Z
        var rowCount by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Dynamically arrange the letters horizontally
            letters.chunked(5).forEach { rowLetters ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowLetters.forEach { letter ->
                        Button(
                            onClick = {
                                selectedLetter = letter.toString()
                                navController.navigate("write$letter")
                            },
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Text(text = letter.toString(), style = TextStyle(fontSize = 18.sp))
                        }
                    }
                }
                rowCount++
            }
        }

        // Display selected letter
        selectedLetter?.let {
            Text(text = "Selected Letter: $it", style = TextStyle(fontSize = 18.sp, color = Color.Black))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // "Write" Button
        ButtonComponent("Learn", navController, "learnMenu")
    }
}
