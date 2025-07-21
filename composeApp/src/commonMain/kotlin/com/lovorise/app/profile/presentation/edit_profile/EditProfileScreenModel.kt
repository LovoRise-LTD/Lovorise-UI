package com.lovorise.app.profile.presentation.edit_profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.age.validateAge
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.profile.domain.repo.ProfilePrefsUpdateItem
import com.lovorise.app.profile.domain.repo.ProfileRepo
import com.lovorise.app.profile.domain.repo.ProfileUpdateItem
import com.lovorise.app.profile.domain.repo.UpdateProfileData
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min

class EditProfileScreenModel(private val profileRepo: ProfileRepo,private val accountsViewModel: AccountsViewModel) : ScreenModel {

    private val _state = MutableStateFlow(EditProfileScreenState())
    val state = _state.asStateFlow()
    private var prefs: SharedPrefs? = null

    fun updateHeightRange(value:Pair<Float,Float>){
        _state.update {
            it.copy(
                height = state.value.height.copy(
                    range = value
                )
            )
        }
    }

    fun updateNavigateBackState(bool:Boolean){
        _state.update {
            it.copy(
                shouldNavigateBack = bool
            )
        }
    }

    fun updateLoading(value: Boolean){
        _state.update {
            it.copy(
                isLoading = value
            )
        }
    }

    fun updateAgeRangeAndDistanceWithMeasurements(context: PlatformContext){
        if (!state.value.isAgeRangeUpdate && !state.value.isDistancePreferenceUpdate) return
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        val currentUser = accountsViewModel.getCurrentUser() ?: return
        accountsViewModel.updateUserRes(
            currentUser.copy(
                distance = state.value.distancePref.toDouble(),
                distanceMeasurement = state.value.distanceMeasurement,
                ageStart = state.value.ageRange.start,
                ageEnd = state.value.ageRange.end
            )
        )
        screenModelScope.launch {
            profileRepo.updateAgeRangeAndDistance(token = token, ageRange = Pair(state.value.ageRange.start,state.value.ageRange.end), distance = state.value.distancePref.toDouble(),distanceMeasurement = state.value.distanceMeasurement.stringValue).collectLatest { response ->
                println("the response is $response")
                when(response){
                    is Resource.Success -> {
                        if (response.data != null) {
                            _state.update {
                                it.copy(
                                    isProfileUpdated = response.data
                                )
                            }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = response.isLoading
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateAgeRangeAndDistancePrefsIfRequired(context: PlatformContext){
        val currentUser = accountsViewModel.getCurrentUser() ?: return
        if (!state.value.isAgeRangeUpdate && !state.value.isDistancePreferenceUpdate) return
        if (state.value.isAgeRangeUpdate){
            accountsViewModel.updateUserRes(
                currentUser.copy(
                    ageStart = state.value.ageRange.start,
                    ageEnd = state.value.ageRange.end
                )
            )
            updateUserPrefs(
                item = ProfilePrefsUpdateItem.AGE_RANGE,
                data = UpdateProfileData(false,Pair(state.value.ageRange.start,state.value.ageRange.end)),
                context = context
            )
        }

        if (state.value.isDistancePreferenceUpdate){
            accountsViewModel.updateUserRes(
                currentUser.copy(
                    distance = state.value.distancePref.toDouble()
                )
            )
            updateUserPrefs(
                item = ProfilePrefsUpdateItem.DISTANCE,
                data = UpdateProfileData(false,state.value.distancePref.toDouble()),
                context = context
            )
        }
    }

    fun updateLocation(context: PlatformContext){
        if (state.value.isLocationUpdated) return
        val currentLocation = accountsViewModel.state.value.currentLocation ?: return

        _state.update {
            it.copy(
                location = UserResponse.LocationData(
                    country = currentLocation.country,
                    city = currentLocation.city,
                    longitude = currentLocation.longitude,
                    latitude = currentLocation.latitude
                )
            )
        }

        updateProfileData(
            item = ProfileUpdateItem.LOCATION,
            data = UpdateProfileData(false,currentLocation),
            context = context,
            shouldRefresh = false
        )
        _state.update {
            it.copy(
                isLocationUpdated = true
            )
        }
    }

    fun updateProfileData(item: ProfileUpdateItem, data: UpdateProfileData<Any>, context: PlatformContext,shouldRefresh:Boolean = true){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            profileRepo.updateProfile(item,data,token).collectLatest { response ->
                println("the response is $response")
                when(response){
                    is Resource.Success -> {
                        if (response.data != null) {
                            _state.update {
                                it.copy(
                                    isProfileUpdated = response.data && shouldRefresh
                                )
                            }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = response.isLoading
                            )
                        }
                    }
                }

            }
        }
    }

    fun updateProfileUpdatedStatus(value:Boolean){
        _state.update {
            it.copy(
                isProfileUpdated = value
            )
        }
    }

    fun updateUserPrefs(item: ProfilePrefsUpdateItem, data: UpdateProfileData<Any>, context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            profileRepo.updateUserPrefs(item,data,token).collectLatest { response ->

                when(response){
                    is Resource.Success -> {
                        if (response.data != null) {
                            _state.update {
                                it.copy(
                                    isProfileUpdated = response.data
                                )
                            }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = response.isLoading
                            )
                        }
                    }
                }

            }
        }
    }

    fun updateUserInterests(data:Map<String,List<String>>,context: PlatformContext){
        if (prefs == null){
            prefs = SharedPrefsImpl(context)
        }
        val token = prefs?.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            profileRepo.updateUserInterests(data,token).collectLatest { response ->
                when(response){
                    is Resource.Success -> {
                        if (response.data != null) {
                            _state.update {
                                it.copy(
                                    isProfileUpdated = response.data
                                )
                            }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = response.isLoading
                            )
                        }
                    }
                }

            }
        }
    }

