package com.loswachabaches.bachewatch.ui.screens.registrarbache

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.tv.material3.OutlinedButtonDefaults
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteUiState
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// ─── Colores ──────────────────────────────────────────────────────────────────
private val Primary    = Color(0xFF1A1A2E)
private val Accent     = Color(0xFFFFDA25)
private val Surface    = Color.White
private val TextMuted  = Color(0xFF9CA3AF)
private val BgLight    = Color(0xFFF5F4F0)
private val BorderColor = Color(0xFFE5E7EB)

private val CDMX = GeoPoint(19.4326, -99.1332)

// ─── Screen ───────────────────────────────────────────────────────────────────
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RegistrarBacheScreen(
    uiState: ReporteUiState = ReporteUiState.Idle,
    onCancelClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: (photoUri: Uri?, location: GeoPoint?, descripcion: String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current

    // Estado foto
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var tempUri  by remember { mutableStateOf<Uri?>(null) }

    // Estado ubicación
    var savedLocation    by remember { mutableStateOf<GeoPoint?>(null) }
    var locationOverlay  by remember { mutableStateOf<MyLocationNewOverlay?>(null) }
    var locationLabel    by remember { mutableStateOf("Obteniendo ubicación…") }

    // Había faltado la descripción xd
    var descripcion by remember { mutableStateOf("") }

    // Fecha/hora en tiempo real
    var currentDateTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = SimpleDateFormat(
                "dd 'de' MMMM 'de' yyyy  •  HH:mm:ss",
                Locale("es", "MX")
            ).format(Date())
            kotlinx.coroutines.delay(1000L)
        }
    }

    // Permisos de cámara y ubicación juntos
    val permissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Launcher de cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) photoUri = tempUri
    }

    fun openCamera() {
        val file = File(context.cacheDir, "bache_${System.currentTimeMillis()}.jpg")
        val uri  = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        tempUri = uri
        cameraLauncher.launch(uri)
    }

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
        if (!permissions.allPermissionsGranted) permissions.launchMultiplePermissionRequest()
    }

    // Cuando el overlay detecta ubicación, la guardamos automáticamente
    LaunchedEffect(locationOverlay) {
        locationOverlay?.let { overlay ->
            while (overlay.myLocation == null) kotlinx.coroutines.delay(500L)
            savedLocation = overlay.myLocation
            locationLabel = "%.5f, %.5f".format(
                overlay.myLocation!!.latitude,
                overlay.myLocation!!.longitude
            )
        }
    }

    Scaffold(
        containerColor = BgLight,
        topBar = { RegistrarTopBar() },
        bottomBar = {
            BottomActions(
                onCancelClick = onCancelClick,
                onSaveClick   = { onSaveClick(photoUri, savedLocation,descripcion) },
                uiState = uiState
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text       = "Reportar Bache",
                modifier   = Modifier.fillMaxWidth(),
                color      = Primary,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ── Foto ──────────────────────────────────────────────────────────
            PhotoCard(
                photoUri  = photoUri,
                onClick   = {
                    if (permissions.allPermissionsGranted) openCamera()
                    else permissions.launchMultiplePermissionRequest()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Descripción ───────────────────────────────────────────────────────────
            SectionTitle(text = "Descripción", icon = Icons.Outlined.EditNote)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Describe el bache") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = Accent,
                    cursorColor = Accent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Sección Ubicación ─────────────────────────────────────────────
            SectionTitle(text = "Ubicación", icon = Icons.Outlined.LocationOn)
            Spacer(modifier = Modifier.height(10.dp))

            MapPreviewCard(
                onOverlayReady = { overlay -> locationOverlay = overlay },
                permissionsGranted = permissions.allPermissionsGranted
            )

            // Label de coordenadas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Outlined.MyLocation,
                    contentDescription = null,
                    tint     = Accent,
                    modifier = Modifier.size(14.dp)
                )
                Text(locationLabel, color = TextMuted, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ── Fecha y hora ──────────────────────────────────────────────────
            InfoCard(
                icon  = Icons.Outlined.CalendarMonth,
                title = "Fecha y hora",
                value = currentDateTime
            )
        }
    }
}

// ─── TopBar ───────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrarTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Bache", color = Accent,        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Watch", color = Color.White,   fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
    )
}

// ─── Foto: placeholder ↔ imagen tomada ───────────────────────────────────────
@Composable
private fun PhotoCard(photoUri: Uri?, onClick: () -> Unit) {
    Surface(
        onClick         = onClick,
        modifier        = Modifier.fillMaxWidth().height(200.dp),
        shape           = RoundedCornerShape(24.dp),
        color           = Surface,
        shadowElevation = 5.dp
    ) {
        if (photoUri != null) {
            // Muestra la foto tomada
            AsyncImage(
                model             = photoUri,
                contentDescription = "Foto del bache",
                contentScale      = ContentScale.Crop,
                modifier          = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))
            )
        } else {
            // Placeholder cámara
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
                    .border(1.dp, BorderColor, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Accent.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            tint     = Primary,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tomar foto del bache",  color = Primary,    fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Toca aquí para abrir la cámara", color = TextMuted, fontSize = 13.sp)
                }
            }
        }
    }
}

// ─── Mapa OSMDroid real ───────────────────────────────────────────────────────
@Composable
private fun MapPreviewCard(
    onOverlayReady: (MyLocationNewOverlay) -> Unit,
    permissionsGranted: Boolean
) {
    Surface(
        modifier        = Modifier.fillMaxWidth().height(180.dp),
        shape           = RoundedCornerShape(22.dp),
        color           = Surface,
        shadowElevation = 5.dp
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp)),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(false)
                    controller.setZoom(16.0)
                    controller.setCenter(CDMX)
                    zoomController.setVisibility(
                        org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                    )
                    val overlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), this).apply {
                        enableMyLocation()
                        enableFollowLocation()
                    }
                    overlays.add(overlay)
                    onOverlayReady(overlay)
                }
            }
        )
    }
}

// ─── Info card ────────────────────────────────────────────────────────────────
@Composable
private fun InfoCard(icon: ImageVector, title: String, value: String) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(18.dp),
        color           = Surface,
        shadowElevation = 3.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Primary, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Primary,   fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))
                Text(value, color = TextMuted, fontSize = 13.sp)
            }
        }
    }
}

// ─── Section title ────────────────────────────────────────────────────────────
@Composable
private fun SectionTitle(text: String, icon: ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = text, tint = Accent, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(text, color = Primary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

// ─── Bottom actions ───────────────────────────────────────────────────────────
@Composable
private fun BottomActions(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    uiState: ReporteUiState = ReporteUiState.Idle
) {
    Surface(
        modifier        = Modifier.fillMaxWidth().navigationBarsPadding(),
        color           = Surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick  = onCancelClick,
                modifier = Modifier.weight(1f).height(50.dp),
                shape    = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Primary
                )
            ) {
                Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick  = onSaveClick,
                enabled = uiState !is ReporteUiState.Cargando,
                modifier = Modifier.weight(1f).height(50.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Primary)
            ) {
                if (uiState is ReporteUiState.Cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Outlined.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Guardar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}