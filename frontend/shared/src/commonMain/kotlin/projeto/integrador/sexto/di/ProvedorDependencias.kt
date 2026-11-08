package projeto.integrador.sexto.di

import projeto.integrador.sexto.data.local.AuthStore
import projeto.integrador.sexto.data.remote.AquaMonitorApi

object ProvedorDependencias {

    val api: AquaMonitorApi by lazy { AquaMonitorApi() }
    val authStore: AuthStore by lazy { AuthStore() }
}
