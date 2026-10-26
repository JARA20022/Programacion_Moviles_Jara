package com.jara.lavacampus.model
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
data class BorradorRepetido(val cantidades:Map<Int,Double>,val nota:String,val omitidos:Int)
object RepetirPedido {
 fun preparar(pedido:PedidoConDetalle,usuarioId:Int,catalogo:List<Servicio>):BorradorRepetido {
    require(usuarioId>0 && pedido.pedido.usuarioId==usuarioId){"No puedes repetir un pedido de otra cuenta."}
    require(pedido.pedido.estado in listOf("ENTREGADO","CANCELADO")){"Solo puedes repetir pedidos del historial."}
    require(pedido.detalles.size<=Validaciones.MAX_LINEAS){"Demasiados servicios."}
    val cantidades=linkedMapOf<Int,Double>();var omitidos=0
    pedido.detalles.groupBy{it.detalle.servicioId}.forEach{(id,lineas)->
        val cantidad=lineas.sumOf{it.detalle.cantidad};val servicio=catalogo.singleOrNull{it.id==id}
        if(servicio==null || !servicio.activo || !Validaciones.precioValido(servicio.precio) || !Validaciones.cantidadValida(servicio.unidad,cantidad)) omitidos++ else cantidades[id]=cantidad
    }
    return BorradorRepetido(cantidades,pedido.pedido.nota.takeIf{Validaciones.textoEditable(it)}.orEmpty(),omitidos)
 }
}
