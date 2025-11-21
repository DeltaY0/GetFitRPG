package com.example.getfitrpg.navigation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    // 1. The Container Style (Black pill with white border)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(25)) // Fully rounded sides
            .border(2.dp, Color.White, RoundedCornerShape(25))
    ) {
        // 2. Calculate widths to handle the animation
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            val maxWidth = maxWidth
            val itemCount = BottomNavItem.entries.size
            // Calculate exactly how wide each tab area is
            val tabWidth = maxWidth / itemCount

            // Find the index of the currently selected item
            val selectedIndex = BottomNavItem.entries.indexOf(selectedItem)

            // 3. The Sliding Animation
            // We animate the "offset" (X position) of the white box
            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow // Smooth slide
                ),
                label = "indicator"
            )

            // 4. The White Background Indicator
            // This sits BEHIND the icons but moves based on the offset
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset) // The magic movement
                    .width(tabWidth)
                    .fillMaxSize()
                    .padding(8.dp) // Add padding so it doesn't touch the border
                    .clip(RoundedCornerShape(10.dp)) // Rounded square shape
                    .background(Color.White)
            )

            // 5. The Icons Row
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem.entries.forEach { item ->
                    val isSelected = item == selectedItem

                    // Icon Container
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Remove default ripple for cleaner look
                            ) { onItemSelected(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            // Key Detail: White icon if unselected, Black icon if selected (to contrast with white box)
                            tint = if (isSelected) Color.Black else Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}