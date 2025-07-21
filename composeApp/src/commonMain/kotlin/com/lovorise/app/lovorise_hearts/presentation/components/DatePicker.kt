package com.lovorise.app.lovorise_hearts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.months
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringArrayResource


@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selectedMonth: (String) -> Unit,
    selectedDay: (Int) -> Unit,
    selectedYear: (Int) -> Unit
) {
    val months = stringArrayResource(Res.array.months)
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val years = (1900..currentYear).toList()

    val monthState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val dayState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val yearState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    val selectedMonthIndex = monthState.firstVisibleItemIndex + 1
    val selectedYearValue = years.getOrNull(yearState.firstVisibleItemIndex) ?: currentYear
    val daysInSelectedMonth = getDaysInMonth(selectedMonthIndex, selectedYearValue)

    // Trigger callbacks whenever the selected values change
    LaunchedEffect(selectedMonthIndex, selectedYearValue) {
        selectedMonth(months.getOrNull(selectedMonthIndex - 1) ?: "")
        selectedYear(selectedYearValue)
    }
    LaunchedEffect(dayState.firstVisibleItemIndex) {
        val selectedDayValue = dayState.firstVisibleItemIndex + 1
        if (selectedDayValue in 1..daysInSelectedMonth) {
            selectedDay(selectedDayValue)
        }
    }

    Row(modifier = modifier) {
        SelectableList(
            items = months,
            state = monthState,
            highlightCondition = { index -> index == monthState.firstVisibleItemIndex + 1 }
        )
        SelectableList(
            items = (1..daysInSelectedMonth).map { it.toString() },
            state = dayState,
            highlightCondition = { index -> index == dayState.firstVisibleItemIndex + 1 }
        )
        SelectableList(
            items = years.map { it.toString() },
            state = yearState,
            highlightCondition = { index -> index == yearState.firstVisibleItemIndex + 1 }
        )
    }
}

fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30 // Default fallback, should not happen for valid months
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

@Composable
fun SelectableList(
    items: List<String>,
    state: LazyListState,
    highlightCondition: (Int) -> Boolean
) {
    LazyColumn(
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { index ->
            Text(
                text = items[index],
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(
                        if (highlightCondition(index)) Color.Gray else Color.Transparent
                    )
            )
        }
    }
}