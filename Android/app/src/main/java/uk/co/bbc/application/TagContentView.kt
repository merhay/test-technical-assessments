package uk.co.bbc.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import uk.co.bbc.application.ui.theme.ApplicationTheme




@Composable
fun ContentView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(  modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
          Text("Placeholder")
            Spacer(modifier = Modifier.padding(bottom = 30.dp))
            Text(text= stringResource(R.string.lorem_Placeholder_Text))
        }
    }


}





@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    ApplicationTheme {
        ContentView()
    }
}