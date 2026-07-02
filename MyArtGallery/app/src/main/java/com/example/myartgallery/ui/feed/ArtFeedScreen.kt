package com.example.myartgallery.ui.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myartgallery.data.ArtPiece
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.google.firebase.auth.FirebaseAuth
import com.example.myartgallery.data.Comment
import com.google.firebase.firestore.FieldValue
import com.example.myartgallery.data.User
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.navigation.NavController


@Composable
fun ArtFeedScreen(navController: NavController) {
    var artPieces by remember { mutableStateOf<List<ArtPiece>>(emptyList()) }
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var followingList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)
                followingList = user?.following ?: emptyList()
            }
        }
    }

    LaunchedEffect(followingList) {
        if (followingList.isNotEmpty()) {
            firestore.collection("artpieces")
                .whereIn("userId", followingList)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val fetchedArtPieces = snapshot.toObjects(ArtPiece::class.java)
                        artPieces = fetchedArtPieces
                    }
                }
        } else {
            // If not following anyone, clear the feed or show a message
            artPieces = emptyList()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (artPieces.isEmpty() && followingList.isNotEmpty()) {
            item { Text("No art from people you follow yet.", modifier = Modifier.padding(16.dp)) }
        } else if (followingList.isEmpty() && currentUser != null) {
            item { Text("Follow users to see their art here!", modifier = Modifier.padding(16.dp)) }
        } else if (currentUser == null) {
            item { Text("Please log in to see the art feed.", modifier = Modifier.padding(16.dp)) }
        }
        items(artPieces) { artPiece ->
            ArtPieceCard(artPiece = artPiece, navController = navController)
        }
    }
}

@Composable
fun ArtPieceCard(artPiece: ArtPiece, navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var commentText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(artPiece.imageUrl),
                contentDescription = artPiece.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = artPiece.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = artPiece.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "By: ", style = MaterialTheme.typography.bodySmall)
            Text(text = artPiece.userId, style = MaterialTheme.typography.bodySmall, modifier = Modifier.clickable { navController.navigate("profile/${artPiece.userId}") })
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val isLiked = artPiece.likes.contains(currentUser?.uid)
                IconButton(onClick = {
                    currentUser?.uid?.let { userId ->
                        val updatedLikes = if (isLiked) {
                            artPiece.likes.toMutableList().apply { remove(userId) }
                        } else {
                            artPiece.likes.toMutableList().apply { add(userId) }
                        }
                        firestore.collection("artpieces").document(artPiece.id)
                            .update("likes", updatedLikes)
                            .addOnFailureListener { e ->
                                // Handle error
                            }
                    }
                }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(text = "${artPiece.likes.size} Likes")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Comments", style = MaterialTheme.typography.titleMedium)
            artPiece.comments.forEach { comment ->
                Text(text = "${comment.userId}: ${comment.text}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Add a comment") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (commentText.isNotBlank()) {
                        currentUser?.uid?.let { userId ->
                            val newComment = Comment(userId = userId, text = commentText)
                            firestore.collection("artpieces").document(artPiece.id)
                                .update("comments", FieldValue.arrayUnion(newComment))
                                .addOnSuccessListener {
                                    commentText = "" // Clear the input field
                                }
                                .addOnFailureListener { e ->
                                    // Handle error
                                }
                        }
                    }
                }) {
                    Text("Post")
                }
            }
        }
    }
}
