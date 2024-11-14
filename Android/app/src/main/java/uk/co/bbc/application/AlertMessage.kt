package uk.co.bbc.application

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.co.bbc.application.ui.theme.ApplicationTheme

@Composable
fun AlertMessage(onHomeClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.error_message)) },
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
fun TvLicenseAlertMessage(onHomeClick: () -> Unit, onConfirmClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.tv_license_message)) },
        confirmButton = { // 6
            Button(
                onClick = onConfirmClick
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

@Preview(showBackground = true)
@Composable
fun AlertMessagePreview() {
    ApplicationTheme {
        AlertMessage { }
    }
}

@Preview(showBackground = true)
@Composable
fun TvLicenseAlertMessagePreview() {
    ApplicationTheme {
        TvLicenseAlertMessage({ }) { }
    }
}