package com.tieniilfilo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun FullScreenImageViewer(
    path: String?,
    onDismiss: () -> Unit,
) {
    if (path.isNullOrBlank() || !File(path).exists()) return

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = File(path),
                contentDescription = "Immagine a schermo intero",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale.coerceIn(0.5f, 5f),
                        scaleY = scale.coerceIn(0.5f, 5f),
                        translationX = offset.x,
                        translationY = offset.y,
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onDismiss() })
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, _ ->
                            if (scale * zoom <= 0.5f) {
                                onDismiss()
                            } else {
                                scale = (scale * zoom).coerceIn(0.5f, 5f)
                                offset = Offset(
                                    x = offset.x + pan.x,
                                    y = offset.y + pan.y,
                                )
                            }
                        }
                    }
                    .padding(16.dp),
                contentScale = ContentScale.Fit,
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Chiudi",
                    tint = Color.White,
                )
            }
        }
    }
}
