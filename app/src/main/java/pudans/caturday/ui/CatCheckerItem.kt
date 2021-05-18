package pudans.caturday.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pudans.caturday.state.CheckerItemState

@Preview
@Composable
private fun Preview(

) {
	CatCheckerItem(
		state = CheckerItemState(
			"fieldName",
			labelValue = "value",
			color = 245
		)
	)
}

@Composable
fun CatCheckerItem(
	state: CheckerItemState
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
//			.padding(12.dp)
	) {
		Text(
			text = state.labelName,
			modifier = Modifier.align(CenterStart),
			color = Color.Black
		)
		Text(
			text = state.labelValue,
			modifier = Modifier.align(CenterEnd),
			color = Color.Black
		)
	}
}