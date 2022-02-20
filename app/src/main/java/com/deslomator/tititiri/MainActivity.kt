package com.deslomator.tititiri

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlin.random.Random

val state = SeleccionViewModel()

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TitiTiriTheme {
                // A surface container using the 'background' color from the theme
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
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
//        Pregunta()
//        Spacer(modifier = Modifier.height(100.dp))
        Opciones()
//        Spacer(modifier = Modifier.height(100.dp))
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
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Opciones()
        }
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
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
fun MyCard(
    visible: Boolean = true,
    text: String,
    color: Color,
    listener: () -> Unit) {
    if (visible) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .clickable(onClick = listener),
            backgroundColor = color,
            shape = RoundedCornerShape(15)
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
fun Pregunta() {
    MyCard(text = state.textoPregunta, color = colorResource(R.color.white_alpha)) { }
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
fun Memorias() {
    val items = frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (state.memoriaSeleccionada) {
        -1 -> ""
        0 -> "Memoria"
        else -> items[state.memoriaSeleccionada].numero.toString()
    }
    val color = when (state.tipo) {
        0 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MyCard(text = text, color = color) { if (state.tipo != 0) expanded = true }
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            Log.d("Memorias()", "tamaño ${items.size}")
            scrambledFreqs().forEach {
                DropdownMenuItem(
                    onClick = {
                        state.memoriaSeleccionada = it.id
                        expanded = false
                    }) {
                    Text(
                        if (it.id == 0) "Memoria" else it.numero.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun Frecuencias() {
    Log.d("Frecuencias()", "inicializando")
    val items = frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (state.frecuenciaSeleccionada) {
        -1 -> ""
        0 -> "Frecuencia"
        else -> items[state.frecuenciaSeleccionada].frecuencia
    }
    val color = when (state.tipo) {
        1 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MyCard(text = text, color = color) { if (state.tipo != 1) expanded = true }
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            scrambledFreqs().forEach {
                DropdownMenuItem(
                    onClick = {
                        state.frecuenciaSeleccionada = it.id
                        expanded = false
                    }) {
                    Text(
                        if (it.id == 0) "Frecuencia" else it.frecuencia,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun Zonas() {
    Log.d("Zonas()", "inicializando")
    val items = frecuencias
    var expanded by remember { mutableStateOf(false) }
    val text = when (state.zonaSeleccionada) {
        -1 -> ""
        0 -> "Zona"
        else -> items[state.zonaSeleccionada].zonaDropdown
    }
    val color = when (state.tipo) {
        2 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier

        .wrapContentWidth()
        .padding(10.dp)
    ) {
        MyCard(text = text, color = color) { if (state.tipo != 2) expanded = true }
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            scrambledFreqs().forEach {
                Log.d("Zonas()", "zona() -> ${it.zonaElegida}, ${it.zonaDropdown}")
                DropdownMenuItem(
                    onClick = {
                        state.zonaSeleccionada = it.id
                        expanded = false
                    }) {
                    Text(
                        if (it.id == 0) "Zona" else it.zonaDropdown,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun BienMal() {
    Spacer(modifier = Modifier.height(20.dp))
    val visible = state.showGood || state.showBad
    val color = when {
        state.showBad -> Color.Red.copy(alpha = .7f)
        state.showGood -> Color.Green.copy(alpha = .7f)
        else -> Color.White.copy(alpha = 0f)
    }
    val text = when {
        state.showBad -> "incorrecto"
        state.showGood -> "correcto"
        else -> ""
    }
    MyCard(visible = visible, text = text, color = color) { }
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun BotonComprobar() {
    OutlinedButton(
        onClick = {
            val goodIndex = when (state.tipo) {
                0 -> state.memoriaSeleccionada
                1 -> state.frecuenciaSeleccionada
                else -> state.zonaSeleccionada
            }
            if (state.memoriaSeleccionada == goodIndex
                && state.frecuenciaSeleccionada == goodIndex
                && state.zonaSeleccionada == goodIndex) {
                state.showBad = false
                state.showGood = true
            } else {
                state.showBad = true
                state.showGood = false
            }
            Log.d("comprobar", "tipo: ${state.tipo}, good: $goodIndex, memoria: ${state.memoriaSeleccionada}, frecuencia: ${state.frecuenciaSeleccionada}, zona: ${state.zonaSeleccionada}")
        },
        border = BorderStroke(1.dp, Color.Red),
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
    ){
        Text( text = "COMPROBAR" )
    }
}

@Composable
fun BotonNueva() {
    Log.d("BotonNueva() init", "state.speak: ${state.speak}")
    var locution by remember { mutableStateOf("") }
    if (state.speak) SendTtsMessage(locution)
    OutlinedButton(
        onClick = {
            for (item in frecuencias) item.elegirZona()
            state.pregunta = Random.nextInt(frecuencias.size - 1) + 1
            state.tipo = Random.nextInt(3)
            val item = frecuencias[state.pregunta]
            when (state.tipo) {
                0 -> {
                    state.textoPregunta = item.numeroTts
                    locution = item.numeroTts
                    state.memoriaSeleccionada = state.pregunta
                    state.frecuenciaSeleccionada = -1
                    state.zonaSeleccionada = -1
                }
                1 -> {
                    state.textoPregunta = item.frecuenciaPregunta
                    locution = item.frecuenciaTts()
                    state.memoriaSeleccionada = -1
                    state.frecuenciaSeleccionada = state.pregunta
                    state.zonaSeleccionada = -1
                }
                else -> {
                    state.textoPregunta = item.zonaPregunta
                    locution = item.zonaTts
                    state.memoriaSeleccionada = -1
                    state.frecuenciaSeleccionada = -1
                    state.zonaSeleccionada = state.pregunta
                }
            }
            state.showBad = false
            state.showGood = false
            state.speak = true
            Log.d("BotonNueva() onClick", "state.speak: ${state.speak}")
        },
        border = BorderStroke(1.dp, Color.Blue),
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ){
        Text( text = "NUEVA PREGUNTA" )
    }
}

@Composable
private fun SendTtsMessage(locution: String) {
    Log.d("sendTtsMessage()", "inicializando, locution: $locution")
    state.speak = false
    if (locution.length > 1) {
        val ttsHelper = TtsHelper(LocalContext.current)
        val msg = ttsHelper.ttsHandler.obtainMessage()
        val bundle = msg.data
        bundle.putString("locution", locution)
        msg.data = bundle
        ttsHelper.ttsHandler.sendMessage(msg)
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    TitiTiriTheme {
        OrientationChooser()
    }
}