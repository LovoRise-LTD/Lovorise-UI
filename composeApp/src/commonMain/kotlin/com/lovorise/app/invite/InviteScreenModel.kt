package com.lovorise.app.invite

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.lovorise_hearts.domain.PurchaseRepo
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InviteScreenModel(
    private val purchaseRepo: PurchaseRepo
) : ScreenModel {

    private val _state = MutableStateFlow(InviteScreenState())
    val state = _state.asStateFlow()

    fun updateInviteFriendData(context:PlatformContext){
        if (!state.value.data?.inviteUrl.isNullOrBlank()) return
        val prefs = SharedPrefsImpl(context)
        val token = prefs.getString(PreferencesKeys.AUTH_TOKEN) ?: return
        screenModelScope.launch {
            purchaseRepo.inviteFriends(token).collectLatest { res ->
                when(res){
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                data = res.data
                            )
                        }
                    }

                    is Resource.Loading ->{
                        _state.update {
                            it.copy(isLoading = res.isLoading)
                        }
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }


}