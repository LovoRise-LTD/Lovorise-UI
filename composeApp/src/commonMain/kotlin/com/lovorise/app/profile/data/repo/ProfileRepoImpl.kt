package com.lovorise.app.profile.data.repo

import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.libs.location.LocationData
import com.lovorise.app.profile.data.source.remote.UserProfileApiService
import com.lovorise.app.profile.data.source.remote.dto.ProfileUpdateRequest
import com.lovorise.app.profile.data.source.remote.dto.UpdatePreferencesRequest
import com.lovorise.app.profile.domain.repo.ProfilePrefsUpdateItem
import com.lovorise.app.profile.domain.repo.ProfileRepo
import com.lovorise.app.profile.domain.repo.ProfileUpdateItem
import com.lovorise.app.profile.domain.repo.UpdateProfileData
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.GenderData
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepoImpl(
    private val profileApiService: UserProfileApiService
) : ProfileRepo {

    override suspend fun updateProfile(item: ProfileUpdateItem, data: UpdateProfileData<Any>, token: String): Flow<Resource<Boolean>> {
        return flow {

            emit(Resource.Loading(true))

            val reqData = when (item) {
                ProfileUpdateItem.NAME -> {
                    ProfileUpdateRequest(name = data.value as String)
                }

                ProfileUpdateItem.AGE -> {
                    ProfileUpdateRequest(age = data.value as Int, ageVisible = data.isVisible)
                }

                ProfileUpdateItem.GENDER -> {
                    val gender = data.value as GenderData
                    ProfileUpdateRequest(gender = gender.gender, anotherGender = gender.moreAboutGender,genderVisible = data.isVisible)
                }

                ProfileUpdateItem.HEIGHT -> {
                    ProfileUpdateRequest(height = data.value as Int, heightVisible = data.isVisible)
                }

                ProfileUpdateItem.EDUCATION -> {
                    ProfileUpdateRequest(education = data.value as String, educationVisible = data.isVisible)
                }

                ProfileUpdateItem.RELIGION -> {
                    ProfileUpdateRequest(religion = data.value as String, religionVisible = data.isVisible)
                }

                ProfileUpdateItem.LANGUAGE -> {
                    ProfileUpdateRequest(language = data.value as List<String>, languageVisible = data.isVisible)
                }

                ProfileUpdateItem.DOB -> {
                    ProfileUpdateRequest(birthDateVisible = data.isVisible, birthDate = data.value as String)
                }

                ProfileUpdateItem.PETS -> {
                    ProfileUpdateRequest(petsVisible = data.isVisible, pets = data.value as List<String>)
                }

                ProfileUpdateItem.BIO -> {
                    ProfileUpdateRequest(bio = data.value as String, bioVisible = data.isVisible)
                }

                ProfileUpdateItem.ETHNICITY -> {
                    ProfileUpdateRequest(ethnicity = data.value as String, ethnicityVisible = data.isVisible)
                }

                ProfileUpdateItem.PROFESSION -> {
                    val profession = data.value as EditProfileScreenState.Profession
                    ProfileUpdateRequest(profession = UserResponseDto.UserRes.ProfessionDto(professionVisible = data.isVisible, jobTitle = profession.jobTitle, company = profession.orgName))
                }

                ProfileUpdateItem.LOCATION -> {
                    val location = data.value as LocationData
//                    ProfileUpdateRequest(location = ProfileUpdateRequest.LocationDataDto(country = "England", city = "London", longitude = 0.1276, latitude = 51.5072))
                    ProfileUpdateRequest(location = ProfileUpdateRequest.LocationDataDto(country = location.country, city = location.city, longitude = location.longitude, latitude = location.latitude))
                }
            }

            val response = profileApiService.modifyProfile(reqData, token)

            emit(Resource.Loading(false))

            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Update operation failed"))
            }

        }
    }


    override suspend fun updateUserPrefs(item: ProfilePrefsUpdateItem, data: UpdateProfileData<Any>, token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))

            val reqData = when (item) {
                ProfilePrefsUpdateItem.TYPE_OF_RELATION -> UpdatePreferencesRequest(typeOfRelation = data.value as List<String>)
                ProfilePrefsUpdateItem.FAMILY -> UpdatePreferencesRequest(familyVisible = data.isVisible, family = data.value as String)
                ProfilePrefsUpdateItem.DRINK -> UpdatePreferencesRequest(drinking = data.value as String, drinkingVisible = data.isVisible)
                ProfilePrefsUpdateItem.SMOKE -> UpdatePreferencesRequest(smoking = data.value as String, smokingVisible = data.isVisible)
                ProfilePrefsUpdateItem.WHO_WOULD_YOU_LIKE_TO_MEET -> UpdatePreferencesRequest(whoWouldYouLikeToMeet = data.value as List<String>, whoWouldYouLikeToMeetVisible = data.isVisible)
                ProfilePrefsUpdateItem.AGE_RANGE -> UpdatePreferencesRequest(ageStart = (data.value as Pair<Int,Int>).first, ageEnd = data.value.second)
                ProfilePrefsUpdateItem.DISTANCE -> UpdatePreferencesRequest(distance = data.value as Double)
            }

            val response = profileApiService.modifyPreference(reqData, token)

            emit(Resource.Loading(false))

            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Update operation failed"))
            }
        }
    }

    override suspend fun updateAgeRangeAndDistance(token: String, ageRange: Pair<Int, Int>, distance: Double, distanceMeasurement: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = profileApiService.modifyPreference(UpdatePreferencesRequest(ageStart = ageRange.first, ageEnd = ageRange.second, distance = distance, distanceMeasurement = distanceMeasurement.ifBlank { null }), token)

            emit(Resource.Loading(false))

            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Update operation failed"))
            }
        }
    }

    override suspend fun updateUserInterests(data: Map<String, List<String>>, token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = profileApiService.modifyInterests(data, token)
            emit(Resource.Loading(false))
            if (response) {
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Couldn't update interests"))
            }
        }
    }
}