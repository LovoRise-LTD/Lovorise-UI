package com.lovorise.app.libs.auth

import kotlinx.cinterop.BetaInteropApi
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class AppleAuthManager :  Oauth {

    // Suspend function to handle Apple login and return idToken
    override suspend fun login(): OAuthLoginData? {
        return suspendCoroutine { continuation ->
            handleAppleIdRequest(
                onSuccess = { data ->
                    continuation.resume(data)
                },
                onFailure = { error ->
                    continuation.resumeWithException(error)
                }
            )
        }
    }

    override suspend fun logout() {
        // No specific action needed for Apple Sign-out
    }

    // Function to handle the Apple ID request
    private fun handleAppleIdRequest(onSuccess: (OAuthLoginData?) -> Unit, onFailure: (Exception) -> Unit) {
        try {
            val appleIDProvider = ASAuthorizationAppleIDProvider()
            val request = appleIDProvider.createRequest()
            request.setRequestedScopes(listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail))

            val authorizationController = ASAuthorizationController(arrayOf(request).toList())
            val delegate = AppleAuthDelegate(onSuccess, onFailure)
            authorizationController.delegate = delegate
            authorizationController.performRequests()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override suspend fun isLastLoggedIn(): Boolean {
        return false
    }
}


class AppleAuthDelegate(
    private val onSuccess: (OAuthLoginData?) -> Unit,
    private val onFailure: (Exception) -> Unit
) : NSObject(), ASAuthorizationControllerDelegateProtocol {

    // Called when Apple login succeeds
    @OptIn(BetaInteropApi::class)
    override fun authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization: ASAuthorization) {
        val appleIDCredential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
        val idToken = appleIDCredential?.identityToken?.let { tokenData ->
            NSString.create(data = tokenData, encoding = NSUTF8StringEncoding)?.toString()
        }

        val email = appleIDCredential?.email

        // Pass the idToken to the success handler
        onSuccess(OAuthLoginData(idToken,email))
    }

    // Called when Apple login fails
    override fun authorizationController(controller: ASAuthorizationController, didCompleteWithError: NSError) {
        val errorCode = didCompleteWithError.code
        val errorDescription = didCompleteWithError.localizedDescription
        println("Apple Sign-In failed: Code: $errorCode, Description: $errorDescription")

        // Check specific error code for better handling
//        when (errorCode) {
//            ASAuthorizationErrorUnknown -> onFailure(Exception("Apple Sign-In failed: Unknown error (Code: 1000)"))
//            ASAuthorizationErrorCanceled -> onFailure(Exception("Apple Sign-In canceled by the user"))
//            ASAuthorizationErrorInvalidResponse -> onFailure(Exception("Invalid response from Apple Sign-In"))
//            ASAuthorizationErrorNotHandled -> onFailure(Exception("Request was not handled"))
//            ASAuthorizationErrorFailed -> onFailure(Exception("Apple Sign-In failed"))
//            else -> onFailure(Exception("Apple Sign-In error: $errorDescription"))
//        }
    }
}