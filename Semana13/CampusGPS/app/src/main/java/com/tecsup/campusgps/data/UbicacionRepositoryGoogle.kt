package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    private val app = context.applicationContext
    private val cliente by lazy { LocationServices.getFusedLocationProviderClient(app) }
    private val gestor = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(app) == ConnectionResult.SUCCESS

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = try {
        if (!LocationManagerCompat.isLocationEnabled(gestor)) throw UbicacionNoDisponible()
        val ubicacion = cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
        if (ubicacion == null) Result.failure(UbicacionNoDisponible())
        else Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
