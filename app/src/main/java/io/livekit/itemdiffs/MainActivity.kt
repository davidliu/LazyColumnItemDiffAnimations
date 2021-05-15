package io.livekit.itemdiffs

import android.os.Bundle
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
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

    Box(
        Modifier
            .background(Color(0xFFEDEAE0))
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val animationState = updateAnimatedItemsState(targetState = uuids.value)
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            animatedItemsIndexed(animationState, key = { item: String -> item }) { _, item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(
                            align = Alignment.CenterVertically
                        ),
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
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityLazyColumnDemo() {
    var itemNum by remember { mutableStateOf(0) }
    Column {
        Row(Modifier.fillMaxWidth()) {
            Button(
                { itemNum += 1 },
                enabled = itemNum <= turquoiseColors.size - 1,
                modifier = Modifier
                    .padding(15.dp)
                    .weight(1f)
            ) {
                Text("Add")
            }

            Button(
                { itemNum -= 1 },
                enabled = itemNum >= 1,
                modifier = Modifier
                    .padding(15.dp)
                    .weight(1f)
            ) {
                Text("Remove")
            }
        }
        LazyColumn {
            itemsIndexed(turquoiseColors) { i, color ->
                AnimatedVisibility(
                    (turquoiseColors.size - itemNum) <= i,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .requiredHeight(90.dp)
                            .background(color)
                    )
                }
            }
        }

        Button(
            { itemNum = 0 },
            modifier = Modifier
                .align(Alignment.End)
                .padding(15.dp)
        ) {
            Text("Clear All")
        }
    }
}

private val turquoiseColors = listOf(
    Color(0xff07688C),
    Color(0xff1986AF),
    Color(0xff50B6CD),
    Color(0xffBCF8FF),
    Color(0xff8AEAE9),
    Color(0xff46CECA)
)
