package com.jara.lavacampus.model
import com.jara.lavacampus.data.local.entity.*
import com.jara.lavacampus.data.local.relation.*
import org.junit.Assert.*
import org.junit.Test
class RepetirPedidoTest {
 private val s=Servicio(1,"Lavado","","KG",8.0,24,true,"wash")
 private val u=Usuario(1,"Ana","U001","ana@correo.test","1234","CLIENTE")
 private fun p(estado:String="ENTREGADO")=PedidoConDetalle(Pedido(1,1,"2026-10-20","2026-10-22",estado,28.0,nota="Sin perfume"),u,listOf(DetalleConServicio(DetallePedido(1,1,1,3.5,8.0,28.0),s)))
 @Test fun precioVigente(){val actual=s.copy(precio=10.0);val b=RepetirPedido.preparar(p(),1,listOf(actual));assertEquals(3.5,b.cantidades[1]!!,0.0);assertEquals(35.0,PreciosCampus.calcular(listOf(actual),b.cantidades).total,0.0);assertEquals("Sin perfume",b.nota)}
 @Test fun omiteInactivo(){val b=RepetirPedido.preparar(p(),1,listOf(s.copy(activo=false)));assertTrue(b.cantidades.isEmpty());assertEquals(1,b.omitidos)}
 @Test fun cambioUnidad(){assertTrue(RepetirPedido.preparar(p(),1,listOf(s.copy(unidad="PRENDA"))).cantidades.isEmpty())}
 @Test(expected=IllegalArgumentException::class) fun otraCuenta(){RepetirPedido.preparar(p(),2,listOf(s))}
 @Test(expected=IllegalArgumentException::class) fun activo(){RepetirPedido.preparar(p("RECIBIDO"),1,listOf(s))}
}
