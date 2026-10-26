package com.jara.lavacampus.model
import com.jara.lavacampus.data.local.entity.Servicio
import org.junit.Assert.*
import org.junit.Test
class PreciosCampusTest {
 private val servicios=listOf(Servicio(1,"Lavado","","KG",8.0,24,true,"wash"),Servicio(2,"Planchado","","PRENDA",2.5,48,true,"iron"))
 @Test fun conservaEjemploGuia(){val p=PreciosCampus.calcular(servicios,mapOf(1 to 3.5,2 to 5.0));assertEquals(40.50,p.total,0.0);assertEquals(0.0,p.descuento,0.0)}
 @Test fun limiteEstricto(){assertEquals(0.0,PreciosCampus.calcular(servicios,mapOf(1 to 5.0)).descuento,0.0);assertEquals(4.40,PreciosCampus.calcular(servicios,mapOf(1 to 5.5)).descuento,0.0)}
 @Test fun soloLavado(){val p=PreciosCampus.calcular(servicios,mapOf(1 to 6.0,2 to 5.0));assertEquals(60.5,p.subtotal,0.0);assertEquals(4.8,p.descuento,0.0);assertEquals(55.7,p.total,0.0);assertEquals(0.0,p.lineas[1].descuento,0.0);assertEquals(p.total,Validaciones.total(p.lineas.map{it.neto}),0.0)}
 @Test fun centimos(){val p=PreciosCampus.calcular(listOf(servicios[0].copy(precio=2.99)),mapOf(1 to 5.5));assertEquals(16.45,p.subtotal,0.0);assertEquals(1.65,p.descuento,0.0);assertEquals(14.8,p.total,0.0)}
 @Test(expected=IllegalArgumentException::class) fun noNaN(){PreciosCampus.calcular(servicios,mapOf(1 to Double.NaN))}
 @Test(expected=IllegalArgumentException::class) fun noInactivos(){PreciosCampus.calcular(servicios.map{it.copy(activo=false)},mapOf(1 to 6.0))}
}
