package com.jara.lavacampus.model
import com.jara.lavacampus.data.local.entity.Servicio
import java.math.BigDecimal
import java.math.RoundingMode
data class ImporteLinea(val servicioId:Int,val cantidad:Double,val precioUnitario:Double,val bruto:Double,val descuento:Double,val neto:Double)
data class PrecioPedido(val subtotal:Double,val descuento:Double,val total:Double,val lineas:List<ImporteLinea>)
object PreciosCampus {
    fun calcular(servicios:List<Servicio>,cantidades:Map<Int,Double>):PrecioPedido {
        require(cantidades.size <= Validaciones.MAX_LINEAS) { "Demasiados servicios." }
        val elegidos=cantidades.map { (id,cantidad) ->
            val s=requireNotNull(servicios.singleOrNull{it.id==id}) { "Servicio inválido." }
            require(s.activo && Validaciones.precioValido(s.precio)) { "Servicio no disponible o tarifa inválida." }
            require(Validaciones.cantidadValida(s.unidad,cantidad)) { "Cantidad inválida para ${s.nombre}." }
            s to cantidad
        }
        val aplica=elegidos.filter{it.first.unidad=="KG"}.sumOf{it.second}>5.0
        val lineas=elegidos.map{(s,cantidad)->
            val bruto=BigDecimal.valueOf(Validaciones.subtotal(cantidad,s.precio))
            val descuento=if(aplica && s.unidad=="KG") bruto.multiply(BigDecimal("0.10")).setScale(2,RoundingMode.HALF_UP) else BigDecimal.ZERO
            ImporteLinea(s.id,cantidad,s.precio,bruto.toDouble(),descuento.toDouble(),bruto.subtract(descuento).toDouble())
        }
        return PrecioPedido(Validaciones.total(lineas.map{it.bruto}),Validaciones.total(lineas.map{it.descuento}),Validaciones.total(lineas.map{it.neto}),lineas)
    }
}
