package com.lovorise.app.profile.presentation.edit_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.add_bio
import coinui.composeapp.generated.resources.add_profession
import coinui.composeapp.generated.resources.age
import coinui.composeapp.generated.resources.age_range
import coinui.composeapp.generated.resources.bio
import coinui.composeapp.generated.resources.camera
import coinui.composeapp.generated.resources.camera_permissions_required
import coinui.composeapp.generated.resources.children_preferences
import coinui.composeapp.generated.resources.choose_option_that_feels_right
import coinui.composeapp.generated.resources.control_profile_privacy
import coinui.composeapp.generated.resources.dating_preference
import coinui.composeapp.generated.resources.distance_preference
import coinui.composeapp.generated.resources.drink_preferences
import coinui.composeapp.generated.resources.drinking
import coinui.composeapp.generated.resources.edit_profile
import coinui.composeapp.generated.resources.education_level
import coinui.composeapp.generated.resources.education_levels
import coinui.composeapp.generated.resources.family_plans
import coinui.composeapp.generated.resources.gender
import coinui.composeapp.generated.resources.height
import coinui.composeapp.generated.resources.hold_drag_to_reorder
import coinui.composeapp.generated.resources.ic_arrow_up
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import coinui.composeapp.generated.resources.ic_info
import coinui.composeapp.generated.resources.interests
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.location
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.name
import coinui.composeapp.generated.resources.open_to_everyone
import coinui.composeapp.generated.resources.permissions_in_your_settings
import coinui.composeapp.generated.resources.pets
import coinui.composeapp.generated.resources.photo
import coinui.composeapp.generated.resources.photo_permissions_required
import coinui.composeapp.generated.resources.please_grant
import coinui.composeapp.generated.resources.religion
import coinui.composeapp.generated.resources.religions
import coinui.composeapp.generated.resources.show_me
import coinui.composeapp.generated.resources.smoking
import coinui.composeapp.generated.resources.smoking_preferences
import coinui.composeapp.generated.resources.who_do_you_want_to_meet
import coinui.composeapp.generated.resources.your_profession
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreenModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.accounts.presentation.signup.language.LanguageScreenModel
import com.lovorise.app.accounts.presentation.signup.profile_upload.EditPickedMediaScreen
import com.lovorise.app.accounts.presentation.signup.profile_upload.GalleryAlbumScreen
import com.lovorise.app.accounts.presentation.signup.profile_upload.ProfileUploadScreenViewModel
import com.lovorise.app.accounts.presentation.signup.profile_upload.TopHeaderSection
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.AlertMessageDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ImageSourceOptionDialog
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ProgressIndicator
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.ReorderableLazyGrid
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.components.Toast
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions_compose.BindEffect
import com.lovorise.app.libs.permissions_compose.rememberPermissionsControllerFactory
import com.lovorise.app.noRippleClickable
import com.lovorise.app.photo_capture_pick.rememberCameraManager
import com.lovorise.app.profile.domain.repo.ProfilePrefsUpdateItem
import com.lovorise.app.profile.domain.repo.ProfileUpdateItem
import com.lovorise.app.profile.domain.repo.UpdateProfileData
import com.lovorise.app.profile.presentation.edit_profile.components.CompleteProfileCard
import com.lovorise.app.profile.presentation.edit_profile.components.EditProfileItem
import com.lovorise.app.profile.presentation.edit_profile.components.EditProfileItemWithSlider
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateAgeSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateBioSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateDatingPrefsSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateDrinkingSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateEducationLevelSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateFamilyPlanningSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateGenderSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateHeightSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateInterestsBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateLanguagesSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateNameSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdatePetSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateProfessionSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateReligionSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateShowMeBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.components.sheets_components.UpdateSmokingSheetContent
import com.lovorise.app.settings.presentation.components.CustomSwitch
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.WhoCanSeeMyProfileScreen
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

object EditProfileScreen: Screen {

