package io.lowapple.app.android.petking.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OAuthButton(
    @DrawableRes id: Int,
    title: String,
    titleColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = backgroundColor, // MaterialTheme.colors.primary
        contentColor = backgroundColor, // MaterialTheme.colors.onPrimary,
        shape = RoundedCornerShape(16.dp), border = BorderStroke(
            width = 1.dp, color = backgroundColor // MaterialTheme.colors.primary
        ), modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, color = Color.White),
                onClick = { onClick?.invoke() }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(48.dp)
        ) {
            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun RoundedButtonPreview() {
    Column {
        OAuthButton(io.lowapple.app.android.petking.R.drawable.ic_google, "구글 로그인") {

        }
        OAuthButton(io.lowapple.app.android.petking.R.drawable.ic_google, "구글 로그인") {

        }
        OAuthButton(io.lowapple.app.android.petking.R.drawable.ic_google, "구글 로그인") {

        }
    }
}