package com.deslomator.tititiri

import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
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
                    Principal()
                }
            }
        }
    }
}

@Composable
fun Principal() {

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Pregunta()
        Spacer(modifier = Modifier.height(100.dp))
        Opciones()
        Spacer(modifier = Modifier.height(100.dp))
        BienMal()
        BotonComprobar()
        Spacer(modifier = Modifier.height(20.dp))
        BotonNueva()
    }
}

@Composable
fun Pregunta() {
    Text(
        state.textoPregunta,
        modifier = Modifier
            .background(Color.White.copy(alpha = .7f))
            .padding(start = 60.dp, end = 60.dp, top = 15.dp, bottom = 15.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun Opciones() {
    Log.d("Opciones()", "creando Row")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Memorias()
        Frecuencias()
        Zonas()
    }
}

@Composable
fun Memorias() {
    Log.d("Memorias()", "inicializando")
    val items = frecuencias
    var expanded by remember { mutableStateOf(false) }
    val color = when (state.tipo) {
        0 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        Text(
            when (state.memoriaSeleccionada) {
                -1 -> ""
                0 -> "Memoria"
                else -> items[state.memoriaSeleccionada].numero.toString()
            },
            modifier = Modifier
                .background(color)
                .width(250.dp)
                .clickable(onClick = { if (state.tipo != 0) expanded = true })
                .padding(10.dp),
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            Log.d("Memorias()", "tamaño ${items.size}")
            items.forEachIndexed { index, value ->
                DropdownMenuItem(
                    onClick = {
                        state.memoriaSeleccionada = index
                        expanded = false
                    }) {
                    Text(
                        if (index == 0) "Memoria" else value.numero.toString(),
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
    val color = when (state.tipo) {
        1 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(10.dp)
    ) {
        Text(
            when (state.frecuenciaSeleccionada) {
                -1 -> ""
                0 -> "Frecuencia"
                else -> items[state.frecuenciaSeleccionada].frecuencia
            },
            modifier = Modifier
                .background(color)
                .width(250.dp)
                .clickable(onClick = { if (state.tipo != 1) expanded = true })
                .padding(10.dp),
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            Log.d("Memorias()", "tamaño ${items.size}")
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        state.frecuenciaSeleccionada = index
                        expanded = false
                    }) {
                    Text(
                        if (index == 0) "Frecuencia" else item.frecuencia,
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
    val color = when (state.tipo) {
        2 -> Color.Magenta.copy(alpha = .7f)
        else -> Color.White.copy(alpha = .7f)
    }
    Box(modifier = Modifier

        .wrapContentWidth()
        .padding(10.dp)
    ) {
        Text(
            when (state.zonaSeleccionada) {
                -1 -> ""
                0 -> "Zona"
                else -> items[state.zonaSeleccionada].zona()
            },
            modifier = Modifier
                .background(color)
                .width(250.dp)
                .clickable(onClick = { if (state.tipo != 2) expanded = true })
                .padding(10.dp),
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(Color.Green.copy(alpha = .7f))
                .width(250.dp),
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                Log.d("Zonas()", "zona() -> ${item.zonaElegida}, ${item.zona()}")
                DropdownMenuItem(
                    onClick = {
                        state.zonaSeleccionada = index
                        expanded = false
                    }) {
                    Text(
                        if (index == 0) "Zona" else item.zona(),
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
    Log.d("BienMal()", "showGood: ${state.showGood}, showBad: ${state.showBad}")
    Spacer(modifier = Modifier.height(20.dp))
    val alpha = if (!state.showGood && !state.showBad) 0f else .7f
    Text(
        text = when {
            state.showBad -> "incorrecto"
            state.showGood -> "correcto"
            else -> ""
        },
        modifier = Modifier
            .background(Color.White.copy(alpha = alpha))
            .wrapContentWidth()
            .padding(20.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3
    )
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
    OutlinedButton(
        onClick = {
            for (item in frecuencias) item.elegirZona()
            state.pregunta = Random.nextInt(frecuencias.size - 1) + 1
            state.tipo = Random.nextInt(3)
            val item = frecuencias[state.pregunta]
            when (state.tipo) {
                0 -> {
                    state.textoPregunta = item.numeroTts
                    state.memoriaSeleccionada = state.pregunta
                    state.frecuenciaSeleccionada = -1
                    state.zonaSeleccionada = -1
                }
                1 -> {
                    state.textoPregunta = item.frecuenciaPregunta
                    state.memoriaSeleccionada = -1
                    state.frecuenciaSeleccionada = state.pregunta
                    state.zonaSeleccionada = -1
                }
                else -> {
                    state.textoPregunta = item.zonaTts
                    state.memoriaSeleccionada = -1
                    state.frecuenciaSeleccionada = -1
                    state.zonaSeleccionada = state.pregunta
                }
            }
            state.showBad = false
            state.showGood = false
        },
        border = BorderStroke(1.dp, Color.Blue),
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ){
        Text( text = "NUEVA PREGUNTA" )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    TitiTiriTheme {
        Principal()
    }
}