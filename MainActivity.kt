package com.gediya.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101014)
private val Panel = Color(0xFF1B1B22)
private val Gold = Color(0xFFD6B56D)
private val Cream = Color(0xFFF5EAD2)
private val Muted = Color(0xFFAAA6A0)

data class ChatContact(val name: String, val preview: String, val time: String, val initials: String)
data class ChatMessage(val text: String, val mine: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GediyaApp() }
    }
}

@Composable
fun GediyaApp() {
    var selected by remember { mutableStateOf<ChatContact?>(null) }
    val contacts = remember {
        listOf(
            ChatContact("Family Group", "Good morning 🙏", "9:41", "FG"),
            ChatContact("Rahul", "See you soon!", "Yesterday", "R"),
            ChatContact("Priya", "Photo received", "Tue", "P"),
            ChatContact("Shop Team", "Meeting at 5 PM", "Mon", "ST")
        )
    }
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Gold, background = Ink, surface = Panel,
            onPrimary = Ink, onBackground = Cream, onSurface = Cream
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Ink) {
            if (selected == null) HomeScreen(contacts) { selected = it }
            else ConversationScreen(selected!!, onBack = { selected = null })
        }
    }
}

@Composable
fun HomeScreen(contacts: List<ChatContact>, onOpen: (ChatContact) -> Unit) {
    var query by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Ink).padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("GEDIYA", color = Gold, fontSize = 27.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("PRIVATE • PREMIUM CHAT", color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
            }
            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Gold)
        }
        Spacer(Modifier.height(26.dp))
        Row(
            Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Gold)
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = query, onValueChange = { query = it },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Cream, fontSize = 14.sp),
                decorationBox = { inner ->
                    if (query.isEmpty()) Text("Search conversations", color = Muted, fontSize = 14.sp)
                    inner()
                }
            )
        }
        Spacer(Modifier.height(24.dp))
        Text("MESSAGES", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(contacts.filter { it.name.contains(query, true) || it.preview.contains(query, true) }) { contact ->
                Row(
                    Modifier.fillMaxWidth().clickable { onOpen(contact) }
                        .background(Panel, RoundedCornerShape(18.dp)).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(48.dp).background(Color(0xFF3A3020), CircleShape), contentAlignment = Alignment.Center) {
                        Text(contact.initials, color = Gold, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(contact.name, color = Cream, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text(contact.preview, color = Muted, fontSize = 13.sp)
                    }
                    Text(contact.time, color = Gold, fontSize = 11.sp)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Text("A better way to stay connected.", color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 18.dp))
    }
}

@Composable
fun ConversationScreen(contact: ChatContact, onBack: () -> Unit) {
    val messages = remember(contact.name) {
        mutableStateListOf(
            ChatMessage("Welcome to Gediya Chat ✨", false),
            ChatMessage("This is a demo conversation.", false),
            ChatMessage("Looks premium!", true)
        )
    }
    var draft by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Ink)) {
        Row(
            Modifier.fillMaxWidth().background(Panel).padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Gold) }
            Column(Modifier.weight(1f)) {
                Text(contact.name, color = Cream, fontWeight = FontWeight.Bold)
                Text("Demo chat", color = Gold, fontSize = 11.sp)
            }
            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Gold)
        }
        LazyColumn(
            Modifier.weight(1f).fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { message ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (message.mine) Arrangement.End else Arrangement.Start) {
                    Text(
                        message.text,
                        color = if (message.mine) Ink else Cream,
                        modifier = Modifier.widthIn(max = 280.dp)
                            .background(if (message.mine) Gold else Panel, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(12.dp).background(Panel, RoundedCornerShape(24.dp)).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = draft, onValueChange = { draft = it },
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp, vertical = 10.dp),
                textStyle = androidx.compose.ui.text.TextStyle(color = Cream, fontSize = 14.sp),
                decorationBox = { inner ->
                    if (draft.isEmpty()) Text("Type a message…", color = Muted, fontSize = 14.sp)
                    inner()
                }
            )
            IconButton(onClick = {
                if (draft.isNotBlank()) {
                    messages.add(ChatMessage(draft.trim(), true))
                    draft = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Gold)
            }
        }
    }
}
