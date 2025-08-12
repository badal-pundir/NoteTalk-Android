package com.example.notetalk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.R
import com.example.notetalk.ui.theme.NoteTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePictureScreen(
    navController: NavController
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val savedProfiledPictureRes by mainViewModel.profilePicture.collectAsState(initial = R.drawable.person_28dp)

    var tempSelectedPicture by remember { mutableIntStateOf(savedProfiledPictureRes) }

    var showDialog by remember { mutableStateOf(false) }

    var enableSaveButton by remember { mutableStateOf(false)}

    if(showDialog) {
        ProfilePictureOptionsDialog(
            onDismissRequest = { showDialog = false },
            onPictureSelected = { it ->
                tempSelectedPicture = it
                profileViewModel.onProfilePictureSelected(it)
                showDialog = false
                enableSaveButton = true
            }
        )

    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Your Account") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                },
                actions = {
                    IconButton(onClick = {  navController.popBackStack()  }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Go Back")
                    }
                }
            )
        },
        bottomBar = {
            ActionButtons(
                onChangeClick = { showDialog = true },
                enableSaveButton,
                onSaveClick = {
                    navController.popBackStack()
                    enableSaveButton = false
                }
            )
        }
    ){  innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = Color.Black
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
//                        .padding(innerPadding)
                        //                    .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter =  painterResource(savedProfiledPictureRes),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = 3.dp,
                                color = Color.Gray,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Profile picture",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your profile picture will display on the home screen",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }

    }
}

@Composable
private fun ActionButtons(
    onChangeClick: () -> Unit,
    isSaveButtonEnabled: Boolean,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = onSaveClick,
            modifier = Modifier.weight(1f),
            enabled = isSaveButtonEnabled
        ) {
            Text("Save")
        }
        OutlinedButton(
            onClick = onChangeClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Change")
        }


    }
}
@Preview
@Composable
fun PreviewEditProfilePicture() {
    NoteTalkTheme {
        ProfilePictureScreen(navController = rememberNavController())
    }
}

