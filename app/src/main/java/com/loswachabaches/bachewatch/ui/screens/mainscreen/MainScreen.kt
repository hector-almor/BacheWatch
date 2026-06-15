package com.loswachabaches.bachewatch.ui.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loswachabaches.bachewatch.R
import com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs.EstadisticasTab
import com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs.MapaTab
import com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs.MiCuentaTab
import com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs.MisReportesTab
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel

private enum class Tab {
    MAPA,
    ESTADISTICAS,
    MIS_REPORTES,
    MI_CUENTA
}

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    onLogoutClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var activeTab by remember { mutableStateOf(Tab.MAPA) }

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            BacheTopBar()
        },
        bottomBar = {
            BacheBottomNav(
                activeTab = activeTab,
                onTabSelected = { activeTab = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.offset(y = 42.dp),
                shape = CircleShape,
                containerColor = AccentColor,
                contentColor = PrimaryColor,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nuevo reporte",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                Tab.MAPA -> MapaTab()

                Tab.ESTADISTICAS -> EstadisticasTab()

                Tab.MIS_REPORTES -> MisReportesTab()

                Tab.MI_CUENTA -> MiCuentaTab(
                    authViewModel = authViewModel,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BacheTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bachewatch_logo),
                    contentDescription = "Logo BacheWatch",
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.size(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_largo),
                    contentDescription = "Nombre BacheWatch",
                    modifier = Modifier.size(width = 125.dp, height = 38.dp),
                    contentScale = ContentScale.Fit
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PrimaryColor
        )
    )
}

@Composable
private fun BacheBottomNav(
    activeTab: Tab,
    onTabSelected: (Tab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = SurfaceColor,
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Outlined.Map,
                label = "Mapa",
                selected = activeTab == Tab.MAPA,
                onClick = { onTabSelected(Tab.MAPA) }
            )

            BottomNavItem(
                icon = Icons.Outlined.BarChart,
                label = "Estadísticas",
                selected = activeTab == Tab.ESTADISTICAS,
                onClick = { onTabSelected(Tab.ESTADISTICAS) }
            )

            BottomNavItem(
                icon = Icons.Outlined.Description,
                label = "Mis reportes",
                selected = activeTab == Tab.MIS_REPORTES,
                onClick = { onTabSelected(Tab.MIS_REPORTES) }
            )

            BottomNavItem(
                icon = Icons.Outlined.AccountCircle,
                label = "Mi cuenta",
                selected = activeTab == Tab.MI_CUENTA,
                onClick = { onTabSelected(Tab.MI_CUENTA) }
            )
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val iconColor = if (selected) AccentColor else TextMutedColor
    val textColor = if (selected) PrimaryColor else TextMutedColor

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(23.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}