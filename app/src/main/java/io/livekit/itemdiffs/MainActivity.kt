package io.livekit.itemdiffs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.livekit.itemdiffs.ui.animatedItemsIndexed
import io.livekit.itemdiffs.ui.theme.ItemDiffsTheme
import io.livekit.itemdiffs.ui.updateAnimatedItemsState
import java.util.*

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemDiffsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContent()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ItemDiffsTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            MainContent()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainContent() {
    val uuids = remember { mutableStateOf(emptyList<String>()) }

    fun add() {
        uuids.value = uuids.value.plus(UUID.randomUUID().toString())
    }

    fun remove(uuid: String) {
        uuids.value = uuids.value.minus(uuid)
    }

    fun removeFirst() {
        if(uuids.value.isEmpty()) {
            return
        }
        uuids.value = uuids.value.subList(1, uuids.value.size)
    }

    Box(
        Modifier
            .background(Color(0xFFEDEAE0))
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val animationState by updateAnimatedItemsState(uuids.value)
        LazyColumn(Modifier.fillMaxSize()) {
            animatedItemsIndexed(animationState, key = { it }) { _, item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(
                            align = Alignment.CenterVertically
                        )
                        .padding(vertical = 4.dp),
                    color = Color.Black,
                    elevation = 4.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = item.take(5).capitalize(),
                            modifier = Modifier.padding(
                                start = 12.dp, top = 12.dp,
                                end = 12.dp, bottom = 4.dp
                            ),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        TextButton(
                            onClick = { remove(item) },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color(0xFF960018),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Remove")
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                add()
            },
            Modifier.align(Alignment.BottomEnd),
            backgroundColor = Color(0xFFFE4164)
        ) { Icon(Icons.Filled.Add, "") }
        FloatingActionButton(
            onClick = {
                removeFirst()
            },
            Modifier.align(Alignment.BottomStart),
            backgroundColor = Color(0xFFFE4164)
        ) { Icon(Icons.Filled.Close, "") }
    }
}
