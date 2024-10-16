import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel

@Composable
fun FilterPopUp(
    activity: Activity,
    showFilterPopUp: MutableState<Boolean>,
    transactionFetchViewModel: TransactionFetchViewModel,
) {
    // ...

    // ...
    when {
        // ...
        showFilterPopUp.value -> {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = {
                    showFilterPopUp.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .width(300.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(16.dp)),  // Adding shadow and rounded corners
                    shape = RoundedCornerShape(16.dp),  // Rounded corners
                    color = MaterialTheme.colorScheme.surface,  // Use theme color or custom color
                    tonalElevation = 10.dp  // Elevation for depth
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Close button on the top-right corner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { showFilterPopUp.value = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.Gray
                                )
                            }
                        }

                        // Title of the popup
                        Text(
                            text = "Nice Looking Popup",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Message inside the popup
                        Text(
                            text = "This is a beautifully styled popup window with rounded corners, shadows, and a close button.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action button
                        Button(onClick = {
                            transactionFetchViewModel.setAmountFilter(0.0)
                            showFilterPopUp.value = false
                        }) {
                            Text(text = "Got It")
                        }
                    }
                }
            }
        }
    }
}