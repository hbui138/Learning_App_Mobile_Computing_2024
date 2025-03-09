@file:Suppress("UNCHECKED_CAST")

package com.example.learningapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.learningapp.datastore_viewmodel.SettingsViewModel
import com.example.learningapp.datastore_viewmodel.UserPreferencesRepository
import com.example.learningapp.datastore_viewmodel.dataStore
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.lifecycle.ViewModelProvider
import com.example.learningapp.R
import com.example.learningapp.datastore_viewmodel.ImageStorageHelper

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val imageStorageHelper = remember { ImageStorageHelper(context) }
    val viewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(
                    UserPreferencesRepository(context.dataStore),
                    imageStorageHelper
                ) as T
            }
        }
    )
    val userPreferences by viewModel.userPreferences.collectAsState()

    var username by remember { mutableStateOf(TextFieldValue()) }
    var dob by remember { mutableStateOf(TextFieldValue()) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(userPreferences) {
        username = TextFieldValue(userPreferences.username)
        dob = TextFieldValue(userPreferences.dob)
        profileImageUri = userPreferences.profileImagePath.takeIf { it.isNotEmpty() }
            ?.let { imageStorageHelper.loadImageFromInternalStorage(it) }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.saveProfileImage(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.default_profile),
                    contentDescription = "Default Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Username Field
        TextField(
            value = username,
            onValueChange = { viewModel.updateUsername(it.text) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        // Date of Birth Field
        TextField(
            value = dob,
            onValueChange = {},
            label = { Text("Date of Birth") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable { showDatePicker = true },
            readOnly = true
        )

        if (showDatePicker) {
            DatePickerDialogComponent(
                currentDobString = dob.text,
                onDateSelected = { viewModel.updateDob(it) },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogComponent(
    currentDobString: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val currentDate = try {
        LocalDate.parse(currentDobString, dateFormatter)
    } catch (e: Exception) {
        LocalDate.now()
    }

    val initialMillis = currentDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selectedDate.format(dateFormatter))
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
