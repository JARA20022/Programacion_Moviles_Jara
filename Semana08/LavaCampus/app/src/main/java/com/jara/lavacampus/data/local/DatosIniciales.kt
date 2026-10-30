package com.jara.lavacampus.data.local

import androidx.room.withTransaction
import com.jara.lavacampus.data.local.entity.DetallePedido
import com.jara.lavacampus.data.local.entity.Pedido
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.entity.Usuario
import java.time.DayOfWeek
import java.time.LocalDate

object DatosIniciales {

    suspend fun sembrar(db: LavaDatabase) {
        db.withTransaction {
            // Toda la siembra se realiza una sola vez y en una transacción.
            if (db.usuarioDao().contar() > 0) return@withTransaction

            suspend fun guardarServicio(servicio: Servicio): Servicio {
                val id = db.servicioDao().insertar(servicio).toInt()
                return servicio.copy(id = id)
            }

            val lavado = guardarServicio(
                Servicio(
                    nombre = "Lavado y secado",
                    descripcion = "Lavado y secado de ropa por kilogramo",
                    unidad = "KG",
                    precio = 8.0,
                    horasEntrega = 24,
                    icono = "wash"
                )
            )

            val planchado = guardarServicio(
                Servicio(
                    nombre = "Planchado",
                    descripcion = "Planchado de prendas",
                    unidad = "PRENDA",
                    precio = 2.5,
                    horasEntrega = 48,
                    icono = "iron"
                )
            )

            val lavadoSeco = guardarServicio(
                Servicio(
                    nombre = "Lavado en seco",
                    descripcion = "Cuidado de prendas delicadas",
                    unidad = "PRENDA",
                    precio = 12.0,
                    horasEntrega = 48,
                    icono = "shirt"
                )
            )

            guardarServicio(
                Servicio(
                    nombre = "Edredón / frazada",
                    descripcion = "Lavado de edredones y frazadas",
                    unidad = "UNIDAD",
                    precio = 20.0,
                    horasEntrega = 48,
                    activo = false,
                    icono = "wash"
                )
            )

            guardarServicio(
                Servicio(
                    nombre = "OTROS SERVICIOS",
                    descripcion = "Servicio pendiente de configuración",
                    unidad = "UNIDAD",
                    precio = 0.0,
                    horasEntrega = 24,
                    activo = false,
                    icono = "wash"
                )
            )

            val ana = Usuario(
                id = 1,
                nombre = "Ana Torres",
                codigo = "U202310234",
                correo = "ana@correo.test",
                clave = "1234",
                rol = "CLIENTE"
            )
            val luis = Usuario(
                id = 2,
                nombre = "Luis Ramos",
                codigo = "U202310235",
                correo = "luis@correo.test",
                clave = "1234",
                rol = "CLIENTE"
            )
            val marta = Usuario(
                id = 3,
                nombre = "Marta Flores",
                codigo = "U202310236",
                correo = "marta@correo.test",
                clave = "1234",
                rol = "CLIENTE"
            )
            val admin = Usuario(
                id = 4,
                nombre = "Administrador",
                codigo = "",
                correo = "admin@lavacampus.test",
                clave = "admin",
                rol = "ADMIN"
            )

            db.usuarioDao().insertarTodos(listOf(ana, luis, marta, admin))

            val hoy = LocalDate.now()
            val lunes = hoy.minusDays((hoy.dayOfWeek.value - 1).toLong())

            suspend fun registrar(
                usuarioId: Int,
                fecha: LocalDate,
                estado: String,
                pagado: Boolean = false,
                casillero: String? = null,
                vararg lineas: Pair<Servicio, Double>
            ) {
                require(lineas.isNotEmpty())

                var entrega = fecha.plusDays(
                    (lineas.maxOf { it.first.horasEntrega } / 24).toLong()
                )
                if (entrega.dayOfWeek == DayOfWeek.SUNDAY) {
                    entrega = entrega.plusDays(1)
                }

                val total = lineas.sumOf { (servicio, cantidad) ->
                    servicio.precio * cantidad
                }

                val pedidoId = db.pedidoDao().insertar(
                    Pedido(
                        usuarioId = usuarioId,
                        fechaRegistro = fecha.toString(),
                        fechaEntrega = entrega.toString(),
                        estado = estado,
                        total = total,
                        casillero = casillero,
                        pagado = pagado
                    )
                ).toInt()

                db.detalleDao().insertarTodos(
                    lineas.map { (servicio, cantidad) ->
                        DetallePedido(
                            pedidoId = pedidoId,
                            servicioId = servicio.id,
                            cantidad = cantidad,
                            precioUnitario = servicio.precio,
                            subtotal = servicio.precio * cantidad
                        )
                    }
                )
            }

            registrar(
                ana.id, lunes, "ENTREGADO", pagado = true,
                lineas = arrayOf(lavado to 2.0, planchado to 1.0)
            )
            registrar(
                luis.id, hoy.minusDays(1), "ENTREGADO", pagado = true,
                lineas = arrayOf(lavado to 3.0, lavadoSeco to 1.0)
            )
            registrar(
                marta.id, hoy, "ENTREGADO", pagado = true,
                lineas = arrayOf(planchado to 4.0, lavado to 1.0)
            )
            registrar(
                ana.id, hoy.minusDays(1), "LISTO",
                casillero = "C-05",
                lineas = arrayOf(lavado to 2.5)
            )
            registrar(
                luis.id, hoy.minusDays(2), "LISTO",
                casillero = "C-07",
                lineas = arrayOf(lavadoSeco to 2.0, planchado to 2.0)
            )
            registrar(
                ana.id, hoy.minusDays(1), "EN_PROCESO",
                lineas = arrayOf(lavado to 1.5)
            )
            registrar(
                marta.id, hoy, "EN_PROCESO",
                lineas = arrayOf(lavado to 4.0, planchado to 2.0)
            )
            registrar(
                ana.id, hoy, "RECIBIDO",
                lineas = arrayOf(planchado to 5.0)
            )
            registrar(
                luis.id, hoy, "RECIBIDO",
                lineas = arrayOf(lavado to 3.5, planchado to 5.0)
            )
            registrar(
                marta.id, hoy.minusDays(3), "CANCELADO",
                lineas = arrayOf(lavado to 1.0)
            )
        }
    }
}
