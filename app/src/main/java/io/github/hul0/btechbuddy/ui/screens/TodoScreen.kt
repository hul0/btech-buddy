package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.hul0.btechbuddy.data.model.TodoItem
import io.github.hul0.btechbuddy.viewmodel.TodoViewModel
import io.github.hul0.btechbuddy.ui.theme.* // Color.kt palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    var newTodoText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To‑Do List", color = Gray900) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gray50,
                    titleContentColor = Gray900,
                    navigationIconContentColor = Blue700,
                    actionIconContentColor = Gray800
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(color = Gray200, thickness = 1.dp)

            // Input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { newTodoText = it },
                    label = { Text("New Task") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = {
                        if (newTodoText.isNotBlank()) {
                            viewModel.addTodo(newTodoText)
                            newTodoText = ""
                        }
                    }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue700,
                        unfocusedBorderColor = Gray300,
                        focusedLabelColor = Blue700,
                        cursorColor = Blue700,
                        focusedTextColor = Gray900,
                        unfocusedTextColor = Gray900,
                        focusedContainerColor = Gray50,
                        unfocusedContainerColor = Gray50,
                        focusedPlaceholderColor = Gray500,
                        unfocusedPlaceholderColor = Gray500
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                FilledIconButton(
                    onClick = {
                        if (newTodoText.isNotBlank()) {
                            viewModel.addTodo(newTodoText)
                            newTodoText = ""
                        }
                    },
                    enabled = newTodoText.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Blue700,
                        contentColor = Gray50,
                        disabledContainerColor = Gray300,
                        disabledContentColor = Gray600
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }

            // List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoListItem(
                        item = todo,
                        onCompletedChange = { viewModel.toggleTodoCompleted(todo.id) },
                        onDelete = { viewModel.deleteTodo(todo.id) }
                    )
                }
                item { Spacer(Modifier.height(12.dp)) }
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
    // Outlined, compact, no elevation/shadows/gradients
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) Green50 else Gray50
        ),
        border = BorderStroke(1.dp, if (item.isCompleted) Green100 else Gray200)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onCompletedChange() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Green700,
                    uncheckedColor = Gray500,
                    checkmarkColor = Gray50
                )
            )
            Spacer(modifier = Modifier.width(10.dp))

            // Status dot + text
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (item.isCompleted) Green700 else Blue700)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = item.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isCompleted) Gray700 else Gray900,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            // Delete
            FilledIconButton(
                onClick = onDelete,
                shape = RoundedCornerShape(10.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Red100,
                    contentColor = Red700
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
