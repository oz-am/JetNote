package com.example.jetnote.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnote.R
import com.example.jetnote.components.NoteButton
import com.example.jetnote.components.NoteInputText
import com.example.jetnote.data.NoteData
import com.example.jetnote.model.Note

@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            }, actions = {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
            }, backgroundColor = Color(0xFFDADFE3)
        )

        // content
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputText(
                text = title, label = "Title", onTextChanged = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() })
                        title = it
                }, modifier = Modifier.padding(9.dp, 8.dp)
            )
            NoteInputText(text = description, label = "Add a note", onTextChanged = {
                if (it.all { char -> char.isLetter() || char.isWhitespace() })
                    description = it
            }, modifier = Modifier.padding(9.dp, 8.dp))
            NoteButton(text = "Save", onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    onAddNote(Note(title = title, description = description))
                    title = ""
                    description = ""
                    Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                }
            })

            Divider()

            LazyColumn {
                items(notes) { note ->
                    NoteRow(note = note, onNoteClicked = {
                        onRemoveNote(it)
                    })
                }
            }
        }
    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit
) {
    Surface(
        modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 3.dp))
            .fillMaxWidth(),
        color = Color(0xFFDADFE3),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp, 6.dp)
                .clickable { onNoteClicked(note) },
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = note.title, style = MaterialTheme.typography.subtitle2)
            Text(text = note.description, style = MaterialTheme.typography.subtitle1)
            Text(text = note.date, style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NoteScreen(notes = NoteData().loadNotes(), onAddNote = {}, onRemoveNote = {})
}
