package za.co.wethinkcode.vnitpick

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProviderAtPosition

private val CommitShape = GenericShape { size, _ ->
    moveTo(0f, size.height)
    lineTo(0f, size.height - 20f)
    lineTo(size.width - 20f, size.height - 20f)
    lineTo(size.width - 20f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ExampleBox(shape: Shape) {
    Box(modifier = Modifier.fillMaxSize()) {
        val tooltipState = rememberBasicTooltipState(isPersistent = true)
        BasicTooltipBox(
            modifier = Modifier.clip(shape),
            positionProvider = PopupPositionProviderAtPosition(
                Offset(0f, 0f),
                true,
                Offset(0f, 0f),
                windowMarginPx = 5
            ),
            tooltip = {
                Text("Tooltip")
            },
            state = tooltipState
        ) {
            Box(
                modifier = Modifier.width(60.dp)
                    .height(100.dp)
                    .clickable {
                        println("Clicked")
                    }
                    .clip(shape)
                    .background(Color.Red)
                    .border(1.dp, Color.Blue, shape = shape)
            )
        }
    }
}
