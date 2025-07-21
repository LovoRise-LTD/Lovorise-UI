package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_languages
import coinui.composeapp.generated.resources.age_range
import coinui.composeapp.generated.resources.dating_preference
import coinui.composeapp.generated.resources.discovery
import coinui.composeapp.generated.resources.distance_preference
import coinui.composeapp.generated.resources.interests
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.preferences
import coinui.composeapp.generated.resources.show_distance_in
import coinui.composeapp.generated.resources.show_me
import coinui.composeapp.generated.resources.we_will_show_you_who_match_your_vibe
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreenModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.language.LanguageScreenModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.domain.repo.ProfilePrefsUpdateItem
import com.lovorise.app.profile.domain.repo.ProfileUpdateItem
import com.lovorise.app.profile.domain.repo.UpdateProfileData
import com.lovorise.app.profile.presentation.edit_profile.DistanceMeasurement
import com.lovorise.app.profile.presentation.edit_profile.ProfileBottomSheetType
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenModel
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.components.EditProfileItem
import com.lovorise.app.profile.presentation.edit_profile.components.EditProfileItemWithSlider
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateDatingPrefsSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateInterestsBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateLanguagesSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateShowMeBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.settings.presentation.components.TextWithBackground
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

class PreferencesScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val languageScreenModel = rememberScreenModel { LanguageScreenModel() }
        val interestsScreenModel = rememberScreenModel { ChooseInterestsScreenModel() }
        val editProfileScreenModel = navigator.koinNavigatorScreenModel<EditProfileScreenModel>()
        val editProfileScreenState by editProfileScreenModel.state.collectAsState()
        val context = LocalPlatformContext.current

        val monthRes = stringArrayResource(Res.array.months)
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()

        LaunchedEffect(true) {
            editProfileScreenModel.loadDataFromApi(accountsState, monthRes)
        }


        DisposableEffect(Unit) {
            onDispose {
                editProfileScreenModel.updateAgeRangeAndDistanceWithMeasurements(context)
             //   editProfileScreenModel.updateLocation(context)
            }
        }


        PreferencesScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            languageScreenModel = languageScreenModel,
            interestsScreenModel = interestsScreenModel,
            state = editProfileScreenState,
            screenModel = editProfileScreenModel,
            accountsViewModel = accountsViewModel
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreenContent(isDarkMode:Boolean,goBack:()->Unit,interestsScreenModel: ChooseInterestsScreenModel,languageScreenModel:LanguageScreenModel,state: EditProfileScreenState,screenModel: EditProfileScreenModel,accountsViewModel: AccountsViewModel) {


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scope = rememberCoroutineScope()

    val languages = stringArrayResource(Res.array.languages)

    val context = LocalPlatformContext.current

    LaunchedEffect(true) {
        languageScreenModel.loadLanguages(languages)
    }

    Column(modifier = Modifier) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.preferences), onBack = goBack, isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Box(Modifier.height(40.dp).fillMaxWidth().background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)), contentAlignment = Alignment.CenterStart){

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.discovery),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    )

                }

                EditProfileItem(
                    title = stringResource(Res.string.dating_preference),
                    statusVisible = true,
                    description = state.datingPrefs.selectedItems.let { if (it.isNotEmpty()) it.joinToString(" ,") else "Choose one option that feels right for you." },
                    onClick = {
                        screenModel.openSheet(ProfileBottomSheetType.UpdateDatingPrefs)
                    },
                    showIndicator = false,
                    isDarkMode = isDarkMode
                )

                EditProfileItem(
                    title = stringResource(Res.string.show_me),
                    statusVisible = state.showMe.isVisible,
                    description = state.showMe.selectedItems.let { if (it.isNotEmpty()) it.joinToString(" ,") else "" },
                    onClick = {
                        screenModel.openSheet(ProfileBottomSheetType.UpdateShowMe)
                    },
                    isDarkMode = isDarkMode
                )

                EditProfileItem(
                    title = stringResource(Res.string.interests),
                    statusVisible = true,
                    description = state.interests.joinToString(", "),
                    onClick = {
                        screenModel.openSheet(ProfileBottomSheetType.UpdateInterests)
                    },
                    isDarkMode = isDarkMode
                )

                ShowInDistance(
                    selectedDistance = state.distanceMeasurement,
                    onClick = screenModel::updateDistanceMeasurement,
                    isDarkMode = isDarkMode
                )



                if (state.distanceMeasurement == DistanceMeasurement.KM) {
                    EditProfileItemWithSlider(
                        title = stringResource(Res.string.distance_preference),
                        description = "${if (state.distanceMeasurement == DistanceMeasurement.KM) state.distancePref.roundToInt() else (state.distancePref).roundToInt()} ${state.distanceMeasurement.stringValue}",
                        sliderValue = if (state.distanceMeasurement == DistanceMeasurement.KM) state.distancePref else (state.distancePref),
                        valueRange = 1f..100f,
                        onValueChangeSingleThumb = { newValue ->
                            // Update distancePrefKm based on selected unit
                            if ((state.distanceMeasurement == DistanceMeasurement.KM && newValue in 1f..100f)) {
                                val value = if (state.distanceMeasurement == DistanceMeasurement.KM) {
                                    newValue // Km, directly assign the new value
                                } else {
                                    newValue * 1.609f // Convert miles back to Km
                                }
                                screenModel.updateDistance(value)
                            }
                        },
                        isDarkMode = isDarkMode
                    )
                }else{
                    EditProfileItemWithSlider(
                        title = stringResource(Res.string.distance_preference),
                        description = "${if (state.distanceMeasurement == DistanceMeasurement.KM) state.distancePref.roundToInt() else (state.distancePref * 0.621).roundToInt()} ${state.distanceMeasurement.stringValue}",
                        sliderValue = if (state.distanceMeasurement == DistanceMeasurement.KM) state.distancePref else (state.distancePref * 0.621f),
                        valueRange = 0f..62.14f,
                        onValueChangeSingleThumb = { newValue ->
                            // Update distancePrefKm based on selected unit
                            if ((state.distanceMeasurement == DistanceMeasurement.MILES && newValue in 0f..62.14f)) {
                                val value = if (state.distanceMeasurement == DistanceMeasurement.KM) {
                                    newValue // Km, directly assign the new value
                                } else {
                                    newValue * 1.609f // Convert miles back to Km
                                }
                                screenModel.updateDistance(value)
                            }
                        },
                        isDarkMode = isDarkMode
                    )
                }

                EditProfileItemWithSlider(
                    title = stringResource(Res.string.age_range),
                    valueRange = 18f..65f,
                    description = "${state.ageRange.start}-${if (state.ageRange.end<65) "${state.ageRange.end}" else "65+"}",
                    isSingleThumbSlider = false,
                    onValueChangeDoubleThumb = { start, end ->
                        screenModel.updateAgeRange(EditProfileScreenState.AgeRange(start.toInt(),end.toInt()))
                    },
                    isDarkMode = isDarkMode
                )

                EditProfileItem(
                    title = stringResource(Res.string.languages),
                    statusVisible = state.languages.isVisible,
                    description = if (state.languages.languages.isNotEmpty()) state.languages.languages.joinToString(", ") else {stringResource(Res.string.add_languages)},
                    showIndicator = state.languages.languages.isEmpty(),
                    onClick = {
                        screenModel.openSheet(ProfileBottomSheetType.UpdateLanguages)
                    },
                    isDarkMode = isDarkMode
                )

                TextWithBackground(
                    text = stringResource(Res.string.we_will_show_you_who_match_your_vibe),
                    verticalPadding = 10.dp,
                    bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9),
                    textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )

                Box(Modifier.fillMaxSize().weight(1f).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))

            }


        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }

    if (state.isLoading) {
        CircularLoader(true)
    }


    if (state.showBottomSheet){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                screenModel.hideSheet()
            },

            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            when (state.editProfileBottomSheetType) {
                is ProfileBottomSheetType.UpdateDatingPrefs ->{
                    UpdateDatingPrefsSheetContent(
                        onSave = { items -> sheetState.hideWithCompletion(scope) {

                            screenModel.hideSheet()
                            screenModel.updateDatingPrefSelections(items)
                            accountsViewModel.state.value.user?.copy(typeOfRelation = items)?.let { accountsViewModel.updateUserRes(it) }
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.TYPE_OF_RELATION,
                                data = UpdateProfileData(false,items),
                                context = context
                            )
                        }},
                        datingPrefs = state.datingPrefs,
                        isDarkMode = isDarkMode,
                        onChangeSelection = screenModel::updateDatingPrefSelections,
                    )

                }

                is ProfileBottomSheetType.UpdateShowMe ->{
                    UpdateShowMeBottomSheetContent(
                        data = state.showMe,
                        isDarkMode = isDarkMode,
                        updateShowMe = screenModel::updateShowMe,
                        onSave = { items -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            accountsViewModel.state.value.user?.let { accountsViewModel.updateUserRes(it.copy(likeToMeet = items)) }
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.WHO_WOULD_YOU_LIKE_TO_MEET,
                                data = UpdateProfileData(state.showMe.isVisible,items),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdateInterests ->{
                    UpdateInterestsBottomSheetContent(
                        isDarkMode = isDarkMode,
                        screenModel = interestsScreenModel,
                        onSave = { interests-> sheetState.hideWithCompletion(scope) {
                            screenModel.updateInterests(interests)
                            screenModel.hideSheet()
                            val data = interestsScreenModel.getInterestsData(interests)
                            accountsViewModel.state.value.user?.let { accountsViewModel.updateUserRes(it.copy(interests = data)) }
                            screenModel.updateUserInterests(
                                data = data,
                                context = context
                            )
                        }},
                        selectedItems = state.interests
                    )
                }



                is ProfileBottomSheetType.UpdateLanguages -> {
                    UpdateLanguagesSheetContent(
                        languages = state.languages,
                        updateLanguage = screenModel::updateLanguages,
                        isDarkMode = isDarkMode,
                        onSave = {languages-> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            accountsViewModel.state.value.user?.let { accountsViewModel.updateUserRes(it.copy(language = languages)) }
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.LANGUAGE,
                                data = UpdateProfileData(state.languages.isVisible,languages),
                                context = context
                            )
                        }},
                        screenModel = languageScreenModel
                    )
                }

                else -> {

                }


            }



        }
    }


}