    @Composable
    override fun Content() {

//        val accountsRepo = koinInject<AccountsRepo>()
//        val accountsViewModel = rememberScreenModel { AccountsViewModel(accountsRepo) }


        val navigator = LocalNavigator.currentOrThrow

        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()

        val context = LocalPlatformContext.current
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val profileUploadScreenModel = navigator.koinNavigatorScreenModel<ProfileUploadScreenViewModel>()


        LaunchedEffect(true){
            profileUploadScreenModel.onInit(controller, context)
//            if (accountsState.currentLocation == null && accountsState.user?.locationData == null){
//                val locationManager = LocationManager(context)
//                val currentLocation = locationManager.getCurrentLocation()
//                if (currentLocation != null) {
//                    accountsViewModel.updateCurrentLocation(currentLocation)
//                }
//            }
        }

        val interestsScreenModel = rememberScreenModel { ChooseInterestsScreenModel() }
        val languageScreenModel = rememberScreenModel { LanguageScreenModel() }
        val editProfileScreenModel = koinScreenModel<EditProfileScreenModel>()
        val profileScreenState by editProfileScreenModel.state.collectAsState()



        val smoking = stringArrayResource(Res.array.smoking_preferences)
        val religion = stringArrayResource(Res.array.religions)
        val drinks = stringArrayResource(Res.array.drink_preferences)
        val edu = stringArrayResource(Res.array.education_levels)
        val family = stringArrayResource(Res.array.children_preferences)
        val languages = stringArrayResource(Res.array.languages)



        LaunchedEffect(true){
            languageScreenModel.loadLanguages(languages)
            editProfileScreenModel.loadDataFromResources(
                smoking = smoking,
                religion = religion,
                drinking = drinks,
                pet = emptyList(),
                family = family,
                edu = edu
            )
        }


        DisposableEffect(Unit) {
            onDispose {
                // Called when the screen is about to close
                profileUploadScreenModel.reorderImages(context,true, updateOnlyLocally = false)
//                if (profileUploadScreenModel.state.value.isProfileUpdated){
//                 //   accountsViewModel.getUser(context){}
//                }
                editProfileScreenModel.updateAgeRangeAndDistancePrefsIfRequired(context)
                editProfileScreenModel.updateLocation(context)
            }
        }

        EditProfileScreenContent(
            onBack = {
                if (profileScreenState.isProfileUpdated){
                    accountsViewModel.getUser(context){}
                    editProfileScreenModel.updateProfileUpdatedStatus(false)
                }
                navigator.pop()
                println("on back clicked")
            },
            isDarkMode = isDarkMode,
            interestsScreenModel = interestsScreenModel,
            languageScreenModel = languageScreenModel,
            profileUploadScreenModel = profileUploadScreenModel,
            navigateToGalleryScreen = {
                navigator.push(GalleryAlbumScreen())
            },
            profileScreenState = profileScreenState,
            screenModel = editProfileScreenModel,
            accountsState = accountsState,
            accountsViewModel = accountsViewModel,
            navigateToEditPickedMediaScreen = {
                navigator.push(EditPickedMediaScreen())
            },
            navigateToControlProfilePrivacy = {
                navigator.push(WhoCanSeeMyProfileScreen())
            }
        )
    }
}

