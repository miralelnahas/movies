package com.areeb.domain.usecases

import com.areeb.data.managers.ConnectionManager
import javax.inject.Inject

class IsNetworkConnectedUseCase @Inject constructor(private val connectionManager: ConnectionManager) {
    operator fun invoke() = connectionManager.isNetworkAvailable()
}