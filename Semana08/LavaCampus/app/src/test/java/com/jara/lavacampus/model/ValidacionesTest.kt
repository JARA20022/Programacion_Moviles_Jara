package com.jara.lavacampus.model

import com.jara.lavacampus.data.local.entity.Servicio
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ValidacionesTest {
    private fun servicio() = Servicio(nombre = "Lavado", descripcion = "Recojo a las 5", unidad = "KG", precio = 8.0, horasEntrega = 24, icono = "wash")
    private fun rechaza(accion: () -> Unit) {
        try { accion(); fail("Debió rechazar la entrada") } catch (_: IllegalArgumentException) { }
    }
    @Test fun nombresUnicodeYSeparadoresSinNumeros() {
        listOf("Planchado", "Lavado y secado", "Edredón / frazada", "Limpieza (especial)", "Edredo\u0301n").forEach { assertNull(it, Validaciones.errorNombre(it)) }
        listOf("", "  ", "AA", "Lavado123", "<script>", "Lavado\nsecado", "A".repeat(61), "Lavado\u0000").forEach { assertNotNull(it, Validaciones.errorNombre(it)) }
        assertEquals(Validaciones.claveNombre("  EDREDÓN   / frazada "), Validaciones.claveNombre("Edredo\u0301n / frazada"))
    }
    @Test fun precioSoloDecimalDosDecimalesYLimites() {
        mapOf("0.01" to 0.01, "8" to 8.0, "3,50" to 3.5, "9999.99" to 9999.99).forEach { (s,v) -> assertEquals(v, Validaciones.precio(s)!!, 0.0) }
        listOf("", "0", "-1", "+3", "1e3", "NaN", "Infinity", "10.123", "99999", "1,2,3", "12..5", " 8 ", "١٢", "9".repeat(10000)).forEach { assertNull(it, Validaciones.precio(it)) }
        listOf(Double.NaN, Double.POSITIVE_INFINITY, -1.0, 0.0, 1.001, 10000.0).forEach { assertFalse(Validaciones.precioValido(it)) }
        assertTrue(Validaciones.precioEditable("12,")); assertFalse(Validaciones.precioEditable("12a"))
    }
    @Test fun correoYLoginMantienenLasCuentasDePractica() {
        assertNull(Validaciones.errorLogin("ana@correo.test", "1234"))
        assertNull(Validaciones.errorLogin("ADMIN@LAVACAMPUS.TEST", "admin"))
        listOf("a..b@correo.test", ".a@correo.test", "a@-correo.test", "a@correo", "a b@correo.test", "a@@correo.test", "a@correo.123", "x".repeat(255)).forEach { assertFalse(it, Validaciones.correoValido(it)) }
        assertNotNull(Validaciones.errorLogin("ana@correo.test", "x".repeat(65)))
        assertNotNull(Validaciones.errorLogin("ana@correo.test", "abc\u0000"))
    }
    @Test fun textoLibreAdmiteNumerosPeroNoAbuso() {
        assertTrue(Validaciones.textoEditable("Recojo 2 prendas a las 5.\nGracias."))
        assertTrue(Validaciones.textoEditable("a".repeat(300)))
        assertFalse(Validaciones.textoEditable("a".repeat(301)))
        assertFalse(Validaciones.textoEditable("hola\u0000"))
        assertFalse(Validaciones.textoEditable("hola\u202E"))
    }
    @Test fun cantidadesValidasPorUnidad() {
        listOf(1.0, 1.5, 3.5, 10.0).forEach { assertTrue(Validaciones.cantidadValida("KG", it)) }
        listOf(0.0, 0.5, 1.25, 10.5, Double.NaN, Double.POSITIVE_INFINITY).forEach { assertFalse(Validaciones.cantidadValida("KG", it)) }
        listOf("PRENDA", "UNIDAD").forEach { u ->
            assertTrue(Validaciones.cantidadValida(u,30.0)); assertFalse(Validaciones.cantidadValida(u,1.5)); assertFalse(Validaciones.cantidadValida(u,31.0))
        }
        assertFalse(Validaciones.cantidadValida("OTRA", 1.0))
    }
    @Test fun lineasNoVaciasNiDuplicadasNiIdentificadoresInvalidos() {
        Validaciones.validarLineas(listOf(LineaPedido(1,3.5),LineaPedido(2,5.0)))
        rechaza { Validaciones.validarLineas(emptyList()) }
        rechaza { Validaciones.validarLineas(listOf(LineaPedido(1,1.0),LineaPedido(1,1.0))) }
        rechaza { Validaciones.validarLineas(listOf(LineaPedido(-1,1.0))) }
        rechaza { Validaciones.validarLineas(listOf(LineaPedido(1,Double.NaN))) }
        rechaza { Validaciones.validarLineas((1..51).map { LineaPedido(it,1.0) }) }
    }
    @Test fun servicioValidaCamposAunqueNoPasePorLaPantalla() {
        val s=servicio();Validaciones.validarServicio(s)
        rechaza { Validaciones.validarServicio(s.copy(id=-1)) }
        rechaza { Validaciones.validarServicio(s.copy(nombre="123")) }
        rechaza { Validaciones.validarServicio(s.copy(precio=Double.NaN)) }
        rechaza { Validaciones.validarServicio(s.copy(unidad="INVALIDA")) }
        rechaza { Validaciones.validarServicio(s.copy(horasEntrega=25)) }
        rechaza { Validaciones.validarServicio(s.copy(icono="otro")) }
        rechaza { Validaciones.validarServicio(s.copy(descripcion="a".repeat(301))) }
    }
    @Test fun importesCentradosEnCentimosYEntregaSinDomingo() {
        assertEquals(40.50, Validaciones.total(listOf(Validaciones.subtotal(3.5,8.0),Validaciones.subtotal(5.0,2.5))),0.0)
        assertEquals(4.49, Validaciones.subtotal(1.5,2.99),0.0)
        assertEquals(0.3,Validaciones.total(listOf(0.1,0.2)),0.0)
        assertEquals(LocalDate.of(2026,10,26),Validaciones.fechaEntrega(48,LocalDate.of(2026,10,23)))
        assertEquals(LocalDate.of(2026,10,26),Validaciones.fechaEntrega(24,LocalDate.of(2026,10,24)))
        rechaza { Validaciones.fechaEntrega(-1) }
    }
}
