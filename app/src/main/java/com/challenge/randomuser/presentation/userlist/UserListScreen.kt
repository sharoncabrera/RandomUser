package com.challenge.randomuser.presentation.userlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.challenge.domain.model.User


@Composable
fun UserListScreen(
    uiState: UserListUiState,
  //  onLoadMore: () -> Unit,
    onDeleteUser: (User) -> Unit,
    onFilterUsers: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TextField(
            value = "", //TODO sharon - variable de estado
            onValueChange = { onFilterUsers(it) },
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent

            )
        )


        when (uiState) {
            is UserListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UserListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.users) { user ->
                        UserListItem(user = user, onDeleteUser = onDeleteUser)
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }

            is UserListUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: ${uiState.message}", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun UserListItem(user: User, onDeleteUser: (User) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = rememberAsyncImagePainter(user.pictureThumbnail),
            contentDescription = "User Picture",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // user info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "${user.name} ${user.lastName}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Row {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Delete User",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue,
                )
            }

        }

        // delete button
        IconButton(onClick = { onDeleteUser(user) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete User",
                tint = Color.Gray
            )
        }
    }

}
