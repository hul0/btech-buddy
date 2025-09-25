package io.github.hul0.btechbuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    // Color palette
    val primaryGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFF8B5CF6)
        )
    )

    val accentGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFEC4899),
            Color(0xFFF43F5E)
        )
    )

    val darkBg = Color(0xFF0F0F1A)
    val cardBg = Color(0xFF1A1A2E).copy(alpha = 0.7f)
    val borderColor = Color(0xFF6366F1).copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(primaryGradient)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "About",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                    Text(
                        "About",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Version 3.0.1",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // About Our Team
            SectionCard(
                title = "Our Amazing Team",
                icon = Icons.Filled.Group,
                backgroundColor = cardBg,
                borderColor = borderColor
            ) {
                Text(
                    "We are a passionate team of developers, designers, and innovators " +
                            "committed to creating exceptional digital experiences. Our diverse " +
                            "backgrounds and expertise allow us to tackle complex challenges " +
                            "with creative solutions.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // WhatsApp Groups
            SectionCard(
                title = "Join Our Community",
                icon = Icons.Filled.Forum,
                backgroundColor = cardBg,
                borderColor = Color(0xFF25D366).copy(alpha = 0.4f)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    WhatsAppGroupCard(
                        groupName = "Tech Discussions",
                        memberCount = "2.5k+ members",
                        onClick = { /* Handle WhatsApp group link */ }
                    )
                    WhatsAppGroupCard(
                        groupName = "App Updates & News",
                        memberCount = "5k+ members",
                        onClick = { /* Handle WhatsApp group link */ }
                    )
                    WhatsAppGroupCard(
                        groupName = "Beta Testing Community",
                        memberCount = "500+ members",
                        onClick = { /* Handle WhatsApp group link */ }
                    )
                }
            }

            // About This App
            SectionCard(
                title = "About This App",
                icon = Icons.Filled.PhoneAndroid,
                backgroundColor = cardBg,
                borderColor = borderColor
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Purpose
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Purpose",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B5CF6),
                            fontSize = 16.sp
                        )
                        Text(
                            "This revolutionary app is designed to transform the way you interact " +
                                    "with technology. We leverage cutting-edge algorithms and intuitive design " +
                                    "to deliver an unparalleled user experience that adapts to your needs.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }

                    // Legal Links
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LegalButton(
                            text = "Terms & Conditions",
                            modifier = Modifier.weight(1f),
                            onClick = { uriHandler.openUri("https://example.com/terms") }
                        )
                        LegalButton(
                            text = "Privacy Policy",
                            modifier = Modifier.weight(1f),
                            onClick = { uriHandler.openUri("https://example.com/privacy") }
                        )
                    }
                }
            }

            // Share Button
            Button(
                onClick = { /* Handle share */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(accentGradient)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                        Text(
                            "Share This Amazing App",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // The Greatest Developer Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFFA500)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F1F2E)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title with crown
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👑", fontSize = 24.sp)
                        Text(
                            "THE DEVELOPER",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFD700)
                        )
                        Text("👑", fontSize = 24.sp)
                    }

                    // Profile Section
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF6366F1),
                                        Color(0xFF8B5CF6),
                                        Color(0xFFEC4899)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "JD",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        "John Developer",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        "Full-Stack Wizard | Mobile Architect | AI Enthusiast",
                        fontSize = 14.sp,
                        color = Color(0xFFFFD700).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    // Bio
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2A2A3E).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "🚀 10+ years crafting digital masterpieces\n" +
                                    "💡 500+ projects delivered with excellence\n" +
                                    "🌟 Stack Overflow top contributor\n" +
                                    "🏆 Google Developer Expert\n" +
                                    "☕ Powered by coffee and passion",
                            modifier = Modifier.padding(16.dp),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }

                    // Tech Stack
                    Text(
                        "Tech Arsenal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5CF6)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TechBadge("Kotlin")
                        TechBadge("Flutter")
                        TechBadge("React")
                        TechBadge("Node.js")
                    }

                    // Contact Options
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ContactButton(
                            icon = Icons.Filled.Email,
                            text = "Let's Collaborate",
                            gradient = Brush.linearGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                            ),
                            onClick = { /* Handle email */ }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SocialButton(
                                text = "GitHub",
                                color = Color(0xFF333333),
                                modifier = Modifier.weight(1f),
                                onClick = { uriHandler.openUri("https://github.com") }
                            )
                            SocialButton(
                                text = "LinkedIn",
                                color = Color(0xFF0077B5),
                                modifier = Modifier.weight(1f),
                                onClick = { uriHandler.openUri("https://linkedin.com") }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SocialButton(
                                text = "Twitter",
                                color = Color(0xFF1DA1F2),
                                modifier = Modifier.weight(1f),
                                onClick = { uriHandler.openUri("https://twitter.com") }
                            )
                            SocialButton(
                                text = "Portfolio",
                                color = Color(0xFFEC4899),
                                modifier = Modifier.weight(1f),
                                onClick = { uriHandler.openUri("https://portfolio.com") }
                            )
                        }
                    }
                }
            }

            // Footer
            Text(
                "Made with ❤️ in Kotlin",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            content()
        }
    }
}

@Composable
fun WhatsAppGroupCard(
    groupName: String,
    memberCount: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF25D366).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    groupName,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    memberCount,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
            Icon(
                Icons.Outlined.ArrowForward,
                contentDescription = "Join",
                tint = Color(0xFF25D366),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LegalButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text,
            fontSize = 12.sp,
            color = Color(0xFF8B5CF6)
        )
    }
}

@Composable
fun TechBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF6366F1).copy(alpha = 0.2f))
            .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            fontSize = 12.sp,
            color = Color(0xFF8B5CF6),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ContactButton(
    icon: ImageVector,
    text: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = text, tint = Color.White)
                Text(text, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}