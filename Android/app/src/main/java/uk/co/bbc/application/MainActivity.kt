package uk.co.bbc.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uk.co.bbc.application.ui.theme.ApplicationTheme
import java.time.LocalDateTime


class MainActivity : ComponentActivity() {

    private val uiState = MutableStateFlow<UiState>(UiState.HomePage(showDialog = false))
    private val onBreakingNewsClick = { uiState.value = UiState.HomePage(showDialog = true) }
    private val onHomeClick = { uiState.value = UiState.HomePage(showDialog = false) }
    private val goToClicked = {title: String ->
        if (title == "TV Guide") {
            uiState.value = UiState.HomePage(
                showDialog = false,
                showTvLicenseDialog = true,
                showLoader = false
            )
        } else {
            uiState.value = UiState.ContentPage(showDialog = false)
        }

    }
    private val onRefreshClick: () -> Unit = {
        uiState.value = UiState.HomePage(showDialog = false, showLoader = true)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000L)
            uiState.value = UiState.HomePage(showDialog = false, showLoader = false)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val uiState = uiState.collectAsState()
                    DrawContent(
                        uiState,
                        innerPadding,
                        onHomeClick,
                        onBreakingNewsClick,
                        goToClicked,
                        onRefreshClick
                    )
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
    goToClicked: (String) -> Unit,
    onRefreshClick: () -> Unit
) {
    val currentTopic = rememberSaveable { mutableStateOf("Politics") }
    val itemPosition = rememberSaveable() { mutableStateOf(0) }
    val newUiState = uiState.value
    when (newUiState) {
        is UiState.HomePage -> {
            HomePage(
                modifier = Modifier.padding(innerPadding),
                onBreakingNewsClick,
                goToClicked,
                onDropdownItemClick = { topic,position ->
                    currentTopic.value = topic
                    itemPosition.value = position},
                title = currentTopic.value,
                itemPosition = itemPosition.value,
                onRefreshClick = onRefreshClick
            )
            if (newUiState.showDialog) {
                AlertMessage(onHomeClick)

            }
            if (newUiState.showLoader) {
                LoadingDialog()
            }
            if (newUiState.showTvLicenseDialog) {
                TvLicenseAlertMessage(onHomeClick)
            }
        }
        is UiState.ContentPage -> {
            ContentPage(
                onHomeClick = onHomeClick,
                title = currentTopic.value
            )
        }
    }
}

sealed interface UiState {
    data class HomePage(val showDialog: Boolean, val showTvLicenseDialog: Boolean = false, val showLoader: Boolean = false) : UiState
    data class ContentPage(val showDialog: Boolean): UiState
}




@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onBreakingNewsClick: () -> Unit,
    goToClicked: (String) -> Unit,
    title: String,
    onDropdownItemClick: (String, Int)-> Unit,
    itemPosition: Int,
    onRefreshClick: () -> Unit
    ) {

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
                LoadingButton(onRefreshClick)
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
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text ="Go to $title ",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { goToClicked(title) }
                )
                PickerDropdownMenu(onClick = onDropdownItemClick,itemPosition)
            }

            Spacer(Modifier.padding(bottom = 150.dp))
            Footer(onBreakingNewsClick, modifier = Modifier
                .padding(80.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentPage(modifier: Modifier = Modifier, onHomeClick: () -> Unit, title: String) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp)) {
        Column(    modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            TopAppBar(title = {Text(text = title)}, navigationIcon = {
                IconButton(onClick = { onHomeClick() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(64.dp))
                }
            })
            Spacer(modifier = Modifier.padding(bottom = 30.dp))
            Text(text = title)
            Spacer(modifier = Modifier.padding(bottom = 30.dp))
            Text(text = stringResource(R.string.lorem_Placeholder_Text))
        }
    }
}

@Composable
fun LoadingButton(onRefreshClick: () -> Unit) {
    var isLoading = rememberSaveable { mutableStateOf(false) }

    Box(

    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            tint = Color.Blue,
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .clickable {
                    onRefreshClick()

                }

        )

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
    val currentDate = LocalDateTime.now().toLocalDate()

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
fun PickerDropdownMenu(onClick: (String, Int)-> Unit, itemPosition: Int) {

    val dropdownExpanded = rememberSaveable { mutableStateOf(false) }
    val topics = listOf("Politics", "UK", "Sport", "Technology", "World", "TV Guide")


    Box(
        modifier = Modifier.padding(end = 16.dp)
    ){
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { dropdownExpanded.value = true }
        ) {
            Text(text= topics[itemPosition])
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = dropdownExpanded.value,
            onDismissRequest = { dropdownExpanded.value = false }
        ) {
            topics.forEachIndexed {index, currentCategory ->
                DropdownMenuItem(
                    onClick = {
                        onClick(topics[index],index)
                      dropdownExpanded.value = false
                    },
                    text = { Text(text = currentCategory) }
                )
            }
        }
    }

}



@Composable
fun AlertMessage(onHomeClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.error_message)) },
        //text = { Text(text = "Jetpack Compose Alert Dialog") },
        confirmButton = { // 6
            Button(
                onClick = onHomeClick
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White
                )
            }
        }
    )
}

@Composable
fun TvLicenseAlertMessage(onHomeClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.tv_license_message)) },
        confirmButton = { // 6
            Button(
                onClick = onHomeClick
            ) {
                Text(
                    text = "Yes",
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onHomeClick
            ) {
                Text(
                    text = "No",
                    color = Color.White
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoadingDialog() {

    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
        ) {
            CircularProgressIndicator()
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
        HomePage(
            onBreakingNewsClick = { },
            goToClicked = {},
            title = "Politics",
            onDropdownItemClick = { _, _ -> },
            itemPosition = 1,
            onRefreshClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlertMessagePreview() {
    ApplicationTheme {
        AlertMessage {  }
    }
}

@Preview(showBackground = true)
@Composable
fun TvLicenseAlertMessagePreview() {
    ApplicationTheme {
        TvLicenseAlertMessage {  }
    }
}