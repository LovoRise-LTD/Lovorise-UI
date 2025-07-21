package com.lovorise.app.libs.auth

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import coil3.PlatformContext
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.lovorise.app.util.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

actual class GoogleOauth actual constructor(private val context: PlatformContext) : Oauth {

    private val credentialManager:CredentialManager = CredentialManager.create(context)

    override suspend fun login():OAuthLoginData? = suspendCancellableCoroutine { continuation ->

//        val rawNonce = UUID.randomUUID().toString()
//        val bytes = rawNonce.toByteArray()
//        val messageDigest = MessageDigest.getInstance("SHA-256")
//        val digest = messageDigest.digest(bytes)
//        val hashedNonce = digest.fold(""){str,it -> str + "%20x".format(it)}
        try {
            val googleIdOption = GetSignInWithGoogleOption.Builder(AppConstants.GOOGLE_AUTH_WEB_CLIENT_ID)
                //  .setFilterByAuthorizedAccounts(true)
                //  .setServerClientId(AppConstants.GOOGLE_AUTH_WEB_CLIENT_ID)
                // .setAutoSelectEnabled(true)
                // .setNonce(hashedNonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val result = credentialManager.getCredential(context,request)
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(result.credential.data)

                    val email = googleIdTokenCredential.id // Email

                    println("the bundle email is $email")
                    continuation.resume(OAuthLoginData(googleIdTokenCredential.idToken,email),onCancellation = null)
                    println("the creds are ${result.credential.data} ${result.credential.type}")

                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            // Handle cancellation (if the coroutine is cancelled, cancel the request)
            continuation.invokeOnCancellation {}

        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    override suspend fun logout(){
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }catch (e:ClearCredentialException){
            e.printStackTrace()
        }
    }

    override suspend fun isLastLoggedIn(): Boolean {
        return false
//        val  mGoogleApiClient =  GoogleApiClient.Builder(context.applicationContext)
//            .addScope()
//            .build()
//
//        return mGoogleApiClient.isConnected
    }
}











