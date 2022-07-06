package io.lowapple.app.android.petking.ui.components.petking.item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.lowapple.app.android.petking.ui.theme.AppTheme

enum class PetKingState {
    START, STOP
}

@Composable
fun PetKingButton(state: PetKingState) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = if (state == PetKingState.START) "시작하기" else "그만하기")
    }
}

@Composable
@Preview
fun PetKingButtonPreview() {
    AppTheme {
        Column {
            PetKingButton(state = PetKingState.START)
            PetKingButton(state = PetKingState.STOP)
        }
    }
}