@Composable
fun ShowInDistance(selectedDistance: DistanceMeasurement,onClick: (DistanceMeasurement)->Unit,isDarkMode: Boolean) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0)
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            // Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.show_distance_in),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(8.dp))

            // Description
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (selectedDistance == DistanceMeasurement.KM) "Kilometre" else "Mile",
                    maxLines = 3,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 13.sp,
                    lineHeight = 19.5.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row (
                    modifier = Modifier.width(112.dp).height(35.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffEBECF0), RoundedCornerShape(16.dp))
                ){

                    DistanceMetricsChip(text = "km", isSelected = selectedDistance == DistanceMeasurement.KM, onClick = {onClick(DistanceMeasurement.KM)}, isDarkMode = isDarkMode)
                    DistanceMetricsChip(text = "mi", isSelected = selectedDistance == DistanceMeasurement.MILES, onClick = {onClick(DistanceMeasurement.MILES)}, isDarkMode = isDarkMode)


                }

            }
        }

        Spacer(Modifier.height(16.dp))
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(),
//            thickness = 1.dp,
//            color = Color(0xffEAECF0)
//        )
    }

}

@Composable
fun DistanceMetricsChip(text:String,isSelected:Boolean,onClick:()->Unit,isDarkMode: Boolean) {

    Box(
        Modifier.size(width = 56.dp, height = 35.dp)
            .then(if (isSelected) Modifier.background(Color(0xffF33358), RoundedCornerShape(16.dp)) else if (isDarkMode && !isSelected) Modifier.background(CARD_BG_DARK, RoundedCornerShape(16.dp))  else Modifier)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else {
                if (isDarkMode) Color.White else Color(0xff344054)
            },
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium
        )
    }

    
}