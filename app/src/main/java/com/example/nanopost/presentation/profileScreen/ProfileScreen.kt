package com.example.nanopost.presentation.profileScreen

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nanopost.R
import com.example.nanopost.presentation.component.CustomDivider
import com.example.nanopost.presentation.component.CustomTopBar
import com.example.nanopost.presentation.component.DarkButton
import com.example.nanopost.presentation.component.NoPhotoAvatar
import com.example.nanopost.presentation.theme.LocalExtendedColors

@Composable
fun ProfileScreen() {

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.profile),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.logout),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = paddingValues,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item() {
                UserInfoCard()
            }
            item() {
                ImagesCard()
            }
            items(items = emptyList<String>()) {

            }
        }

    }

}

@Composable
fun UserInfoCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            NoPhotoAvatar(
                "E", modifier = Modifier
                    .padding(end = 16.dp)
                    .size(64.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Text",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    "just do it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        CustomDivider()

        Row(modifier = Modifier.fillMaxWidth()) {
            InfoCard(value = "12", name = stringResource(R.string.images))

            InfoCard(value = "5", name = stringResource(R.string.subscribers))

            InfoCard(value = "16", name = stringResource(R.string.posts))
        }

        CustomDivider()

        DarkButton(
            onClick = {},
            text = stringResource(R.string.edit),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun InfoCard(value: String, name: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        )

        Text(
            name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        )
    }
}

@Composable
fun ImagesCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = LocalExtendedColors.current.surface1,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = LocalExtendedColors.current.surface1,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Text(stringResource(R.string.images))

            Spacer(modifier = Modifier.weight(1f))

            Icon(painter = painterResource(R.drawable.chevron_right), contentDescription = null)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {

        }
    }
}

@Composable
fun Image(uri: Uri) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}