    fun updateDistanceMeasurement(value:DistanceMeasurement){
        _state.update {
            it.copy(
                isDistancePreferenceUpdate = true,
                distanceMeasurement = value
            )
        }
    }


    fun loadDataFromApi(accountsState: AccountsApiCallState,months:List<String>){
        val dobText = accountsState.user?.birthDate?.split("-")?.reversed()?.joinToString("") ?: ""
        val validatedAgeResult = validateAge(dobText,months)

        var distancePrefs = accountsState.user?.distance?.toFloat()

        if (distancePrefs == null){
            distancePrefs = 100f
            updateDistance(100f)
        }

        var ageStart = accountsState.user?.ageStart
        var ageEnd = accountsState.user?.ageEnd

        if (ageStart == null || ageEnd == null){
            val age = validatedAgeResult?.age ?: 18
            ageStart = min(18, abs(age-10))
            ageEnd = age + 10
            updateAgeRange(EditProfileScreenState.AgeRange(ageStart,ageEnd))
        }

        val currentLocation = accountsState.currentLocation
        val location = if (currentLocation == null){
            accountsState.user?.locationData
        }else {
            UserResponse.LocationData(
                country = currentLocation.country,
                city = currentLocation.city,
                longitude = currentLocation.longitude,
                latitude = currentLocation.latitude
            )
        }


        _state.update {
            it.copy(
                bio = EditProfileScreenState.Bio(description = accountsState.user?.bio ?: "", isVisibleOnProfile = accountsState.user?.bioVisible ?: true),
                age = EditProfileScreenState.Age(age = validatedAgeResult?.age ?: 18, lastUpdated = accountsState.user?.ageUpdatedAt, formattedAge = validatedAgeResult?.formatted ?: "", dobText = dobText),
                datingPrefs = EditProfileScreenState.DatingPrefs(selectedItems = accountsState.user?.typeOfRelation ?: emptyList()),
                height = EditProfileScreenState.Height(height = accountsState.user?.height ?: -1, index = abs((accountsState.user?.height ?: 89) - 90), isVisible = accountsState.user?.heightVisible ?: true),
                interests = accountsState.user?.interests?.values?.filterNotNull()?.flatten()?.filterNotNull() ?: emptyList(),
                ageRange = EditProfileScreenState.AgeRange(start = ageStart, end = ageEnd),
                name = EditProfileScreenState.Name(name = accountsState.user?.name ?: "", lastUpdated = accountsState.user?.nameUpdatedAt),
                religion = EditProfileScreenState.Religion(selectedIndex = state.value.religion.items.indexOf(accountsState.user?.religion ?: ""), isVisible = accountsState.user?.religionVisible ?: true),
                drinking = EditProfileScreenState.Drinking(selectedIndex = state.value.drinking.items.indexOf(accountsState.user?.drinking ?: ""), isVisible = accountsState.user?.drinkingVisible ?: true),
                educationLevel = EditProfileScreenState.EducationLevel(selectedIndex = state.value.educationLevel.items.indexOf(accountsState.user?.education ?: ""), isVisible = accountsState.user?.educationVisible ?: true),
                smoking = EditProfileScreenState.Smoking(selectedIndex = state.value.smoking.items.indexOf(accountsState.user?.smoking ?: ""), isVisible = accountsState.user?.smokingVisible ?: true),
                gender = EditProfileScreenState.Gender(selectedIndex = state.value.gender.categories.indexOf(accountsState.user?.gender ?: ""), isVisible = accountsState.user?.genderVisible ?: true, moreAboutGender = accountsState.user?.extraGenderText ?: ""),
                familyPlanning = EditProfileScreenState.FamilyPlanning(selectedIndex = state.value.familyPlanning.items.indexOf(accountsState.user?.family ?: ""), isVisible = accountsState.user?.familyVisible ?: true),
                languages = EditProfileScreenState.Languages(languages = accountsState.user?.language?.filterNotNull() ?: emptyList(), isVisible = accountsState.user?.languageVisible ?: true),
                distancePref = distancePrefs,
                pet = EditProfileScreenState.Pet(selectedIndex = state.value.pet.items.indexOf(accountsState.user?.pets?.firstOrNull() ?: ""), isVisible = accountsState.user?.petsVisible ?: true),
                showMe = EditProfileScreenState.ShowMe(selectedItems = accountsState.user?.likeToMeet ?: emptyList(), isVisible = accountsState.user?.likeToMeetVisible ?: true),
                profession = EditProfileScreenState.Profession(jobTitle = accountsState.user?.profession?.jobTitle ?:"", orgName =  accountsState.user?.profession?.company ?:"", isVisible =  accountsState.user?.profession?.professionVisible ?: true),
                location = location,
                distanceMeasurement = accountsState.user?.distanceMeasurement ?: DistanceMeasurement.KM
            )
        }
    }