@OptIn(InternalVoyagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenContent(
    onBack:()->Unit,
    isDarkMode:Boolean,
    interestsScreenModel: ChooseInterestsScreenModel,
    languageScreenModel: LanguageScreenModel,
    profileUploadScreenModel:ProfileUploadScreenViewModel,
    navigateToGalleryScreen:()->Unit,
    navigateToControlProfilePrivacy:()->Unit,
    profileScreenState: EditProfileScreenState,
    screenModel: EditProfileScreenModel,
    accountsState:AccountsApiCallState,
    accountsViewModel: AccountsViewModel,
    navigateToEditPickedMediaScreen:()->Unit
) {

    val context = LocalPlatformContext.current
    val monthRes = stringArrayResource(Res.array.months)

    LaunchedEffect(true){
        screenModel.loadDataFromApi(accountsState,monthRes)
        if (accountsState.user?.medias != null) {
            profileUploadScreenModel.loadImages(accountsState.user.medias)
        }
     //   delay(900)
      //  profileUploadScreenModel.loadImagesFromGallery(context, 0)

    }

    LaunchedEffect(accountsState.user?.medias){
        if (accountsState.user?.medias != null) {
            println("the media images has updated")
            profileUploadScreenModel.loadImages(accountsState.user.medias)
        }
    }

    var toastMessage by remember { mutableStateOf("") }

    BackHandler(true){
        onBack()
    }

    val state by screenModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    val sheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val profileUploadSheetState =  rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cameraManager = rememberCameraManager {
        it?.let { profileUploadScreenModel.updateCapturedImage(image = it,context = context, onComplete = {
            navigateToEditPickedMediaScreen()
        }) }
    }

    val profileUploadState by  profileUploadScreenModel.state.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollValue ->
                val maxScroll = scrollState.maxValue
                screenModel.changeFloatingBottomState(if (scrollValue >= maxScroll * 0.9) true else false)
            }
    }

    LaunchedEffect(profileUploadState.cameraPermissionState,profileUploadState.galleryPermissionState){

        when(profileUploadState.cameraPermissionState){
            PermissionState.Granted -> {
                if (profileUploadState.launchCamera) {
                    cameraManager.launch()
                    profileUploadScreenModel.onToggleCamera()
                }
            }
            PermissionState.DeniedAlways -> {
                if (profileUploadState.launchCamera) {
                    profileUploadScreenModel.onTogglePermissionRationale()
                    profileUploadScreenModel.onToggleCamera()
                }
            }
            PermissionState.Denied , PermissionState.NotGranted ->{
                if (profileUploadState.launchCamera) {
                    profileUploadScreenModel.onToggleCamera()
                }
            }
            PermissionState.NotDetermined ->{}
        }

        when(profileUploadState.galleryPermissionState){
            PermissionState.Granted -> {
                if (profileUploadState.launchGallery) {
                    profileUploadScreenModel.retryFetching()
                    navigateToGalleryScreen()
                    profileUploadScreenModel.onToggleGallery()
                }
            }
            PermissionState.DeniedAlways -> {
                if (profileUploadState.launchGallery) {
                    profileUploadScreenModel.onTogglePermissionRationale()
                    profileUploadScreenModel.onToggleGallery()
                }
            }
            PermissionState.Denied , PermissionState.NotGranted ->{
                if (profileUploadState.launchGallery) {
                    profileUploadScreenModel.onToggleGallery()
                }
            }
            PermissionState.NotDetermined ->{}
        }
    }

    LaunchedEffect(profileUploadState.images){
        profileUploadScreenModel.updateButtonState()
    }

    if (profileUploadState.permissionRationalDialog) {
        AlertMessageDialog(title = if (profileUploadState.launchCamera) stringResource(Res.string.camera_permissions_required) else stringResource(Res.string.photo_permissions_required),
            message = "${stringResource(Res.string.please_grant)} ${if (profileUploadState.launchCamera)  stringResource(Res.string.camera) else stringResource(Res.string.photo)} ${stringResource(Res.string.permissions_in_your_settings)}",
            onPositiveClick = {
                //   if (state.launchCamera) viewModel.onToggleCamera() else viewModel.onToggleGallery()
                if (profileUploadState.launchGallery) profileUploadScreenModel.onToggleGallery()
                if (profileUploadState.launchCamera) profileUploadScreenModel.onToggleCamera()
                profileUploadScreenModel.onTogglePermissionRationale()
                profileUploadScreenModel.openSettings()
            },
            onNegativeClick = {
                // if (state.launchCamera) viewModel.onToggleCamera() else viewModel.onToggleGallery()
                if (profileUploadState.launchGallery) profileUploadScreenModel.onToggleGallery()
                if (profileUploadState.launchCamera) profileUploadScreenModel.onToggleCamera()
                profileUploadScreenModel.onTogglePermissionRationale()
            },
            isDarkMode = isDarkMode
        )

    }



    Column {

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

            TopHeaderSection(
                onBack = { onBack() },
                onSave = {
//                    val filteredImages = profileUploadState.images.filter { it.image != null && it.imgId == null }
//                    if (filteredImages.isNotEmpty()){
//                        println("The upload for ${filteredImages.size} files initiated")
//                        screenModel.updateLoading(true)
//                        profileUploadScreenModel.getImageUploadDataForImages(filteredImages) { images ->
//                            println("the upload data are $images")
//                            accountsViewModel.uploadImages(images, context){
//                                accountsViewModel.getUser(context)
//                                screenModel.updateLoading(false)
//                                onBack()
//                            }
//                        }
//                    }else{
//                        onBack()
//                    }
                    onBack()
                },
                text = stringResource(Res.string.edit_profile),
                isDarkMode = isDarkMode
            )

            if ((accountsState.user?.progress ?: 0) < 100) {
                CompleteProfileCard(progress = accountsState.user?.progress ?: 0)
            }

            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {

                    if (profileUploadState.images.isNotEmpty()) {

                        Box (
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            contentAlignment = Alignment.BottomCenter
                        ){

                            ReorderableLazyGrid(
                                images = profileUploadState.images,
                                onClickAdd = {
                                    profileUploadScreenModel.updateClickedIndex(it)
                                    profileUploadScreenModel.onToggleImageSourceOptionDialog()
                                },
                                onEdit = {
                                    profileUploadScreenModel.updateClickedIndex(it)
                                    profileUploadScreenModel.onToggleImageSourceOptionDialog()
                                },
                                onRemove = {
                                    profileUploadScreenModel.updateClickedIndex(it)
                                    profileUploadScreenModel.onToggleImageSourceOptionDialog()
                                },
                                onMove = { from, to ->
                                    profileUploadScreenModel.movePhoto(from, to)
                                  //  profileUploadScreenModel.reorderImages(context,true, updateOnlyLocally = true)
                                },
                                aspectRatio = profileUploadState.aspectRatio
                            )

                            if (profileUploadState.progress < 1f) {
                                ProgressIndicator(profileUploadState.progress)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))

                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){

                        Image(
                            imageVector = vectorResource(Res.drawable.ic_info),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = stringResource(Res.string.hold_drag_to_reorder),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 21.sp,
                            color = if(isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                        )

                    }

                    Spacer(Modifier.height(16.dp))
                    Spacer(Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(24.dp)
                            .noRippleClickable(navigateToControlProfilePrivacy),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = stringResource(Res.string.control_profile_privacy),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.2.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                            fontFamily = PoppinsFontFamily()
                        )

                        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center){
                            if (isDarkMode){
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                                    contentDescription = null,
                                    tint = DISABLED_LIGHT
                                )
                            }else {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                                    contentDescription = null
                                )
                            }
                        }

                    }

                    Spacer(Modifier.height(20.dp))

                    EditProfileItem(
                        title = stringResource(Res.string.bio),
                        statusVisible = state.bio.isVisibleOnProfile,
                        description = state.bio.description.ifBlank {stringResource(Res.string.add_bio)},
                        showIndicator = state.bio.description.isBlank(),
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateBio)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.dating_preference),
                        statusVisible = true,
                        description = state.datingPrefs.selectedItems.let {if (it.isEmpty()) stringResource(Res.string.choose_option_that_feels_right) else it.joinToString(", ") },
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateDatingPrefs)},
                        showIndicator = false,
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.name),
                        statusVisible = true,
                        description = state.name.name,
                        showIndicator = state.name.name.isBlank(),
                        onClick = { screenModel.openSheet(ProfileBottomSheetType.UpdateName) },
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.age),
                        statusVisible = state.age.isVisible,
                        description = state.age.age.toString(),
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateAge)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.gender),
                        statusVisible = state.gender.isVisible,
                        description = state.gender.categories.getOrElse(state.gender.selectedIndex) { "Pick the gender that best describe you" },
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateGender)},
                        isDarkMode = isDarkMode
                    )

                    WhoDoYouWantToMeetSection(
                        modifier = Modifier,
                        isDarkMode = isDarkMode,
                        showMe = state.showMe,
                        updateShowMe = {
                            screenModel.updateShowMe(it)
                        }
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.interests),
                        statusVisible = true,
                        showIndicator = state.interests.isEmpty(),
                        description = state.interests.joinToString(", ").ifBlank { "Add Interests" },
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateInterests)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.height),
                        statusVisible = state.height.isVisible,
                        description = if (state.height.height > 0) "${state.height.height} cm" else "Add height",
                        showIndicator = state.height.height <= 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateHeight)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.location),
                        statusVisible = true,
                        description = "${accountsState.currentLocation?.city}, ${accountsState.currentLocation?.country}",
                        isDarkMode = isDarkMode
                    )

                    EditProfileItemWithSlider(
                        title = stringResource(Res.string.distance_preference),
                        description = "${state.distancePref.toInt()} km",
                        sliderValue = state.distancePref,
                        valueRange = 1f..100f,
                        onValueChangeSingleThumb = screenModel::updateDistance,
                        isDarkMode = isDarkMode
                    )


                    EditProfileItem(
                        title = stringResource(Res.string.show_me),
                        statusVisible = state.showMe.isVisible,
                        description = state.showMe.selectedItems.let {if (it.isEmpty()) "" else it.joinToString(", ") },
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateShowMe)},
                        isDarkMode = isDarkMode
                    )

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
                        title = stringResource(Res.string.education_level),
                        statusVisible = state.educationLevel.isVisible,
                        description = state.educationLevel.items.getOrElse(state.educationLevel.selectedIndex){"Add education"},
                        showIndicator = state.educationLevel.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateEducationLevel)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.your_profession),
                        statusVisible = state.profession.isVisible,
                        description = if (state.profession.jobTitle.isBlank()) stringResource(Res.string.add_profession) else "${state.profession.jobTitle}, ${state.profession.orgName}",
                        showIndicator = state.profession.jobTitle.isBlank(),
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateProfession)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.pets),
                        statusVisible = state.pet.isVisible,
                        description = state.pet.items.getOrElse(state.pet.selectedIndex){"Add pets"},
                        showIndicator = state.pet.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdatePets)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.religion),
                        statusVisible = state.religion.isVisible,
                        description = state.religion.items.getOrElse(state.religion.selectedIndex){"Add religion"},
                        showIndicator = state.religion.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateReligion)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.family_plans),
                        statusVisible = state.familyPlanning.isVisible,
                        description = state.familyPlanning.items.getOrElse(state.familyPlanning.selectedIndex){"Add family plans"},
                        showIndicator = state.familyPlanning.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateFamilyPlans)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.drinking),
                        statusVisible = state.drinking.isVisible,
                        description = state.drinking.items.getOrElse(state.drinking.selectedIndex){"Add drinking preference"},
                        showIndicator = state.drinking.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateDrinking)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.smoking),
                        statusVisible = state.smoking.isVisible,
                        description = state.smoking.items.getOrElse(state.smoking.selectedIndex){"Add smoking preference"},
                        showIndicator = state.smoking.selectedIndex < 0,
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateSmoking)},
                        isDarkMode = isDarkMode
                    )

                    EditProfileItem(
                        title = stringResource(Res.string.languages),
                        statusVisible = state.languages.isVisible,
                        description = if (state.languages.languages.isNotEmpty()) state.languages.languages.joinToString(", ") else {"Add languages"},
                        showIndicator = state.languages.languages.isEmpty(),
                        onClick = {screenModel.openSheet(ProfileBottomSheetType.UpdateLanguages)},
                        isDarkMode = isDarkMode
                    )
                    Spacer(Modifier.height(16.dp))
                }

                Box(Modifier.fillMaxSize().padding(bottom = 5.dp, end = 16.dp), contentAlignment = Alignment.BottomEnd){
                    if (state.isFloatingButtonVisible) {
                        FloatingActionButton(
                            modifier = Modifier.padding(bottom = 50.dp),
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 1.dp),
                            containerColor = if (isDarkMode) CARD_BG_DARK else Color.White,
                            onClick = {
                                scope.launch {
                                    scrollState.animateScrollTo(0)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_arrow_up),
                                contentDescription = "",
                                tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                            )
                        }
                    }
                }
            }


        }




        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }


    PaginatedLazyVerticalGrid(paginationState = profileUploadScreenModel.paginationState, columns = GridCells.Fixed(3)){}


    if (profileScreenState.isLoading || state.isLoading){
        CircularLoader(true)
    }

    AnimatedVisibility(
        modifier = Modifier.padding(top = 56.dp).padding(horizontal = 60.dp).height(36.dp),
        visible = toastMessage.isNotBlank(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Toast(text = toastMessage)

        LaunchedEffect(Unit) {
            delay(2000) // Hide after 2 seconds
            toastMessage = ""
        }

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
                is ProfileBottomSheetType.UpdateBio -> {
                    UpdateBioSheetContent(
                        onSave = {
                            sheetState.hideWithCompletion(scope) {
                                screenModel.hideSheet()
                                screenModel.updateProfileData(
                                    item = ProfileUpdateItem.BIO,
                                    data = UpdateProfileData(state.bio.isVisibleOnProfile,state.bio.description),
                                    context = context
                                )
                        }},
                        bio = state.bio,
                        updateBio = screenModel::updateBio,
                        isDarkMode = isDarkMode
                    )
                }

                is ProfileBottomSheetType.UpdateDatingPrefs ->{
                    UpdateDatingPrefsSheetContent(
                        onSave = {items -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.TYPE_OF_RELATION,
                                data = UpdateProfileData(false,items),
                                context = context
                            )
                        }},
                        datingPrefs = state.datingPrefs,
                        isDarkMode = isDarkMode,
                        onChangeSelection = screenModel::updateDatingPrefSelections
                    )

                }

                is ProfileBottomSheetType.UpdateName ->{
                    UpdateNameSheetContent(
                        name = state.name,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            if (accountsState.user?.isNameUpdatable == false) {
                                toastMessage = "Name cannot be updated"
                            }else {
                                accountsState.user?.copy(isNameUpdatable = false)?.let{accountsViewModel.updateUserRes(it)}
                                screenModel.updateProfileData(
                                    item = ProfileUpdateItem.NAME,
                                    data = UpdateProfileData(false, state.name.name),
                                    context = context
                                )
                            }
                        }},
                        updateName = {
                            if (accountsState.user?.isNameUpdatable == true) {
                                screenModel.updateName(it)
                            }
                        },
                        shouldDisable = accountsState.user?.isNameUpdatable == false
                    )
                }

                is ProfileBottomSheetType.UpdateAge ->{
                    UpdateAgeSheetContent(
                        isAgeUpdatable = accountsState.user?.isAgeUpdatable ?: false,
                        age = state.age,
                        isDarkMode = isDarkMode,
                        updateAge = { age,forceUpdate->
                            println("update the age $age $forceUpdate ${accountsState.user?.isAgeUpdatable == true || forceUpdate}")
                            if (accountsState.user?.isAgeUpdatable == true || forceUpdate) {
                                screenModel.updateAge(age)
                            }
                        },
                        onSave = { forceUpdate -> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            if (accountsState.user?.isAgeUpdatable == false && !forceUpdate) {
                                toastMessage = "Age cannot be updated"
                            }else {
                                accountsState.user?.copy(isAgeUpdatable = false, isAgeVisible = state.age.isVisible)?.let{accountsViewModel.updateUserRes(it)}
                                screenModel.updateProfileData(
                                    item = ProfileUpdateItem.AGE,
                                    data = UpdateProfileData(state.age.isVisible, state.age.age),
                                    context = context
                                )
                            }
                        }},
                        shouldDisable = accountsState.user?.isAgeUpdatable == false
                    )
                }

                is ProfileBottomSheetType.UpdateGender ->{
                    UpdateGenderSheetContent(
                        gender = state.gender,
                        isDarkMode = isDarkMode,
                        updateGender = screenModel::updateGender,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            val gender = state.gender.categories[state.gender.selectedIndex]
                            if (accountsState.user?.gender != gender || accountsState.user.extraGenderText != state.gender.moreAboutGender) {
                                accountsState.user?.copy(isGenderUpdatable = false)?.let{accountsViewModel.updateUserRes(it)}
                                screenModel.updateProfileData(
                                    item = ProfileUpdateItem.GENDER,
                                    data = UpdateProfileData(
                                        state.gender.isVisible,
                                        GenderData(
                                            gender = gender,
                                            moreAboutGender = state.gender.moreAboutGender
                                        )
                                    ),
                                    context = context
                                )
                            }
                        }},
                        shouldDisable = accountsState.user?.isGenderUpdatable == false
                    )
                }

                is ProfileBottomSheetType.UpdateShowMe ->{
                    UpdateShowMeBottomSheetContent(
                        data = state.showMe,
                        isDarkMode = isDarkMode,
                        updateShowMe = screenModel::updateShowMe,
                        onSave = {items-> sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
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
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.updateInterests(it)
                            screenModel.hideSheet()
                            screenModel.updateUserInterests(
                                data = interestsScreenModel.getInterestsData(),
                                context = context
                            )
                        }},
                        selectedItems = state.interests
                    )
                }

                is ProfileBottomSheetType.UpdateHeight ->{
                    UpdateHeightSheetContent(
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.HEIGHT,
                                data = UpdateProfileData(state.height.isVisible,state.height.height),
                                context = context
                            )
                        }},
                        updateHeight = screenModel::updateHeight,
                        height = if (state.height.height < 0) EditProfileScreenState.Height(index = 70, height = 160) else  state.height
                    )
                }

                is ProfileBottomSheetType.UpdateEducationLevel ->{
                    UpdateEducationLevelSheetContent(
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.EDUCATION,
                                data = UpdateProfileData(state.educationLevel.isVisible,state.educationLevel.items[state.educationLevel.selectedIndex]),
                                context = context
                            )
                        }},
                        education = state.educationLevel,
                        updateEducation = screenModel::updateEducationLevel
                    )
                }

                is ProfileBottomSheetType.UpdateProfession ->{
                    UpdateProfessionSheetContent(
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.PROFESSION,
                                data = UpdateProfileData(state.profession.isVisible,state.profession),
                                context = context
                            )
                        }},
                        profession = state.profession,
                        updateProfession = screenModel::updateProfession
                    )
                }

                is ProfileBottomSheetType.UpdateDrinking -> {
                    UpdateDrinkingSheetContent(
                        drinking = state.drinking,
                        updateDrinking = screenModel::updateDrinking,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.DRINK,
                                data = UpdateProfileData(state.drinking.isVisible,state.drinking.items[state.drinking.selectedIndex]),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdateFamilyPlans -> {
                    UpdateFamilyPlanningSheetContent(
                        familyPlanning = state.familyPlanning,
                        updateFamilyPlanning = screenModel::updateFamilyPlanning,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.FAMILY,
                                data = UpdateProfileData(state.familyPlanning.isVisible,state.familyPlanning.items[state.familyPlanning.selectedIndex]),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdatePets -> {
                    UpdatePetSheetContent(
                        pet = state.pet,
                        updatePet = screenModel::updatePet,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.PETS,
                                data = UpdateProfileData(state.pet.isVisible, listOf(state.pet.items[state.pet.selectedIndex])),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdateReligion -> {
                    UpdateReligionSheetContent(
                        religion = state.religion,
                        updateReligion = screenModel::updateReligion,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.RELIGION,
                                data = UpdateProfileData(state.religion.isVisible,state.religion.items[state.religion.selectedIndex]),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdateSmoking -> {
                    UpdateSmokingSheetContent(
                        smoking = state.smoking,
                        updateSmoking = screenModel::updateSmoking,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateUserPrefs(
                                item = ProfilePrefsUpdateItem.SMOKE,
                                data = UpdateProfileData(state.smoking.isVisible,state.smoking.items[state.smoking.selectedIndex]),
                                context = context
                            )
                        }},
                    )
                }

                is ProfileBottomSheetType.UpdateLanguages -> {
                    UpdateLanguagesSheetContent(
                        languages = state.languages,
                        updateLanguage = screenModel::updateLanguages,
                        isDarkMode = isDarkMode,
                        onSave = { sheetState.hideWithCompletion(scope) {
                            screenModel.hideSheet()
                            screenModel.updateProfileData(
                                item = ProfileUpdateItem.LANGUAGE,
                                data = UpdateProfileData(state.languages.isVisible,state.languages.languages),
                                context = context
                            )
                        }},
                        screenModel = languageScreenModel
                    )
                }

                is ProfileBottomSheetType.UpdateSortBy ->{

                }






                null -> {

                }


            }



        }
    }

    if (profileUploadState.imageSourceOptionDialog){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp,0.dp,0.dp,0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = profileUploadSheetState,
            onDismissRequest = profileUploadScreenModel::onToggleImageSourceOptionDialog,
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ){

            ImageSourceOptionDialog(onGalleryRequest = {
                profileUploadScreenModel.onToggleImageSourceOptionDialog()
                profileUploadScreenModel.launchGallery(navigateToGalleryScreen)
            }, onCameraRequest = {
                profileUploadScreenModel.onToggleImageSourceOptionDialog()
                profileUploadScreenModel.launchCamera { cameraManager.launch() }
            }, isDarkMode = isDarkMode,
                showDeleteOption = profileUploadState.clickedIndex > 0 && profileUploadState.images[profileUploadState.clickedIndex].image != null,
                onDeleteRequest = {
                    screenModel.updateProfileUpdatedStatus(true)
                    profileUploadScreenModel.onToggleImageSourceOptionDialog()
                    profileUploadScreenModel.deleteItem(context)
                },
                isUpdate = profileUploadState.images.getOrNull(profileUploadState.clickedIndex)?.image != null
            )


        }
    }
}

@Composable
fun WhoDoYouWantToMeetSection(modifier: Modifier, isDarkMode: Boolean, showMe: EditProfileScreenState.ShowMe, updateShowMe:(EditProfileScreenState.ShowMe)-> Unit){
    val showMeItems = listOf("Man","Woman","Non-binary")

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = if (isDarkMode) Color(0xff737272) else Color(0xffEAECF0)
        )

        Row(modifier = Modifier.fillMaxWidth().height(52.dp), verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier,
                text = stringResource(Res.string.who_do_you_want_to_meet),
                color = if (isDarkMode) Color.White else Color(0xff101828),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
                .requiredHeight(36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Text(
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                text = stringResource(Res.string.open_to_everyone),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontFamily = PoppinsFontFamily()
            )

            Box(Modifier.requiredHeight(20.dp).requiredWidth(36.dp)) {
                CustomSwitch(
                    modifier = Modifier.fillMaxSize(),
                    isChecked = showMe.selectedItems.size == 3,
                    onCheckChanged = {
                        updateShowMe(showMe.copy(
                            selectedItems = showMeItems
                        ))
                    },
                    isDarkMode = isDarkMode
                )
            }

        }
        Spacer(Modifier.height(16.dp))
        showMeItems.forEachIndexed { index, value ->
            TextWithCheckbox(
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 8.dp),
                text = value,
                isChecked = showMe.selectedItems.any { it == value },
                hideCheckBox = false,
                onClick = {
                    updateShowMe(showMe.copy(
                        selectedItems = showMe.selectedItems.toMutableList().apply {
                            if (showMe.selectedItems.contains(value)){
                                remove(value)
                            }else{
                                add(value)
                            }
                        }
                    ))
                },
                isDarkMode = isDarkMode
            )
            Spacer(Modifier.height(8.dp))
            if (index != showMeItems.lastIndex) {
                CustomDivider(modifier = Modifier.padding(8.dp), isDarkMode = isDarkMode)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun SheetState.hideWithCompletion(
    scope: CoroutineScope,
    onHidden: () -> Unit
) {
    scope.launch {
        hide()
    }.invokeOnCompletion {
        if (!isVisible) {
            onHidden()
        }
    }
}
