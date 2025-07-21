package com.lovorise.app.filters

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import coinui.composeapp.generated.resources.access_essential_filters_to_find_ideal_match
import coinui.composeapp.generated.resources.add_this_filter
import coinui.composeapp.generated.resources.advanced
import coinui.composeapp.generated.resources.advanced_filters_for_more_specific_results
import coinui.composeapp.generated.resources.age_range
import coinui.composeapp.generated.resources.apply
import coinui.composeapp.generated.resources.basic
import coinui.composeapp.generated.resources.current_location
import coinui.composeapp.generated.resources.dating_preference
import coinui.composeapp.generated.resources.dating_preferences
import coinui.composeapp.generated.resources.distance_preference
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.education_level
import coinui.composeapp.generated.resources.education_levels
import coinui.composeapp.generated.resources.family_plans
import coinui.composeapp.generated.resources.filters
import coinui.composeapp.generated.resources.height
import coinui.composeapp.generated.resources.ic_chev_down
import coinui.composeapp.generated.resources.ic_chev_up
import coinui.composeapp.generated.resources.ic_locked
import coinui.composeapp.generated.resources.ic_premium_filter
import coinui.composeapp.generated.resources.interests
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.location
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.profile_status
import coinui.composeapp.generated.resources.profile_status_items
import coinui.composeapp.generated.resources.religion
import coinui.composeapp.generated.resources.religions
import coinui.composeapp.generated.resources.reset
import coinui.composeapp.generated.resources.show_me
import coinui.composeapp.generated.resources.show_verified_profile_only
import coinui.composeapp.generated.resources.sort_by
import coinui.composeapp.generated.resources.sort_by_options
import coinui.composeapp.generated.resources.verification
import coinui.composeapp.generated.resources.who_would_you_like_to_meet_options
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreenModel
import com.lovorise.app.accounts.presentation.signup.language.LanguageScreenModel
import com.lovorise.app.accounts.presentation.signup.meeting_prefs.OpenToEveryOne
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.ProfileScreensState
import com.lovorise.app.profile.presentation.PurchaseSubscriptionScreen
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenModel
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.ProfileBottomSheetType
import com.lovorise.app.profile.presentation.edit_profile.components.EditProfileItemWithSlider
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateDatingPrefsSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateEducationLevelSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateFamilyPlanningSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateInterestsBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateLanguagesSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateReligionSheetContent
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.profile_visitors.components.CustomTabs
import com.lovorise.app.settings.presentation.components.CustomSwitch
import com.lovorise.app.settings.presentation.components.LocationLoader
import com.lovorise.app.settings.presentation.components.TitleText
import com.lovorise.app.settings.presentation.components.UpdateSortBySheetContent
import com.lovorise.app.swipe.presentation.SwipeScreenModel
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class FilterScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val screenModel = rememberScreenModel { FiltersScreenModel() }

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()

        val profileScreenState by profileScreenModel.state.collectAsState()

        val languageScreenModel = rememberScreenModel { LanguageScreenModel() }
        val chooseInterestsScreenModel = rememberScreenModel { ChooseInterestsScreenModel() }
        val editProfileScreenModel = navigator.koinNavigatorScreenModel<EditProfileScreenModel>()
        val swipeScreenModel = navigator.koinNavigatorScreenModel<SwipeScreenModel>()

        val editProfileScreenState by editProfileScreenModel.state.collectAsState()
        val context = LocalPlatformContext.current

        val monthRes = stringArrayResource(Res.array.months)
        val accountsState by accountsViewModel.state.collectAsState()

        LaunchedEffect(accountsState.user) {
            screenModel.loadDataFromAccountsState(accountsState)
          //  editProfileScreenModel.loadDataFromApi(accountsState, monthRes)
        }


        DisposableEffect(Unit) {
            onDispose {
              //  editProfileScreenModel.updateAgeRangeAndDistancePrefsIfRequired(context)
                //   editProfileScreenModel.updateLocation(context)
            }
        }


