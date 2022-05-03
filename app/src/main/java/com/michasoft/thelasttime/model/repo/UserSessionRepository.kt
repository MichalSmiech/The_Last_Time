package com.michasoft.thelasttime.model.repo

import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.util.IClosable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by mśmiech on 29.04.2022.
 */
class UserSessionRepository(
    val user: User,
    private val userRepository: UserRepository
) : IClosable {
    val closableList = LinkedList<IClosable>()

    override suspend fun close() {
        coroutineScope {
            closableList.forEach {
                launch { it.close() }
            }
        }
    }

    suspend fun logout() {
        close()
        userRepository.logout()
    }
}