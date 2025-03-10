package com.example.learningapp.ui.learn

import android.graphics.ImageDecoder
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.size.Size
import com.example.learningapp.R
import com.example.learningapp.fetchWordDetails
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

@Composable
fun LearnAScreen(navController: NavController) {
    val wordsList = listOf("apple", "ant", "art")
    var words by remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            words = fetchWordDetails(wordsList)
            println("Fetched words: $words")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display GIF at the top
        item {
            GifImage()
        }

        items(words) { (word, image) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Hiển thị từ
                Text(text = word)

                LoadImageWithPicasso(image)
            }
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.letter_a_learn).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun LoadImageWithPicasso(url: String?) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                Picasso.get()
                    .load(url)
                    .placeholder(android.R.drawable.ic_menu_report_image) // Ảnh tạm khi tải
                    .error(android.R.drawable.ic_delete) // Ảnh lỗi nếu URL không hợp lệ
                    .into(this)
            }
        },
        modifier = Modifier.size(100.dp)
    )
}
