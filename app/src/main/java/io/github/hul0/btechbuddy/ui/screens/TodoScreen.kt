package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.data.model.TodoItem
import io.github.hul0.btechbuddy.viewmodel.TodoViewModel

// Legendary palette (aligned with other screens)
private val DeepSpace = Color(0xFF0D1421)
private val NearBlack = Color(0xFF070B14)
private val CyberTeal = Color(0xFF1BFFFF)
private val ElectricPurple = Color(0xFF8458B3)
private val NeonPink = Color(0xFFD4145A)
private val GlowWhite = Color(0xFFFFFFFF)

// Background gradient
private val DarkHeroGradient = Brush.verticalGradient(
    colors = listOf(
        DeepSpace,
        Color(0xFF0B0F1A),
        NearBlack
    )
)

// Glass brushes
private val GlassField = Brush.linearGradient(
    colors = listOf(
        GlowWhite.copy(alpha = 0.10f),
        GlowWhite.copy(alpha = 0.06f),
        GlowWhite.copy(alpha = 0.04f)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    var newTodoText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // Top bar over subtle scrim for legibility
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.22f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = { Text("To‑Do List", color = GlowWhite) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = GlowWhite,
                        navigationIconContentColor = GlowWhite,
                        actionIconContentColor = GlowWhite
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkHeroGradient)
                .padding(paddingValues)
        ) {
            Divider(color = GlowWhite.copy(alpha = 0.12f), thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { newTodoText = it },
                    placeholder = { Text("New task...", color = GlowWhite.copy(alpha = 0.62f)) },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassField, RoundedCornerShape(20.dp)),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = {
                        if (newTodoText.isNotBlank()) {
                            viewModel.addTodo(newTodoText)
                            newTodoText = ""
                        }
                    }),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberTeal,
                        unfocusedBorderColor = GlowWhite.copy(alpha = 0.24f),
                        focusedLabelColor = CyberTeal,
                        cursorColor = CyberTeal,
                        focusedTextColor = GlowWhite,
                        unfocusedTextColor = GlowWhite,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f),
                        unfocusedPlaceholderColor = GlowWhite.copy(alpha = 0.62f),
                        focusedTrailingIconColor = GlowWhite.copy(alpha = 0.9f),
                        unfocusedTrailingIconColor = GlowWhite.copy(alpha = 0.8f)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                FilledIconButton(
                    onClick = {
                        if (newTodoText.isNotBlank()) {
                            viewModel.addTodo(newTodoText)
                            newTodoText = ""
                        }
                    },
                    enabled = newTodoText.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = CyberTeal,
                        contentColor = Color.Black,
                        disabledContainerColor = GlowWhite.copy(alpha = 0.10f),
                        disabledContentColor = GlowWhite.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoListItem(
                        item = todo,
                        onCompletedChange = { viewModel.toggleTodoCompleted(todo.id) },
                        onDelete = { viewModel.deleteTodo(todo.id) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun TodoListItem(
    item: TodoItem,
    onCompletedChange: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, GlowWhite.copy(alpha = 0.20f))
    ) {
        Row(
            modifier = Modifier
                .background(GlassField, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onCompletedChange() },
                colors = CheckboxDefaults.colors(
                    checkedColor = CyberTeal,
                    uncheckedColor = GlowWhite.copy(alpha = 0.45f),
                    checkmarkColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (item.isCompleted) GlowWhite.copy(alpha = 0.28f) else CyberTeal)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = item.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = if (item.isCompleted) GlowWhite.copy(alpha = 0.6f) else GlowWhite,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            FilledIconButton(
                onClick = onDelete,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = GlowWhite.copy(alpha = 0.10f),
                    contentColor = NeonPink
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
