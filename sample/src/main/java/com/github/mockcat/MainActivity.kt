package com.github.mockcat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.mockcat.core.api.Mockcat
import com.github.mockcat.data.User
import com.github.mockcat.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                UserScreen(viewModel)
            }
        }
    }
}

@Composable
fun UserScreen(mainViewModel: MainViewModel) {
    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column {
                Button(onClick = {
                    Mockcat.createShortcut(context)
                }) {
                    Text(text = "Create Mockcat Shortcut")
                }

                Button(onClick = {
                    val intent = Mockcat.getLaunchIntent(context)
                    context.startActivity(intent)
                }) {
                    Text(text = "Launch Mockcat")
                }
            }
        },
        bottomBar = {
            Button(
                onClick = mainViewModel::fetchUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                enabled = uiState !is UserUiState.Loading
            ) {
                Text(text = "Fetch User Data", fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Use a 'when' block to display the correct UI for the current state
            when (val currentState = uiState) {
                is UserUiState.Idle -> {
                    Text("Click the button to fetch a user.", fontSize = 18.sp)
                }
                is UserUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UserUiState.Success -> {
                    UserDetails(user = currentState.user)
                }
                is UserUiState.Error -> {
                    Text(
                        "Error: ${currentState.message}",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetails(user: User) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = user.avatar,
            contentDescription = "User Avatar",
            modifier =
            Modifier
                .size(128.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${user.firstName} ${user.lastName}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user.email,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
