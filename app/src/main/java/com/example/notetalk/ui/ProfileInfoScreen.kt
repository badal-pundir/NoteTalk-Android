package com.example.notetalk.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.R
import com.example.notetalk.data.PreviewNoteRepository
import com.example.notetalk.ui.theme.NoteTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Profile")},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to home screen")
                    }

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ProfileHeader(

            )
            Spacer(modifier = Modifier.height(16.dp))

            StatCard(noteCount = notes.size)
            Spacer(modifier = Modifier.height(24.dp))

            ProfileOption(
                icon = Icons.Default.Settings,
                text = "Settings",
                onClick = { navController.navigate("settings") }
            )
//            Divider()
            ProfileOption(
                icon = Icons.Default.Star,
                text = "Star",
                onClick = {  }
            )
//            Divider()
            ProfileOption(
                icon = Icons.Default.Info,
                text = "Info",
                onClick = {  }
            )
            ProfileOption(
                icon = Icons.Default.Info,
                text = "Edit Profile",
                onClick = { navController.navigate("EditProfilePicture") }
            )
        }

    }
}

@Composable
private fun ProfileHeader() {
    val mainViewModel: MainViewModel = hiltViewModel()
//    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profiledPictureRes by mainViewModel.profilePicture.collectAsState(initial = R.drawable.person_28dp)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        /*Image(
            painter = painterResource(R.drawable.user1),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
            )*/
        ProfileIcon(
            imagePainter = painterResource(profiledPictureRes),
            size = 100.dp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Badal Pundir", // Placeholder name
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "badal.pundir@zyx.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(noteCount: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = noteCount.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Notes Created",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProfileOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go to $text"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun StatCardPreview() {
    NoteTalkTheme {
        StatCard(6)
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    NoteTalkTheme {
        ProfileHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileOptionPreview() {
    NoteTalkTheme {
        ProfileOption(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = {}
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NoteTalkTheme {
        val fakeRepo = remember { PreviewNoteRepository() } // fake repo for preview
        val viewModel = remember { NoteViewModel(fakeRepo) }
        val navController = rememberNavController()

        ProfileScreen(navController, viewModel)
    }
}