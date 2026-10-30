package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.data.prefs.PreferenciasStore
import com.jara.lavacampus.model.LineaPedido
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.model.PreciosCampus
import com.jara.lavacampus.model.RepetirPedido
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.ceil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class NuevoPedidoEstado(
    val servicios: List<Servicio> = emptyList(),
    val cantidades: Map<Int, Double> = emptyMap(),
    val descripcionesOtros: Map<Int, String> = emptyMap(),
    val nota: String = "",
    val total: Double = 0.0,
    val subtotal: Double = 0.0,
    val descuento: Double = 0.0,
    val fechaEntrega: String? = null,
    val cargando: Boolean = true,
    val cargandoRepeticion: Boolean = false,
    val aviso: String? = null,
    val guardando: Boolean = false,
    val recibirPedidos: Boolean = false,
    val error: String? = null,
    val pedidoCreado: Long? = null
) {
    val puedeConfirmar: Boolean
        get() = recibirPedidos && !cargando && !cargandoRepeticion && !guardando && pedidoCreado == null && cantidades.values.any { it > 0.0 }
}

class NuevoPedidoViewModel(
    private val estudiante: Usuario,
    private val repo: LavaRepository,
    private val preferencias: PreferenciasStore,
    private val pedidoOrigenId: Int? = null
) : ViewModel() {
    private val _estado = MutableStateFlow(NuevoPedidoEstado(cargandoRepeticion = pedidoOrigenId != null))
    val estado = _estado.asStateFlow()

    private var repeticionPendiente = pedidoOrigenId != null
    init {
        viewModelScope.launch {
            preferencias.recibirPedidos.catch { e ->
                if (e is CancellationException) throw e
                _estado.value = _estado.value.copy(error = "No se pudo consultar la disponibilidad de la lavandería.")
                emit(false)
            }.collect { abierto -> _estado.value = _estado.value.copy(recibirPedidos = abierto) }
        }
        viewModelScope.launch {
            repo.observarServiciosActivos()
                .catch { e ->
                    if (e is CancellationException) throw e
                    _estado.value = _estado.value.copy(
                        cargando = false,
                        error = "No se pudieron cargar los servicios. Vuelve a abrir Nuevo pedido."
                    )
                }
                .collect { servicios ->
                    _estado.value = recalcular(_estado.value.copy(servicios = servicios, cargando = false))
                    if(repeticionPendiente){repeticionPendiente=false;cargarRepeticion(requireNotNull(pedidoOrigenId))}
                }
        }
    }

    private suspend fun cargarRepeticion(id:Int) {
        try {
            require(id>0){"Pedido de origen inválido."}
            val original=requireNotNull(repo.obtenerPedido(id)){"El pedido original ya no existe."}
            val borrador=RepetirPedido.preparar(original,estudiante.id,_estado.value.servicios)
            val aviso=if(borrador.cantidades.isEmpty()) "No quedan servicios disponibles para repetir este pedido. Elige otros."
                else "Pedido #$id cargado con precios actuales. Revisa y confirma." + (if(borrador.omitidos>0) " Se omitieron ${borrador.omitidos} servicios no disponibles o con cantidades incompatibles." else "")
            _estado.value=recalcular(_estado.value.copy(cantidades=borrador.cantidades,descripcionesOtros=borrador.descripcionesOtros,nota=borrador.nota,aviso=aviso))
        } catch(e:CancellationException){throw e
        } catch(e:IllegalArgumentException){_estado.value=_estado.value.copy(error=e.message)
        } catch(e:Exception){_estado.value=_estado.value.copy(error="No se pudo recuperar el pedido. Vuelve a intentarlo.")
        } finally {_estado.value=_estado.value.copy(cargandoRepeticion=false)}
    }

    private fun recalcular(actual: NuevoPedidoEstado): NuevoPedidoEstado {
        val idsActivos = actual.servicios.map { it.id }.toSet()
        val cantidades = actual.cantidades.filter { (id, cantidad) -> id in idsActivos && cantidad > 0.0 }
        val elegidos = actual.servicios.filter { it.id in cantidades }
        val precio = PreciosCampus.calcular(elegidos, cantidades)
        val fecha = elegidos.maxOfOrNull { it.horasEntrega }?.let { horas ->
            var entrega = LocalDate.now().plusDays(ceil(horas / 24.0).toLong())
            if (entrega.dayOfWeek == DayOfWeek.SUNDAY) entrega = entrega.plusDays(1)
            entrega.toString()
        }
        return actual.copy(cantidades = cantidades, subtotal = precio.subtotal, descuento = precio.descuento, total = precio.total, fechaEntrega = fecha)
    }

    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val actual = _estado.value
        if (actual.cargando || actual.cargandoRepeticion || actual.guardando || actual.pedidoCreado != null) return
        val servicio = actual.servicios.firstOrNull { it.id == servicioId } ?: return
        val esKg = servicio.unidad == "KG"
        val paso = if (esKg) 0.5 else 1.0
        if (delta != paso && delta != -paso) return
        val limite = if (esKg) 10.0 else 30.0
        val anterior = actual.cantidades[servicioId] ?: 0.0
        val siguiente = when {
            anterior == 0.0 && delta > 0.0 -> 1.0
            anterior == 1.0 && delta < 0.0 -> 0.0
            else -> (anterior + delta).coerceIn(0.0, limite)
        }
        val cantidades = actual.cantidades.toMutableMap()
        val descripcionesOtros = actual.descripcionesOtros.toMutableMap()
        if (siguiente == 0.0) {
            cantidades.remove(servicioId)
            descripcionesOtros.remove(servicioId)
        } else {
            cantidades[servicioId] = siguiente
        }
        _estado.value = recalcular(actual.copy(cantidades = cantidades, descripcionesOtros = descripcionesOtros, error = null))
    }

    fun cambiarDescripcionOtros(servicioId: Int, descripcion: String) {
        val actual = _estado.value
        if (actual.cargandoRepeticion || actual.guardando || actual.pedidoCreado != null) return
        val descripciones = actual.descripcionesOtros.toMutableMap()
        descripciones[servicioId] = descripcion
        _estado.value = actual.copy(descripcionesOtros = descripciones, error = null)
    }

    fun cambiarNota(nota: String) {
        if (!_estado.value.cargandoRepeticion && !_estado.value.guardando && _estado.value.pedidoCreado == null) {
            val errorNota = Validaciones.errorTexto(nota, "La nota")
            if (errorNota != null) { _estado.value = _estado.value.copy(error = errorNota); return }
            _estado.value = _estado.value.copy(nota = nota, error = null)
        }
    }

    fun confirmar() {
        val solicitud = _estado.value
        if (!solicitud.recibirPedidos) {
            _estado.value = solicitud.copy(error = "La lavandería no está recibiendo pedidos")
            return
        }
        if (!solicitud.puedeConfirmar) return
        _estado.value = solicitud.copy(guardando = true, error = null)
        viewModelScope.launch {
            try {
                require(Validaciones.errorTexto(solicitud.nota, "La nota") == null) { "La nota no es válida." }
                Validaciones.validarLineas(solicitud.cantidades.map { (id, cantidad) -> LineaPedido(id, cantidad, solicitud.descripcionesOtros[id]) })
                solicitud.cantidades.keys.forEach { id ->
                    val servicio = solicitud.servicios.firstOrNull { it.id == id }
                    if (servicio?.nombre == "OTROS SERVICIOS") {
                        val descripcion = solicitud.descripcionesOtros[id]
                        require(descripcion != null && Validaciones.errorDescripcionOtros(descripcion) == null) { "Describe el servicio solicitado en OTROS SERVICIOS." }
                    }
                }
                require(preferencias.recibirPedidos.first()) { "La lavandería no está recibiendo pedidos" }
                val id = repo.enTransaccion {
                    require(repo.contarActivos(estudiante.id) < 3) { "Ya tienes 3 pedidos activos" }
                    val serviciosActuales = solicitud.cantidades.keys.map { id ->
                        val servicio = requireNotNull(repo.obtenerServicio(id)) { "Un servicio ya no existe. Revisa tu pedido." }
                        require(Validaciones.cantidadValida(servicio.unidad, solicitud.cantidades.getValue(id))) { "Cantidad inválida para ${servicio.nombre}." }
                        require(Validaciones.precioValido(servicio.precio)) { "La tarifa del servicio no es válida." }
                        require(servicio.activo) { "Un servicio fue desactivado. Revisa tu pedido." }
                        require(servicio.precio == solicitud.servicios.firstOrNull { it.id == id }?.precio) { "Los precios cambiaron. Revisa el resumen y confirma nuevamente." }
                        servicio
                    }
                    val revisada = recalcular(solicitud.copy(servicios = serviciosActuales))
                    repo.crearPedido(
                        usuarioId = estudiante.id,
                        lineas = revisada.cantidades.map { (id, cantidad) -> 
                            LineaPedido(id, cantidad, revisada.descripcionesOtros[id]) 
                        },
                        fechaEntrega = requireNotNull(revisada.fechaEntrega), nota = revisada.nota.trim()
                    )
                }
                _estado.value = _estado.value.copy(guardando = false, pedidoCreado = id)
            } catch (e: CancellationException) { throw e
            } catch (e: IllegalArgumentException) {
                _estado.value = _estado.value.copy(guardando = false, error = e.message)
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(guardando = false, error = "No se pudo guardar el pedido. Intenta nuevamente.")
            }
        }
    }
}
