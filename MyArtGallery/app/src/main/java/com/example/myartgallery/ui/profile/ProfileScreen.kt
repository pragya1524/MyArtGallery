package com.example.myartgallery.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myartgallery.data.ArtPiece
import com.example.myartgallery.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(userId: String) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var user by remember { mutableStateOf<User?>(null) }
    var userArtPieces by remember { mutableStateOf<List<ArtPiece>>(emptyList()) }
    val isCurrentUser = currentUser?.uid == userId

    LaunchedEffect(userId) {
        firestore.collection("users").document(userId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }
            user = snapshot?.toObject(User::class.java)
        }

        firestore.collection("artpieces")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    userArtPieces = snapshot.toObjects(ArtPiece::class.java)
                }
            }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let { profileUser ->
            Text(text = profileUser.username, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${profileUser.followers.size} Followers | ${profileUser.following.size} Following")
            Spacer(modifier = Modifier.height(16.dp))

            if (!isCurrentUser) {
                val isFollowing = profileUser.followers.contains(currentUser?.uid)
                Button(onClick = {
                    currentUser?.uid?.let { currentUserId ->
                        if (isFollowing) {
                            // Unfollow
                            firestore.collection("users").document(userId)
                                .update("followers", FieldValue.arrayRemove(currentUserId))
                            firestore.collection("users").document(currentUserId)
                                .update("following", FieldValue.arrayRemove(userId))
                        } else {
                            // Follow
                            firestore.collection("users").document(userId)
                                .update("followers", FieldValue.arrayUnion(currentUserId))
                            firestore.collection("users").document(currentUserId)
                                .update("following", FieldValue.arrayUnion(userId))
                        }
                    }
                }) {
                    Text(if (isFollowing) "Unfollow" else "Follow")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Art Pieces", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()) {
                items(userArtPieces) { artPiece ->
                    Image(
                        painter = rememberAsyncImagePainter(artPiece.imageUrl),
                        contentDescription = artPiece.description,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}


