package com.deslomator.tititiri

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tititiri.ui.theme.TitiTiriTheme

val model = FrecuenciasModel()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TitiTiriTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var isLandscape by remember { mutableStateOf(false) }
                    isLandscape =   LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                    OrientationChooser(isLandscape)
                }
            }
        }
    }
}

@Composable
fun OrientationChooser(isLandscape: Boolean = false) {
    if (isLandscape) PrincipalLandscape()
    else PrincipalPortrait()
}

@Composable
fun PrincipalPortrait() {
    Unicornio()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Opciones()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BienMal()
            BotonComprobar()
            Spacer(modifier = Modifier.height(20.dp))
            BotonNueva()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PrincipalLandscape() {
    Unicornio(landscape = true)
    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Opciones()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BienMal()
            BotonComprobar()
            Spacer(modifier = Modifier.height(20.dp))
            BotonNueva()
        }
    }
}

@Composable
fun Opciones() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Memorias()
        Frecuencias()
        Zonas()
    }
}

@Composable
fun Unicornio(landscape: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    Image(
        modifier = Modifier
            .fillMaxSize()
            .scale(scaleX = if (landscape) 2.5f else 2f, scaleY = if (landscape) 2f else 2.5f),
        painter = painterResource(
            id = R.drawable.ic_launcher_foreground
        ),
        contentDescription = "carrusel fondo",
        colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
    )
}

@Composable
fun MySurface(
    visible: Boolean = true,
    text: String,
    tipoPregunta: Int = -1,
    color: Color = MaterialTheme.colors.background,
    clickCallback: () -> Unit) {
    val animColor by animateColorAsState(
        when (model.selectedTipo) {
            tipoPregunta -> MaterialTheme.colors.primaryVariant
            else -> MaterialTheme.colors.primary
        })
    val defcolor = if (tipoPregunta == -1) color else animColor
    if (visible) {
        Surface(
            modifier = Modifier
                .width(250.dp)
                .clickable(onClick = clickCallback),
            shape = MaterialTheme.shapes.medium,
            color = defcolor
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun MyCombo(
    items: List<Pair<Int, String>>,
    selectedIndex: Int,
    default: String,
    tipoPregunta: Int,
    clickCallback: (Pair<Int, String>) -> Unit,
    scrambledFreqs: List<Pair<Int, String>>
) {
    val text = when (selectedIndex) {
        -1 -> ""
        0 -> default
        else -> items[selectedIndex].second
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        MySurface(text = text, tipoPregunta = tipoPregunta) { if (model.selectedTipo != tipoPregunta) expanded = true }
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            scrambledFreqs.forEach {
                DropdownMenuItem(
                    onClick = {
                        clickCallback(it)
                        expanded = false
                    }) {
                    Text(
                        if (it.first == 0) default else it.second,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun Memorias() {
    MyCombo(
        items = src.frecuencias.map { Pair(it.id, it.memoria.toString()) },
        selectedIndex = model.memoriaSeleccionada,
        default = "Memoria",
        tipoPregunta = 0,
        clickCallback = { p -> model.onMemoriaChanged(p.first) },
        scrambledFreqs = model.scrambledFreqs().map { Pair(it.id, it.memoria.toString()) }
    )
}

@Composable
fun Frecuencias() {
    MyCombo(
        items = src.frecuencias.map { Pair(it.id, it.frecuencia) },
        selectedIndex = model.frecuenciaSeleccionada,
        default = "Frecuencia",
        tipoPregunta = 1,
        clickCallback = { p -> model.onFrecuenciaChanged(p.first) },
        scrambledFreqs = model.scrambledFreqs().map { Pair(it.id, it.frecuencia) }
    )
}

@Composable
fun Zonas() {
    MyCombo(
        items = src.frecuencias.map { Pair(it.id, it.zonaDropdown()) },
        selectedIndex = model.zonaSeleccionada,
        default = "Zona",
        tipoPregunta = 2,
        clickCallback = { p -> model.onZonaChanged(p.first) },
        scrambledFreqs = model.scrambledFreqs().map { Pair(it.id, it.zonaDropdown()) }
    )
}

@Composable
fun BienMal() {
    Spacer(modifier = Modifier.height(20.dp))
    val visible = model.showGood || model.showBad
    val color by animateColorAsState(
        when {
            model.showBad -> MaterialTheme.colors.error
            model.showGood -> MaterialTheme.colors.primaryVariant
            else -> Color.Transparent
        })
    val text = when {
        model.showBad -> "incorrecto"
        model.showGood -> "correcto"
        else -> ""
    }
    MySurface(visible = visible, text = text, color = color) {}
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun BotonComprobar() {
    OutlinedButton(
        onClick = { model.checkAnswer() },
        border = BorderStroke(1.dp, MaterialTheme.colors.error),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.error)
    ){
        Text(text = "COMPROBAR")
    }
}

@Composable
fun BotonNueva() {
//    Log.d("BotonNueva() init", "state.speak: ${model.speak}")
//    if (model.speak) SendTtsMessage(locution)
    val context = LocalContext.current
    OutlinedButton(
        onClick = { model.setNewPregunta(context = context) },
        border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.primaryVariant)
    ){
        Text( text = "NUEVA PREGUNTA" )
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview(
) {
    TitiTiriTheme {
        OrientationChooser()
    }
}

@Preview(device = Devices.PIXEL_4, widthDp = 720, heightDp = 360, showBackground = true)
@Composable
fun DefaultPreviewLandscape(
) {
    TitiTiriTheme(darkTheme = true) {
        OrientationChooser(true)
    }
}