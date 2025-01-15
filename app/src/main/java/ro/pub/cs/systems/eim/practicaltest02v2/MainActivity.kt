package ro.pub.cs.systems.eim.practicaltest02v2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.meaning)

        // when pressing id/searchButton, use fetchWordMeaning on the word from id/search, and display the result in id/meaning
        findViewById<TextView>(R.id.searchButton).setOnClickListener {
            val word = findViewById<TextView>(R.id.search).text.toString()
            fetchWordMeaning(word)
        }

        connectToServer()
    }

    private fun connectToServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket("10.0.2.2", 5000)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                // Read messages in a loop
                while (true) {
                    val message = reader.readLine() ?: break
                    showToast(message)
                }

                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchWordMeaning(word: String) {
        RetrofitInstance.api.getWordMeaning(word).enqueue(object : Callback<List<WordResponse>> {
            override fun onResponse(call: Call<List<WordResponse>>, response: Response<List<WordResponse>>) {
                // Log raw JSON from the response body
                val rawJson = response.body()?.toString()
                Log.d("Raw JSON Response", rawJson ?: "No response body")

                if (response.isSuccessful && response.body() != null) {
                    val wordResponse = response.body()!!.first()
                    val meaning = wordResponse.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
                    resultTextView.text = "'$word' = $meaning"
                } else {
                    resultTextView.text = "No definition found for '$word'"
                }
            }

            override fun onFailure(call: Call<List<WordResponse>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}