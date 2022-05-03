package com.michasoft.thelasttime.model.repo

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.dataSource.IUserDataSource
import com.michasoft.thelasttime.model.dataSource.RoomUserDataSource

/**
 * Created by mśmiech on 29.04.2022.
 */
class UserRepository(
    private val context: Context,
    private val userDataSource: IUserDataSource
) {
    var currentUser: User? = null

    suspend fun init() {
        var user = userDataSource.getCurrentUser()
        if (user != null) {
            val authUser = Firebase.auth.currentUser
            if (user.remoteId != null) {
                if (authUser == null) {
                    userDataSource.clearCurrentUserFlag()
                    user = null
                } else if (authUser.uid != user.remoteId) {
                    user = null
                    userDataSource.clearCurrentUserFlag()
                    logoutRemotely()
                }
            } else if(authUser != null) {
                logoutRemotely()
            }
        }
        currentUser = user
    }

    suspend fun getUserByRemoteId(remoteId: String): User? {
        return userDataSource.getUserByRemoteId(remoteId)
    }

    suspend fun insertUser(user: User) {
        userDataSource.insertUser(user)
    }

    suspend fun signIn(user: User): Boolean {
        if(Firebase.auth.currentUser?.uid != user.remoteId) {
            return false
        }
        userDataSource.setCurrentUserFlag(user.id)
        currentUser = user
        return true
    }

    suspend fun logout() {
        currentUser = null
        userDataSource.clearCurrentUserFlag()
        logoutRemotely()
    }

    private fun logoutRemotely() {
        AuthUI.getInstance().signOut(context)
    }
}