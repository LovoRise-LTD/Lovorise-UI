package com.lovorise.app.accounts.presentation.signup.profile_upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.ic_restore_crop
import com.attafitamim.krop.core.crop.CropState
import com.attafitamim.krop.core.crop.CropperStyle
import com.attafitamim.krop.core.crop.DefaultCropperStyle
import com.attafitamim.krop.core.crop.LocalCropperStyle
import com.attafitamim.krop.ui.CropperDialogProperties
import com.attafitamim.krop.ui.CropperPreview
import com.attafitamim.krop.ui.DefaultControls
import com.attafitamim.krop.ui.DefaultTopBar
import org.jetbrains.compose.resources.painterResource


@Composable
fun ImageCropperDialog1(
    state: CropState,
    style: CropperStyle = DefaultCropperStyle,
    dialogProperties: DialogProperties = CropperDialogProperties,
    dialogPadding: PaddingValues = PaddingValues(16.dp),
    dialogShape: Shape = RoundedCornerShape(8.dp),
    topBar: @Composable (CropState) -> Unit = { DefaultTopBar(it) },
    cropControls: @Composable BoxScope.(CropState) -> Unit = { DefaultControls(it) },
    isDarkMode:Boolean
) {
    LaunchedEffect(Unit) {
        state.setInitialState(style) // Could be buggy, since it is run in a separate thread
    }


    CompositionLocalProvider(LocalCropperStyle provides style) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(dialogPadding),
            shape = dialogShape,
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .background(Color.White)
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .fillMaxWidth()
                )
                TopAppBar(title = {},
                    navigationIcon = {
                        IconButton(onClick = { state.done(accept = false) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { state.reset() }) {
                            Icon(painter = painterResource(coinui.composeapp.generated.resources.Res.drawable.ic_restore_crop), null)
                        }
                        IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
                            Icon(Icons.Default.Done, null)
                        }
                    },
                    backgroundColor = Color.White,
                    elevation = 0.dp
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clipToBounds()
                ) {
                    CropperPreview(state = state, modifier = Modifier.fillMaxSize())

                }
                Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
                    cropControls(state)
                }
                Spacer(
                    modifier = Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .fillMaxWidth()
                        .background(if (isDarkMode) Color.Black else Color.White)
                )
            }
        }
    }
}