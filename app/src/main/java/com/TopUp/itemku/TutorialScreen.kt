@file:OptIn(ExperimentalFoundationApi::class)
package com.TopUp.itemku

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TutorialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val hasSeenTutorial = sharedPreferences.getBoolean("HasSeenTutorial", false)

        if (hasSeenTutorial) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContent {
                TutorialScreen(sharedPreferences)
            }
        }
    }
}

@Composable
fun TutorialScreen(sharedPreferences: SharedPreferences) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            coroutineScope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    val tutorialItems = listOf(
        TutorialPage(
            title = "Selamat Datang di Aplikasi Top Up Kami",
            description = "Aplikasi ini menyediakan Kebutuhan Gaming Anda.",
            imageRes = R.drawable.logo1
        ),
        TutorialPage(
            title = "Temukan Segala Kebutuhan Gaming Anda",
            description = "Cari dan jelajahi berbagai jenis akun dan TopUp di aplikasi kami!.",
            imageRes = R.drawable.logo2
        ),
        TutorialPage(
            title = "Ada Berbagai Macam Metode Pembayaran",
            description = "Memudahkan anda memilih Pembayaran Yang Anda Sukai.",
            imageRes = R.drawable.logo3
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            TutorialItem(
                tutorialItems[page].title,
                tutorialItems[page].description,
                tutorialItems[page].imageRes
            )
        }

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .background(color)
                        .size(8.dp)
                )
            }
        }

        Button(
            onClick = {
                sharedPreferences.edit().putBoolean("HasSeenTutorial", true).apply()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Mulai")
        }
    }
}

@Composable
fun TutorialItem(title: String, description: String, imageRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

data class TutorialPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

