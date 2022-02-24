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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tititiri.ui.theme.TitiTiriTheme

val state = SeleccionViewModel()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TitiTiriTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    OrientationChooser()
                }
            }
        }
    }
}

@Composable
fun OrientationChooser() {
    val portrait =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    if (portrait) PrincipalPortrait()
    else PrincipalLandscape()
}

@Composable
fun PrincipalPortrait() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.background)))
    Image(
        modifier = Modifier
            .fillMaxSize()
            .scale(scaleX = 2f, scaleY = 2.5f),
        painter = painterResource(
            id = R.drawable.ic_launcher_foreground),
        contentDescription = "carrusel fondo",
        colorFilter = ColorFilter.tint(Color.DarkGray))
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    )
    Image(
        modifier = Modifier
            .fillMaxSize()
            .scale(scaleX = 2f, scaleY = 2.5f),
        painter = painterResource(
            id = R.drawable.ic_launcher_foreground
        ),
        contentDescription = "carrusel fondo",
        colorFilter = ColorFilter.tint(Color.DarkGray)
    )
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
fun MySurface(
    visible: Boolean = true,
    text: String,
    tipoPregunta: Int = -1,
    color: Color = Color.White,
    clickCallback: () -> Unit) {
    val animColor by animateColorAsState(
        when (Frecuencias.selectedTipo) {
            tipoPregunta -> Color.Magenta.copy(alpha = .7f)
            else -> Color.White.copy(alpha = .7f)
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
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MyDropdown(
    expanded: Boolean,
    clickCallback: (Pair<Int, String>) -> Unit,
    onDismiss: () -> Unit,
    default: String,
    scrambledFreqs: List<Pair<Int, String>>
) {
    DropdownMenu(
        expanded = expanded,
        modifier = Modifier
            .background(Color.Green.copy(alpha = .7f))
            .width(250.dp),
        onDismissRequest = onDismiss
    ) {
        scrambledFreqs.forEach {
            DropdownMenuItem(
                onClick = {
                    clickCallback(it)
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


@Composable
fun MyCombo(
    selectedIndex: Int,
    default: String,
    tipoPregunta: Int,
    clickCallback: (Pair<Int, String>) -> Unit,
    scrambledFreqs: List<Pair<Int, String>>
) {
    val items = Frecuencias.frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (selectedIndex) {
        -1 -> ""
        0 -> default
        else -> items[selectedIndex].memoria.toString()
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MySurface(text = text, tipoPregunta = tipoPregunta) { if (Frecuencias.selectedTipo != tipoPregunta) expanded = true }
        MyDropdown(
            expanded = expanded,
            clickCallback = clickCallback,
            onDismiss = { expanded = false },
            default = default,
            scrambledFreqs = scrambledFreqs
        )
    }
}

@Composable
fun Memorias() {
    MyCombo(
        selectedIndex = Frecuencias.memoriaSeleccionada,
        default = "Memoria",
        tipoPregunta = 0,
        clickCallback = { p ->
            Frecuencias.memoriaSeleccionada = p.first
//            expanded = false
        },
        scrambledFreqs = Frecuencias.scrambledFreqs().map { Pair(it.id, it.memoria.toString()) }
    )
}

@Composable
fun Memorias2() {
    val items = Frecuencias.frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (Frecuencias.memoriaSeleccionada) {
        -1 -> ""
        0 -> "Memoria"
        else -> items[Frecuencias.memoriaSeleccionada].memoria.toString()
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MySurface(text = text, tipoPregunta = 0) { if (Frecuencias.selectedTipo != 0) expanded = true }
        MyDropdown(
            expanded = expanded,
            clickCallback = { p ->
                Frecuencias.memoriaSeleccionada = p.first
                expanded = false
            },
            onDismiss = { expanded = false },
            default = "Memoria",
            scrambledFreqs = Frecuencias.scrambledFreqs().map { Pair(it.id, it.memoria.toString()) }
        )
    }
}

@Composable
fun Frecuencias() {
    Log.d("Frecuencias()", "inicializando")
    val items = Frecuencias.frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (Frecuencias.frecuenciaSeleccionada) {
        -1 -> ""
        0 -> "Frecuencia"
        else -> items[Frecuencias.frecuenciaSeleccionada].frecuencia
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MySurface(text = text, tipoPregunta = 1) { if (Frecuencias.selectedTipo != 1) expanded = true }
        MyDropdown(
            expanded = expanded,
            clickCallback = { p ->
                Frecuencias.frecuenciaSeleccionada = p.first
                expanded = false
            },
            onDismiss = { expanded = false },
            default = "Frecuencia",
            scrambledFreqs = Frecuencias.scrambledFreqs().map { Pair(it.id, it.frecuencia) }
        )
    }
}

@Composable
fun Zonas() {
    Log.d("Zonas()", "inicializando")
    val items = Frecuencias.frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (Frecuencias.zonaSeleccionada) {
        -1 -> ""
        0 -> "Zona"
        else -> items[Frecuencias.zonaSeleccionada].zonaDropdown()
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MySurface(text = text, tipoPregunta = 2) { if (Frecuencias.selectedTipo != 2) expanded = true }
        MyDropdown(
            expanded = expanded,
            clickCallback = { p ->
                Frecuencias.zonaSeleccionada = p.first
                expanded = false
            },
            onDismiss = { expanded = false },
            default = "Zona",
            scrambledFreqs = Frecuencias.scrambledFreqs().map { Pair(it.id, it.zonaDropdown()) }
        )
    }
}

@Composable
fun BienMal() {
    Spacer(modifier = Modifier.height(20.dp))
    val visible = state.showGood || state.showBad
    val color by animateColorAsState(
        when {
            state.showBad -> Color.Red.copy(alpha = .7f)
            state.showGood -> Color.Green.copy(alpha = .7f)
            else -> Color.White.copy(alpha = 0f)
        })
    val text = when {
        state.showBad -> "incorrecto"
        state.showGood -> "correcto"
        else -> ""
    }
    MySurface(visible = visible, text = text, color = color) {}
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun BotonComprobar() {
    OutlinedButton(
        onClick = {
            val goodIndex = when (Frecuencias.selectedTipo) {
                0 -> Frecuencias.memoriaSeleccionada
                1 -> Frecuencias.frecuenciaSeleccionada
                else -> Frecuencias.zonaSeleccionada
            }
            if (Frecuencias.memoriaSeleccionada == goodIndex
                && Frecuencias.frecuenciaSeleccionada == goodIndex
                && Frecuencias.zonaSeleccionada == goodIndex) {
                state.showBad = false
                state.showGood = true
            } else {
                state.showBad = true
                state.showGood = false
            }
        },
        border = BorderStroke(1.dp, Color.Magenta),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Magenta)
    ){
        Text(text = "COMPROBAR")
    }
}

@Composable
fun BotonNueva() {
    Log.d("BotonNueva() init", "state.speak: ${state.speak}")
    var locution by remember { mutableStateOf("") }
    if (state.speak) SendTtsMessage(locution)
    OutlinedButton(
        onClick = {
            Frecuencias.setNewPregunta()
            locution = when (Frecuencias.selectedTipo) {
                0 -> Frecuencias.selectedItem().numeroTts
                1 -> Frecuencias.selectedItem().frecuenciaTts()
                else -> Frecuencias.selectedItem().zonaTts()
            }
            state.showBad = false
            state.showGood = false
            state.speak = true
            Log.d("BotonNueva() onClick", "state.speak: ${state.speak}")
        },
        border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.teal_700))
    ){
        Text( text = "NUEVA PREGUNTA" )
    }
}

@Composable
private fun SendTtsMessage(locution: String) {
    Log.d("sendTtsMessage()", "inicializando, locution: $locution")
    state.speak = false
    if (locution.length > 1) {
        TtsHelper(LocalContext.current, locution = locution)
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    TitiTiriTheme {
        OrientationChooser()
    }
}