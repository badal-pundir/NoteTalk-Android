package com.example.notetalk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notetalk.R
import com.example.notetalk.ui.theme.NoteTalkTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilePictureOptionsDialog(
    onDismissRequest: () -> Unit,
    onPictureSelected: (Int) -> Unit = {}, // on Save
) {

    val listOfProfilePictures = listOf(
        R.drawable.person_28dp,
        R.drawable.luffy1,
        R.drawable.oh_yah,
        R.drawable.no_emotion,
        R.drawable.og_shinchan,
        R.drawable.spiderman,
        R.drawable.the_og_panda,
        R.drawable.the_wierd_one,
        R.drawable.user1
    )

    val mainViewModel: MainViewModel = hiltViewModel()
//    val profileViewModel: ProfileViewModel = hiltViewModel()
    val lastSavedProfiledPictureRes by mainViewModel.profilePicture.collectAsState(initial = R.drawable.person_28dp)

    var selectedImageRes by remember (lastSavedProfiledPictureRes) {
        mutableIntStateOf(lastSavedProfiledPictureRes)
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(selectedImageRes),
                    contentDescription = "Avatar Option $selectedImageRes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose an Avatar",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                FlowRow(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    maxItemsInEachRow = 3
                ) {
                    listOfProfilePictures.forEach { imageRes ->
                        val isSelected = selectedImageRes == imageRes
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = "Avatar Option $imageRes",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .clickable{ selectedImageRes = imageRes}
                                .border(
                                    width = if(isSelected) 3.dp else 0.dp,
                                    color = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            onPictureSelected(selectedImageRes)
                            onDismissRequest()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = true // enable only if an image is selected
                    ) {
                        Text("Save")
                    }
                }
            }


        }
    }
}

@Preview
@Composable
fun PreviewProfilePictureOptions() {
    NoteTalkTheme {
        ProfilePictureOptionsDialog ({},{})
    }
}