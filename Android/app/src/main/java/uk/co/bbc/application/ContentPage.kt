package uk.co.bbc.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.co.bbc.application.ui.theme.ApplicationTheme

const val TEST_TAG_BACK_BUTTON = "back button"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentPage(onHomeClick: () -> Unit, title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(modifier = Modifier.testTag(TEST_TAG_BACK_BUTTON), onClick = { onHomeClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            })
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.padding(bottom = 30.dp))
                Text(text = title)
                Spacer(modifier = Modifier.padding(bottom = 30.dp))
                Text(text = stringResource(R.string.lorem_Placeholder_Text))
                Spacer(modifier = Modifier.padding(bottom = 30.dp))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPagePreview() {
    ApplicationTheme {
        ContentPage(
            onHomeClick = { },
            title = "Politics"
        )
    }
}