    fun loadDataFromResources(smoking:List<String>,drinking:List<String>,family:List<String>,religion:List<String>,pet:List<String>,edu:List<String>){
        _state.update {
            it.copy(
                smoking = it.smoking.copy(items = smoking),
                drinking = it.drinking.copy(items = drinking),
                familyPlanning = it.familyPlanning.copy(items = family),
                religion = it.religion.copy(items = religion),
                educationLevel = it.educationLevel.copy(items = edu),
           //     pet = it.pet.copy(items = pet),
            )
        }

    }

    fun toggleNameCheckboxState(){
        _state.update {
            it.copy(
                name = it.name.copy(isChecked = !it.name.isChecked)
            )
        }
    }

    fun updateBio(bio:EditProfileScreenState.Bio){
        _state.update {
            it.copy(bio = bio)
        }
    }

    fun updateName(name: EditProfileScreenState.Name){
        _state.update {
            it.copy(name = name)
        }
    }

    fun updateInterests(data:List<String>){
        _state.update {
            it.copy(
                interests = data
            )
        }
    }

    fun updateHeight(data:EditProfileScreenState.Height){
        _state.update {
            it.copy(
                height = data
            )
        }
    }

    fun updateAge(age:EditProfileScreenState.Age){
        accountsViewModel.state.value.user?.copy(isAgeVisible = age.isVisible)?.let{accountsViewModel.updateUserRes(it)}

        println("the age is being updated $age")
        _state.update {
            it.copy(
                age = age
            )
        }
    }

    fun updateEducationLevel(data:EditProfileScreenState.EducationLevel){
        _state.update {
            it.copy(
                educationLevel = data
            )
        }
    }

    fun updateProfession(data:EditProfileScreenState.Profession){
        _state.update {
            it.copy(
                profession = data
            )
        }
    }

    fun updatePet(data:EditProfileScreenState.Pet){
        _state.update {
            it.copy(
                pet = data
            )
        }
    }

    fun updateReligion(data:EditProfileScreenState.Religion){
        _state.update {
            it.copy(
                religion = data
            )
        }
    }

    fun updateFamilyPlanning(data:EditProfileScreenState.FamilyPlanning){
        _state.update {
            it.copy(
                familyPlanning = data
            )
        }
    }

    fun updateDrinking(data:EditProfileScreenState.Drinking){
        _state.update {
            it.copy(
                drinking = data
            )
        }
    }

    fun updateSmoking(data:EditProfileScreenState.Smoking){
        _state.update {
            it.copy(
                smoking = data
            )
        }
    }

    fun updateLanguages(data:EditProfileScreenState.Languages){
        _state.update {
            it.copy(
                languages = data
            )
        }
    }

    fun updateAgeRange(range:EditProfileScreenState.AgeRange){
        _state.update {
            it.copy(
                ageRange = range,
                isAgeRangeUpdate = true
            )
        }
    }

    fun updateDistance(value:Float){
        _state.update {
            it.copy(
                distancePref = value,
                isDistancePreferenceUpdate = true
            )
        }
    }

    fun updateGender(gender:EditProfileScreenState.Gender){
        _state.update {
            it.copy(
                gender = gender,
            )
        }
    }

    fun updateShowMe(data:EditProfileScreenState.ShowMe){
        _state.update {
            it.copy(
                showMe = data
            )
        }
    }




    fun updateDatingPrefSelections(items:List<String>){
        _state.update {
            it.copy(
                datingPrefs = it.datingPrefs.copy(selectedItems = items)
            )
        }
    }


    fun openSheet(type:ProfileBottomSheetType){
        _state.update {
            it.copy(
                showBottomSheet = true,
                editProfileBottomSheetType = type
            )
        }
    }

    fun hideSheet(){
        _state.update {
            it.copy(
                showBottomSheet = false
            )
        }
    }

    fun toggleBioVisibility(){
        _state.update {
            it.copy(
                bio = it.bio.copy(isVisibleOnProfile = !it.bio.isVisibleOnProfile)
            )
        }
    }

    fun changeFloatingBottomState(value:Boolean){
        _state.update {
            it.copy(
                isFloatingButtonVisible = value
            )
        }
    }




}