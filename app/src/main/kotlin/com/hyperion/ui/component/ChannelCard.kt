package com.hyperion.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyperion.R
import com.hyperion.domain.model.DomainSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelCard(
    modifier: Modifier = Modifier,
    channel: DomainSearch.Result.Channel,
    onClick: () -> Unit,
    onSubscribe: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        onClick = onClick
    ) {
        Row(
            Modifier
                .heightIn(min = 70.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChannelThumbnail(
                modifier = Modifier.size(60.dp),
                url = channel.thumbnailUrl
            )

            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.labelLarge
                )

                if (channel.subscriptionsText != null) {
                    Text(
                        text = channel.subscriptionsText,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                if (channel.videoCountText != null) {
                    Text(
                        text = channel.videoCountText,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Button(onClick = onSubscribe) {
                Text(stringResource(R.string.subscribe))
            }
        }
    }
}