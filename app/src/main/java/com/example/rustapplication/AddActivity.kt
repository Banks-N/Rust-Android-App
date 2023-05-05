package com.example.rustapplication

//import com.example.rustapplication.lib.Inputs
import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rustapplication.lib.RustLog
import com.example.rustapplication.ui.theme.RustApplicationTheme
import java.util.*


class AddActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("rust_lib")
            RustLog.initialiseLogging()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RustApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(14.dp))
                            Row {
                                Spacer(modifier = Modifier.size(140.dp))

                                Text("Exercise Tracker")

                                Spacer(modifier = Modifier.size(14.dp))
                            }

                            Spacer(modifier = Modifier.size(14.dp))
                            Row {
                                Spacer(modifier = Modifier.size(140.dp))
                                Button(onClick = {
                                    val intent = Intent(this@AddActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Text(text = "Add Entry")
                                }

                                Spacer(modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}
