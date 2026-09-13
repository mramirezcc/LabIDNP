package com.example.booksaver

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booksaver.ui.theme.BookSaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookSaverTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookSaverApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookSaverApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var pages by remember { mutableStateOf("") }
    var savedRecord by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título del libro") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = pages,
            onValueChange = { pages = it },
            label = { Text("Número de páginas leídas") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val filename = "current_book.txt"
                val fileContents = "Título: $title\nAutor: $author\nPáginas: $pages\n"
                try {
                    context.openFileOutput(filename, Context.MODE_PRIVATE).use { output ->
                        output.write(fileContents.toByteArray())
                    }
                    Log.d("BookSaver", "Datos guardados correctamente.")
                } catch (e: Exception) {
                    Log.e("BookSaver", "Error al guardar datos", e)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Button(
            onClick = {
                val filename = "current_book.txt"
                try {
                    context.openFileInput(filename).bufferedReader().use { reader ->
                        val content = reader.readText()
                        Log.d("BookSaver", "Contenido del archivo:\n$content")
                        savedRecord = content
                    }
                } catch (e: Exception) {
                    Log.e("BookSaver", "Error al leer datos o archivo no existe", e)
                    savedRecord = "Error al leer datos o archivo no existe."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver registro")
        }

        if (savedRecord.isNotEmpty()) {
            Text(
                text = savedRecord,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookSaverAppPreview() {
    BookSaverTheme {
        BookSaverApp()
    }
}