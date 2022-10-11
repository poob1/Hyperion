package com.hyperion.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.hyperion.R
import com.hyperion.domain.model.DomainChannel
import com.hyperion.ui.component.VideoCard
import com.hyperion.ui.navigation.AppDestination
import com.hyperion.ui.viewmodel.ChannelViewModel
import com.xinto.taxi.BackstackNavigator
import org.koin.androidx.compose.getViewModel

enum class ChannelTab(
    @StringRes
    val title: Int,
    val imageVector: ImageVector
) {
    HOME(R.string.home, Icons.Default.Home),
    VIDEOS(R.string.videos, Icons.Default.VideoLibrary),
    PLAYLISTS(R.string.playlists, Icons.Default.ViewList),
    ABOUT(R.string.about, Icons.Default.Info)
}

@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel = getViewModel(),
    navigator: BackstackNavigator<AppDestination>,
    channelId: String
) {
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getChannel(channelId)
    }

    when (state) {
        is ChannelViewModel.State.Loaded -> {
            ChannelScreenLoaded(
                navigator = navigator,
                channel = state.channel,
                onClickShare = viewModel::shareChannel
            )
        }
        ChannelViewModel.State.Loading -> {
            ChannelScreenLoading(
                onClickBack = navigator::pop
            )
        }
        is ChannelViewModel.State.Error -> {
            ChannelScreenError(
                error = state.error,
                onClickBack = navigator::pop
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreenLoaded(
    navigator: BackstackNavigator<AppDestination>,
    channel: DomainChannel,
    onClickShare: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(channel.name) },
                navigationIcon = {
                    IconButton(onClick = navigator::pop) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onClickShare) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        var selectedTab by rememberSaveable { mutableStateOf(ChannelTab.HOME) }

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (channel.banner != null) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .clip(MaterialTheme.shapes.medium),
                            model = channel.banner,
                            loading = {
                                val localElevation = LocalAbsoluteTonalElevation.current

                                Box(
                                    modifier = Modifier
                                        .placeholder(
                                            visible = true,
                                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(localElevation + 2.dp),
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = MaterialTheme.colorScheme.surfaceColorAtElevation(localElevation + 3.dp)
                                            )
                                        )
                                        .fillMaxSize(),
                                )
                            },
                            success = {
                                SubcomposeAsyncImageContent()
                            },
                            contentDescription = channel.name
                        )
                    }

                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(52.dp),
                            model = channel.avatar,
                            contentDescription = channel.name
                        )

                        Column(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Text(
                                text = channel.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (channel.subscriberText != null) {
                                Text(
                                    text = channel.subscriberText,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        FilledTonalButton(
                            enabled = false,
                            onClick = { /* TODO */ }
                        ) {
                            Text(stringResource(R.string.subscribe))
                        }
                    }
                }
            }

            stickyHeader {
                TabHeader(
                    selectedTab = selectedTab,
                    onClick = { selectedTab = it }
                )
            }

            when (selectedTab) {
                ChannelTab.HOME -> {
                    items(channel.videos) { video ->
                        VideoCard(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            video = video,
                            onClick = { navigator.push(AppDestination.Player(video.id)) }
                        )
                    }
                }

                ChannelTab.VIDEOS -> {}
                ChannelTab.PLAYLISTS -> {}
                ChannelTab.ABOUT -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreenLoading(
    onClickBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = { }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreenError(
    error: Exception,
    onClickBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = { Text(stringResource(R.string.error)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Default.Error,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(R.string.error)
            )

            Text(
                text = stringResource(R.string.error_occurred),
                style = MaterialTheme.typography.titleMedium
            )

            error.localizedMessage?.let { message ->
                SelectionContainer {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TabHeader(
    selectedTab: ChannelTab,
    onClick: (ChannelTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        tabs = {
            ChannelTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    text = { Text(stringResource(tab.title)) },
                    icon = { Icon(tab.imageVector, null) },
                    onClick = { onClick(tab) }
                )
            }
        }
    )
}