//        val reelsScreenModel = navigator.koinNavigatorScreenModel<ReelsScreenModel>()




        FilterScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            languageScreenModel = languageScreenModel,
            navigateToPurchaseSubscription = {
                navigator.push(PurchaseSubscriptionScreen(
                    //navigator = navigator,
                    subscriptionType = SubscriptionType.WEEKLY
                ))
            },
            profileScreenState = profileScreenState,
            //state = editProfileScreenState,
            editProfileScreenModel1 = editProfileScreenModel,
            accountsViewModel = accountsViewModel,
            screenModel = screenModel,
            interestsScreenModel = chooseInterestsScreenModel,
            swipeScreenModel = swipeScreenModel
//            editProfileScreenModel1 =
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenContent(isDarkMode:Boolean,goBack:()->Unit,screenModel: FiltersScreenModel,languageScreenModel: LanguageScreenModel,navigateToPurchaseSubscription:()->Unit,profileScreenState: ProfileScreensState,editProfileScreenModel1: EditProfileScreenModel,accountsViewModel: AccountsViewModel,interestsScreenModel:ChooseInterestsScreenModel,swipeScreenModel:SwipeScreenModel) {

//    var showVerifiedProfile by remember{ mutableStateOf(EditProfileScreenState.ShowVerifiedProfile()) }
//    var sortBy by remember{ mutableStateOf(EditProfileScreenState.SortBy()) }

    val isLocked = remember(profileScreenState.subscriptionType) { profileScreenState.subscriptionType == SubscriptionType.FREE}

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

//    var isShowMeHidden by remember { mutableStateOf(false) }



    val accountsState by accountsViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val languagesList = stringArrayResource(Res.array.languages)
    val educationLevels = stringArrayResource(Res.array.education_levels)
    val monthRes = stringArrayResource(Res.array.months)
    val religions = stringArrayResource(Res.array.religions)


    val showMeItems = stringArrayResource(Res.array.who_would_you_like_to_meet_options)


    val context = LocalPlatformContext.current

    val tabs = listOf(
        stringResource(Res.string.basic),
        stringResource(Res.string.advanced)
    )

    val profileStatusItems = stringArrayResource(Res.array.profile_status_items)
    val sortByOptions = stringArrayResource(Res.array.sort_by_options)
    val datingPrefs = stringArrayResource(Res.array.dating_preferences)

//    var tabIndex by rememberSaveable{ mutableIntStateOf(0) }

    val pagerState = rememberPagerState { 2 }

    val state by screenModel.state.collectAsState()




    LaunchedEffect(true) {
        languageScreenModel.loadLanguages(languagesList)
      //  editProfileScreenModel.loadDataFromApi(accountsViewModel.state.value, monthRes)
    }

    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {

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

            HeaderWithTitleAndBack(title = stringResource(Res.string.filters), onBack = goBack, addShadow = true, isDarkMode = isDarkMode)

            Spacer(Modifier.height(13.dp))

            CustomTabs(
                modifier = Modifier.padding(horizontal = 16.dp),
                activeIndex = pagerState.currentPage,
                tabs = tabs,
                onTabSelected = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                isDarkMode = isDarkMode
            )



            HorizontalPager(
                state = pagerState
            ){ page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                   //     .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(13.dp))

                    FilterDescriptionSection(
                        text = stringResource(if(page == 0) Res.string.access_essential_filters_to_find_ideal_match else Res.string.advanced_filters_for_more_specific_results),
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.height(8.dp))

                    CurrentLocationSection(onClick = {}, location = state.locationData ?: accountsState.currentLocation?.let { LocationData(latitude = it.latitude, longitude = it.longitude, city = it.city, country = it.country) }, isDarkMode = isDarkMode)


                    EditProfileItemWithSlider(
                        addDividerAtTop = false,
                        titleColor = Color(0xff141313),
                        title = stringResource(Res.string.distance_preference),
                        description = "${state.distancePrefs.toInt()}${if (state.distancePrefs.toInt() >= 100) "+" else ""} km",
                        sliderValue = if (state.distancePrefs.toFloat() > 100) 100f else state.distancePrefs.toFloat(),
                        valueRange = 1f..100f,
                        onValueChangeSingleThumb = screenModel::updateDistancePrefs,
                        isDarkMode = isDarkMode
                    )

                    EditProfileItemWithSlider(
                        addDividerAtTop = false,
                        titleColor = Color(0xff141313),
                        title = stringResource(Res.string.age_range),
                        valueRange = 18f..65f,
                        doubleSliderValue = Pair(state.ageRange.start,state.ageRange.endInclusive),
                        description = "${state.ageRange.start.toInt()}-${if (state.ageRange.endInclusive<65) "${state.ageRange.endInclusive.toInt()}" else "65+"}",
                        isSingleThumbSlider = false,
                        onValueChangeDoubleThumb = { start, end ->
                            screenModel.updateAgeRange(start..end)
                        },
                        isDarkMode = isDarkMode
                    )

                    if (page == 1){
                        EditProfileItemWithSlider(
                            addDividerAtTop = false,
                            titleColor = Color(0xff141313),
                            title = stringResource(Res.string.height),
                            valueRange = 109f..213f,
                            description = if(state.heightRange == 109f..213f) "Any height" else "${state.heightRange.start.toInt()}cm to ${state.heightRange.endInclusive.toInt()}cm",
                            isSingleThumbSlider = false,
                            onValueChangeDoubleThumb = { start, end ->
                                screenModel.updateHeightRange(start..end)
                            },
                            doubleSliderValue = Pair(state.heightRange.start,state.heightRange.endInclusive),
                            isLocked = isLocked,
                            icon = if (isLocked) Res.drawable.ic_locked else Res.drawable.ic_premium_filter,
                            isDarkMode = isDarkMode
                        )
                    }

                    FilterItem(
                        title = stringResource(Res.string.dating_preference),
                        description = state.datingPrefs.let { if (it.isEmpty()) stringResource(Res.string.add_this_filter) else state.datingPrefs.joinToString(",") },
                        onClick = {
                            screenModel.openSheet(ProfileBottomSheetType.UpdateDatingPrefs)
                        },
                        isDarkMode = isDarkMode
                    )


                    HideShowChooser(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(Res.string.show_me),
                        onChange = { value->
                            var selectedItems = state.showMe.data.toMutableList()
                            val isSelected = selectedItems.contains(value)

                            selectedItems = if (isSelected && selectedItems.size > 1) {
                                selectedItems.toMutableList().apply {
                                    remove(value)
                                }
                            }else{
                                selectedItems.toMutableList().apply {
                                    if (value == "All"){
                                        removeAll(selectedItems)
                                        add(value)
                                    }else{
                                        remove("All")
                                        if (selectedItems.size < 2){
                                            add(value)
                                        }
                                    }
                                }
                            }
                            screenModel.updateShowMe(state.showMe.copy(data = selectedItems))
                        },
                        onVisibilityChange = {
                            screenModel.updateShowMe(state.showMe.copy(isExpanded = !state.showMe.isExpanded))
                        },
                        isPremium = false,
                        isLocked = false,
                        items = showMeItems.filter { it != "All" },
                        selectedValues = state.showMe.data,
                        isHidden = !state.showMe.isExpanded,
                        isDarkMode = isDarkMode,
                        enableShowEveryoneSwitch = true,
                        isEveryoneSelected = state.showMe.data.contains("All"),
                    )
                    Spacer(Modifier.height(8.dp))

                    HideShowChooser(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(Res.string.profile_status),
                        onChange = { value->
                            screenModel.updateProfileStatus(state.profileStatus.copy(data = value))
                        },
                        onVisibilityChange = {
                            screenModel.updateProfileStatus(state.profileStatus.copy(isExpanded = !state.profileStatus.isExpanded))
                        },
                        isPremium = false,
                        isLocked = false,
                        items = profileStatusItems,
                        selectedValues = listOf(state.profileStatus.data),
                        isHidden = !state.profileStatus.isExpanded,
                        isDarkMode = isDarkMode
                    )



                    if (page == 1) {
                        Spacer(Modifier.height(8.dp))

                        HideShowChooser(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            title = stringResource(Res.string.sort_by),
                            onChange = { value ->
                                screenModel.updateSortBy(state.sortBy.copy(data = value))
                            },
                            onVisibilityChange = {
                                screenModel.updateSortBy(state.sortBy.copy(isExpanded = !state.sortBy.isExpanded))
                            },
                            isPremium = true,
                            isLocked = isLocked,
                            items = sortByOptions,
                            isHidden = !state.sortBy.isExpanded,
                            selectedValues = listOf(state.sortBy.data),
                            isDarkMode = isDarkMode
                        )



                        FilterItem(
                            title = stringResource(Res.string.verification),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else stringResource(
                                Res.string.show_verified_profile_only
                            ),
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                }
                            },
                            isVisible = state.showVerifiedOnly,
                            isLocked = isLocked,
                            onVisibilityChange = screenModel::toggleShowVerifiedOnly,
                            showSwitch = true,
                            isPremium = true,
                            isDarkMode = isDarkMode
                        )

                        FilterItem(
                            title = stringResource(Res.string.interests),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else state.interests.joinToString(", ").ifEmpty { stringResource(Res.string.add_this_filter) } ,
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                } else {
                                    screenModel.openSheet(ProfileBottomSheetType.UpdateInterests)
                                }
                            },
                            isLocked = isLocked,
                            isPremium = true,
                            isDarkMode = isDarkMode
                        )

                        FilterItem(
                            title = stringResource(Res.string.religion),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else state.religion.ifBlank { stringResource(Res.string.add_this_filter) },
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                } else {
                                    screenModel.openSheet(ProfileBottomSheetType.UpdateReligion)
                                }
                            },
                            isLocked = isLocked,
                            isPremium = true,
                            isDarkMode = isDarkMode
                        )

                        FilterItem(
                            title = stringResource(Res.string.family_plans),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else state.familyPlanning.ifBlank { stringResource(Res.string.add_this_filter) },
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                } else {
                                    screenModel.openSheet(ProfileBottomSheetType.UpdateFamilyPlans)
                                }
                            },
                            isLocked = isLocked,
                            isPremium = true,
                            isDarkMode = isDarkMode
                        )

                        FilterItem(
                            title = stringResource(Res.string.education_level),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else state.education.ifBlank { stringResource(Res.string.add_this_filter) },
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                } else {
                                    screenModel.openSheet(ProfileBottomSheetType.UpdateEducationLevel)
                                }
                            },
                            isPremium = true,
                            isLocked = isLocked,
                            isDarkMode = isDarkMode
                        )

                        FilterItem(
                            title = stringResource(Res.string.languages),
                            description = if (isLocked) stringResource(Res.string.add_this_filter) else state.languages.joinToString(", ").ifBlank {  stringResource(Res.string.add_this_filter)  },
                            onClick = {
                                if (isLocked) {
                                    navigateToPurchaseSubscription()
                                } else {
                                    screenModel.openSheet(ProfileBottomSheetType.UpdateLanguages)
                                }
                            },
                            isPremium = true,
                            isLocked = isLocked,
                            isDarkMode = isDarkMode
                        )
                    }

                 //   Box(Modifier.fillMaxSize().weight(1f).background(Color(0xffF3F5F9)))

                }
            }


        }

        Spacer(Modifier.height(14.dp))
        ButtonWithText(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            textColor = Color.White,
            text = stringResource(Res.string.apply),
            onClick = {
                swipeScreenModel.updateReloadState(true)
            },
            bgColor = Color(0xffF33358)
        )
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Text(
                text = stringResource(Res.string.reset),
                color = if (isDarkMode) Color(0xffBEC1C6) else Color(0xff98A2B3),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp
            )
        }
        Spacer(Modifier.height(22.dp))

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


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

            when (state.sheetType) {



                is ProfileBottomSheetType.UpdateDatingPrefs ->{
                    UpdateDatingPrefsSheetContent(
                        onSave = { items -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateDatingPrefs(items)
                        }},
                        datingPrefs = state.datingPrefs.let { EditProfileScreenState.DatingPrefs(categories = datingPrefs, selectedItems = it) },
                        isDarkMode = isDarkMode,
                        onChangeSelection = {items->
                            screenModel.updateDatingPrefs(items)
                        }
                    )

                }

