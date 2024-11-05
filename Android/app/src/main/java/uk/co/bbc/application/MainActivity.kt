package uk.co.bbc.application

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import uk.co.bbc.application.ui.theme.ApplicationTheme
import java.time.LocalDateTime
import java.util.Timer

class MainActivity : ComponentActivity() {

    private val uiState = MutableStateFlow<UiState>(UiState.HomePage(showDialog = false))
    private val onBreakingNewsClick = { uiState.value = UiState.HomePage(showDialog = true) }
    private val onHomeClick = { uiState.value = UiState.HomePage(showDialog = false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val uiState = uiState.collectAsState()
//                    val navController = rememberNavController()
//                    NavHost(
//                        navController = navController,
//                        startDestination =
//                    ) { }
                    DrawContent(uiState, innerPadding, onHomeClick, onBreakingNewsClick)
                }
            }
        }
    }
}

@Composable
fun DrawContent(
    uiState: State<UiState>,
    innerPadding: PaddingValues,
    onHomeClick: () -> Unit,
    onBreakingNewsClick: () -> Unit,
) {
    val newUiState = uiState.value
    when (newUiState) {
        is UiState.HomePage -> {
            HomePage(
                modifier = Modifier.padding(innerPadding),
                onBreakingNewsClick
            )
            if (newUiState.showDialog) {
                AlertMessage(onHomeClick)
            }
        }
    }
}

sealed interface UiState {
    data class HomePage(val showDialog: Boolean) : UiState
}


@Composable
fun HomePage(modifier: Modifier = Modifier, onBreakingNewsClick: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.bbc_logo),
                contentDescription = "BBC Logo",
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            Spacer(Modifier.padding(bottom = 50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.page_title),
                    fontSize = 35.sp,
                    modifier = Modifier.padding(horizontal = 30.dp)
                )
                LoadingButton()
            }
            Image(
                painter = painterResource(R.mipmap.bbc_broadcasting_house_foreground),
                contentDescription = "BBC Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(350.dp)
            )
            Subheading()
            Spacer(Modifier.padding(bottom = 15.dp))
            PickerDropdownMenu()
            Spacer(Modifier.padding(bottom = 150.dp))
            Footer(onBreakingNewsClick, modifier = Modifier
                .padding(80.dp)

            )
        }
    }
}

@Composable
fun LoadingButton() {
    var isLoading = rememberSaveable { mutableStateOf(false) }

    Box(

    ) {
        IconButton(

            colors = IconButtonDefaults
                .iconButtonColors(Color.Blue),
            onClick = {
                isLoading.value = true
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000L)
                    isLoading.value = false
                }
            }
        )

        {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .background(color = Color.Blue)

            )
        }
        if(isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(color = Color.Yellow)

            )
        }
    }

}


@Composable
fun Subheading(modifier: Modifier = Modifier) {
    val currentDate = LocalDateTime.now()

    Text(
        text = "Last updated: $currentDate",
        modifier = Modifier.padding(bottom = 10.dp)
    )
    Text(
        text = stringResource(R.string.subtitle_text),
        modifier = Modifier.padding(horizontal = 45.dp)
    )
}

@Composable
fun PickerDropdownMenu() {
    val dropdownExpanded = rememberSaveable() { mutableStateOf(false) }
    val itemPosition = rememberSaveable() { mutableStateOf(0) }
    val topics = listOf("Politics", "UK", "Sport", "Technology", "World", "TV Guide")
    val currentTopic =  topics.get(itemPosition.value)

    Box(modifier = Modifier.fillMaxWidth()){
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { dropdownExpanded.value = true }
        ) {
            Text(text ="Go to $currentTopic",
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .clickable {  }
            )
            Text(text= topics.get(itemPosition.value))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = dropdownExpanded.value,
            onDismissRequest = { dropdownExpanded.value = false }
        ) {
            topics.forEachIndexed() {index, currentCategory ->
                DropdownMenuItem(
                    onClick = {
                      dropdownExpanded.value = false
                      itemPosition.value = index
                    },
                    text = { Text(text = currentCategory) }
                )
            }
        }
    }

}



@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
fun AlertMessage(onHomeClick: () -> Unit) {

    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .background(color = Color.Yellow)
        ) {
            Text(text = stringResource(R.string.error_message))
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onHomeClick

            ) { Text("Close") }
        }
    }
}

@Composable
fun Footer(onBreakingNewsClick: () -> Unit, modifier: Modifier) {

    Button(
        onClick = onBreakingNewsClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(text = stringResource(R.string.breaking_news))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ApplicationTheme {
        HomePage(onBreakingNewsClick = { })
    }
}