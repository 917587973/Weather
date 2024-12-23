package com.avinash.weather.ui.weather

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.avinash.weather.ui.weather.components.Animation
import com.avinash.weather.utils.PreferencesHelper
import com.avinash.weather.ui.weather.WeatherUiState as WeatherUiState
import com.avinash.weather.R

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val searchWidgetState by viewModel.searchWidgetState
    val searchTextState by viewModel.searchTextState
    val uiState: WeatherUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                WeatherScreenContent(uiState = uiState, modifier = modifier, viewModel = viewModel,  searchWidgetState = searchWidgetState,
                    searchTextState = searchTextState,
                    onTextChange = { viewModel.updateSearchTextState(it) },
                    onCloseClicked = { viewModel.updateSearchWidgetState(SearchWidgetState.CLOSED) },
                    onSearchClicked = { viewModel.getWeather(it) },
                    onSearchTriggered = {
                        viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                    })
            }
        },
    )
}

@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel?,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
)

{

    when {
        uiState.isLoading -> {
            Animation(modifier = Modifier.fillMaxSize(), animation = R.raw.animation_loading)
        }

        uiState.errorMessage.isNotEmpty() -> {
            WeatherErrorState(uiState = uiState, viewModel = viewModel)
        }

        else -> {
            WeatherApp(modifier = modifier, uiState = uiState, searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = { viewModel!!.updateSearchTextState(it) },
                onCloseClicked = { viewModel!!.updateSearchWidgetState(SearchWidgetState.CLOSED) },
                onSearchClicked = { viewModel!!.getWeather(it) },
                onSearchTriggered = {
                    viewModel!!.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                })
       }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    modifier: Modifier = Modifier,
    uiState: WeatherUiState,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var cardClicked by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Location", color = Color(0xFFC4C4C4)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0),
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),

                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                            if (searchQuery.isNotEmpty()) {
                                selectedCity = searchQuery
                                onSearchClicked(searchQuery)
                                cardClicked = false

                        }
                    }

                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                Animation(
                    modifier = Modifier.fillMaxSize(),
                    animation = R.raw.animation_loading
                )
            } else if (uiState.weather != null && !cardClicked) {
                WeatherCard(
                    onClick = { cardClicked = true },
                    modifier = modifier,
                    uiState = uiState,
                    onSearchClicked
                )
                preferencesHelper.saveCity(uiState.weather.name)

            } else if (uiState.errorMessage.isNotEmpty() || uiState.weather == null) {
                NoCitySelectedMessage()
            } else if (cardClicked) {
                DetailedWeatherView(context, uiState)
            }
        }
    }
}




@Composable
private fun WeatherErrorState(
    modifier: Modifier = Modifier,
    uiState: WeatherUiState,
    viewModel: WeatherViewModel?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Animation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f),
            animation = R.raw.animation_error,
        )

        Button(onClick = { viewModel?.getWeather() }) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Retry",
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(R.string.retry),
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            modifier = modifier
                .weight(2f)
                .alpha(0.5f)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = "Something went wrong: ${uiState.errorMessage}",
            textAlign = TextAlign.Center,
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon),
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
)
{
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = MaterialTheme.colorScheme.primary,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = stringResource(R.string.search_hint),
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(0.7f),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon),
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_icon),
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                    keyboardController?.hide()
                },
            ),
        )
    }
}


@Composable
@Preview
fun DefaultAppBarPreview() {
    DefaultAppBar(onSearchClicked = {})
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Search for a city",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}

@Composable
fun WeatherCard(
    onClick: () -> Unit,
    modifier: Modifier,
    uiState: WeatherUiState,
    onSearchClicked: (String) -> Unit,

) {
    val preferencesHelper = PreferencesHelper(LocalContext.current)
    val savedCity = preferencesHelper.getCity()
    val context = LocalContext.current
    var showDetailedWeather by remember { mutableStateOf(false) }



    if (showDetailedWeather) {
        DetailedWeatherView(
            context = context,
            uiState
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 540.dp)
                .clickable {
                    showDetailedWeather = true

                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (savedCity.isNullOrEmpty()){
                        Text(
                            text = uiState.weather!!.name.orEmpty(),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = uiState.weather?.temperature?.let { "$it°" } ?: "N/A",
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            )
                        preferencesHelper.saveCity(uiState.weather.name)

                    }else if (!savedCity.isNullOrEmpty()){
                        Text(
                            text = uiState.weather!!.name.orEmpty() ,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = uiState.weather?.temperature?.let { "$it°" } ?: "N/A",
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            )
                        preferencesHelper.saveCity(uiState.weather.name)

                    }

                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_uv),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(104.dp),
                    tint = Color.Unspecified
                )

            }
        }
    }
}




@Composable
fun DetailedWeatherView(
    context: Context,
    uiState: WeatherUiState
) {
    val preferencesHelper = PreferencesHelper(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {  }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_uv),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(180.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.weather!!.name.orEmpty(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = uiState.weather?.temperature?.let { "$it°" } ?: "N/A",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherDetailItem(label = "Humidity", value = uiState.weather.humidity.toString())
                    WeatherDetailItem(label = "UV", value = uiState.weather.uv.toString())
                    WeatherDetailItem(label = "Feels Like", value = uiState.weather.feelsLike.toString())
                }
            }
        }
    }



}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}


@Composable
fun NoCitySelectedMessage() {
    val preferencesHelper = PreferencesHelper(LocalContext.current)
    val savedCity = preferencesHelper.getCity()

    if (savedCity.isNullOrEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 40.dp)
        ) {
            Text(
                text = "No City Selected",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please Search For A City",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