//                is EditProfileBottomSheetType.UpdateShowMe ->{
//                    UpdateShowMeBottomSheetContent(
//                        data = state.showMe,
//                        isDarkMode = isDarkMode,
//                        updateShowMe = editProfileScreenModel::updateShowMe,
//                        onSave = { index-> sheetState.hideWithCompletion(scope) {
//                            editProfileScreenModel.hideSheet()
//                            accountsViewModel.state.value.user?.let { accountsViewModel.updateUserRes(it.copy(likeToMeet = state.showMe.categories[index])) }
//                            editProfileScreenModel.updateUserPrefs(
//                                item = ProfilePrefsUpdateItem.WHO_WOULD_YOU_LIKE_TO_MEET,
//                                data = UpdateProfileData(state.showMe.isVisible,state.showMe.categories[index]),
//                                context = context
//                            )
//                        }},
//                    )
//                }

                is ProfileBottomSheetType.UpdateSortBy ->{
                    UpdateSortBySheetContent(
                        sortBy = state.sortBy.let { EditProfileScreenState.SortBy(items = sortByOptions) },
                        isDarkMode = isDarkMode,
                        updateSortBy = {
                            sortByOptions.getOrNull(it.selectedIndex)?.let {v-> screenModel.updateSortBy(state.sortBy.copy(data = v)) }
                        },
                        onSave = { sheetState.hideWithCompletion(scope) { screenModel.hideSheet()}},
                        buttonText = stringResource(Res.string.done)
                    )
                }

                is ProfileBottomSheetType.UpdateEducationLevel -> {
                    UpdateEducationLevelSheetContent(
                        isDarkMode = isDarkMode,
                        onSave = { index -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            educationLevels.getOrNull(index)?.let { screenModel.updateEducation(it) }
                        }},
                        education = state.education.let { EditProfileScreenState.EducationLevel(items = educationLevels) },
                        updateEducation = {
                            educationLevels.getOrNull(it.selectedIndex)?.let {v-> screenModel.updateEducation(v) }
                        }
                    )
                }

                is ProfileBottomSheetType.UpdateFamilyPlans -> {
                    UpdateFamilyPlanningSheetContent(
                        buttonText = stringResource(Res.string.done),
                        updateFamilyPlanning = {
                            it.items.getOrNull(it.selectedIndex)?.let{ plan-> screenModel.updateFamilyPlanning(plan) }
                        },
                        isDarkMode = isDarkMode,
                        onSave = { index -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                        }},
                        familyPlanning = state.familyPlanning.let { EditProfileScreenState.FamilyPlanning() }
                    )
                }

                is ProfileBottomSheetType.UpdateReligion -> {
                    UpdateReligionSheetContent(
                        buttonText = stringResource(Res.string.done),
                        updateReligion = {
                            religions.getOrNull(it.selectedIndex)?.let{v->screenModel.updateReligion(v)}
                        },
                        isDarkMode = isDarkMode,
                        onSave = {index -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            religions.getOrNull(index)?.let{v->screenModel.updateReligion(v)}
                        }},
                        religion = state.religion.let { EditProfileScreenState.Religion(selectedIndex = religions.indexOf(it)) }
                    )
                }


                is ProfileBottomSheetType.UpdateInterests ->{
                    UpdateInterestsBottomSheetContent(
                        isDarkMode = isDarkMode,
                        screenModel = interestsScreenModel,
                        onSave = { interests-> sheetState.hideWithCompletion(scope) {
                            screenModel.updateInterests(interests)
                            screenModel.hideSheet()
                        }},
                        selectedItems = state.interests
                    )
                }






                is ProfileBottomSheetType.UpdateLanguages -> {
                    UpdateLanguagesSheetContent(
                        languages =  state.languages.let { EditProfileScreenState.Languages(it) },
                        updateLanguage = { screenModel.updateLanguages(it.languages) },
                        isDarkMode = isDarkMode,
                        onSave = {languages-> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateLanguages(languages)
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
fun HideShowChooser(selectedValues:List<String>,items:List<String>,onChange:(String)->Unit,isHidden:Boolean,title:String,isPremium:Boolean,isLocked:Boolean,onVisibilityChange: () -> Unit,modifier: Modifier = Modifier,isDarkMode: Boolean,enableShowEveryoneSwitch:Boolean = false,isEveryoneSelected:Boolean = false,onToggleEveryoneSwitch:()->Unit = {}) {

    Column(modifier = modifier.fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth().height(52.dp).noRippleClickable(onVisibilityChange), verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier,
                text = title,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )
            if (isLocked){
                Spacer(Modifier.width(10.dp))
                Image(
                    imageVector = vectorResource(Res.drawable.ic_locked),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (isPremium && !isLocked){
                Spacer(Modifier.width(8.dp))
                Image(
                    imageVector = vectorResource(Res.drawable.ic_premium_filter),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = vectorResource(if (isHidden) Res.drawable.ic_chev_up else Res.drawable.ic_chev_down),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isDarkMode) Color.White else Color(0xff101828)
            )
        }

        if (!isHidden){
        if (enableShowEveryoneSwitch){
            OpenToEveryOne(
                isDarkMode = isDarkMode,
                isChecked = isEveryoneSelected,
                onCheckChanged = {onChange("All")}
            )
            Spacer(Modifier.height(6.dp))
        }

            items.forEachIndexed { _, value ->
                TextWithCheckbox(
                    modifier = Modifier.align(Alignment.Start),
                    text = value,
                    isChecked = selectedValues.any { it == value },
                    hideCheckBox = selectedValues.none { it == value },
                    onClick = {
                        onChange(value)
                    },
                    isDarkMode = isDarkMode
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider(isDarkMode = isDarkMode)
            }
        }




    }
    
}


@Composable
fun FilterDescriptionSection(text:String,isDarkMode: Boolean) {
    Spacer(Modifier.height(10.dp))

    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = text,
        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Normal
    )

    Spacer(Modifier.height(10.dp))
}

@Composable
fun CurrentLocationSection(onClick:()->Unit,location: LocationData?,isDarkMode: Boolean) {
    Spacer(Modifier.height(16.dp))

    Column(Modifier.padding(horizontal = 16.dp).noRippleClickable(onClick)) {
        TitleText(stringResource(Res.string.location), isDarkMode = isDarkMode)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.current_location),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )
            if (location != null && !location.city.isNullOrBlank() && !location.country.isNullOrBlank()) {
                Text(
                    text = "${location.city}, ${location.country}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = PRIMARY
                )

            }else{
                LocationLoader(Modifier)
            }
        }

    }

    Spacer(modifier = Modifier.height(20.dp))
}
@Composable
fun FilterItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit = {},
    isLocked:Boolean = false,
    onLockClick:()->Unit = {},
    isVisible:Boolean = true,
    onVisibilityChange:()->Unit = {},
    showSwitch:Boolean = false,
    isPremium: Boolean = false,
    isDarkMode: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onClick()
            }
    ) {

//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(),
//            thickness = 1.dp,
//            color = Color(0xffEAECF0)
//        )

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
               // horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = if (isDarkMode) Color.White else Color(0xff141313),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.width(8.dp))

                if (isLocked){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_locked),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (!isLocked && isPremium){
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_premium_filter),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }

            }

            Spacer(Modifier.height(8.dp))

            // Description
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = description,
                    maxLines = 3,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 13.sp,
                    lineHeight = 19.5.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.width(8.dp))


                if (!isLocked && showSwitch){
                    Box(Modifier.height(20.dp).width(36.dp)) {
                        CustomSwitch(
                            modifier = Modifier.fillMaxSize(),
                            isChecked = isVisible,
                            onCheckChanged = {
                                onVisibilityChange()
                            },
                            isDarkMode = isDarkMode
                        )
                    }
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


