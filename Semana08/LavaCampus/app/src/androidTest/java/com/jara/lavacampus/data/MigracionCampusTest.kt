package com.jara.lavacampus.data
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jara.lavacampus.data.local.LavaDatabase
import com.jara.lavacampus.data.local.MIGRACION_1_2
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class MigracionCampusTest {
 @Test fun migracionConservaDatosYRoomValidaEsquema() {
  val context=ApplicationProvider.getApplicationContext<android.content.Context>();val nombre="prueba_migracion_campus.db"
  context.deleteDatabase(nombre);val archivo=context.getDatabasePath(nombre);archivo.parentFile?.mkdirs()
  try {
   SQLiteDatabase.openOrCreateDatabase(archivo,null).use { db ->
    db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, codigo TEXT NOT NULL, correo TEXT NOT NULL, clave TEXT NOT NULL, rol TEXT NOT NULL)")
    db.execSQL("CREATE TABLE servicios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, descripcion TEXT NOT NULL, unidad TEXT NOT NULL, precio REAL NOT NULL, horasEntrega INTEGER NOT NULL, activo INTEGER NOT NULL, icono TEXT NOT NULL)")
    db.execSQL("CREATE TABLE pedidos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, usuarioId INTEGER NOT NULL, fechaRegistro TEXT NOT NULL, fechaEntrega TEXT NOT NULL, estado TEXT NOT NULL, total REAL NOT NULL, casillero TEXT, pagado INTEGER NOT NULL, nota TEXT NOT NULL, fechaCreacion INTEGER NOT NULL, FOREIGN KEY(usuarioId) REFERENCES usuarios(id) ON UPDATE NO ACTION ON DELETE NO ACTION)")
    db.execSQL("CREATE INDEX index_pedidos_usuarioId ON pedidos(usuarioId)")
    db.execSQL("CREATE INDEX index_pedidos_estado ON pedidos(estado)")
    db.execSQL("CREATE TABLE detalles_pedido (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pedidoId INTEGER NOT NULL, servicioId INTEGER NOT NULL, cantidad REAL NOT NULL, precioUnitario REAL NOT NULL, subtotal REAL NOT NULL, FOREIGN KEY(pedidoId) REFERENCES pedidos(id) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(servicioId) REFERENCES servicios(id) ON UPDATE NO ACTION ON DELETE NO ACTION)")
    db.execSQL("CREATE INDEX index_detalles_pedido_pedidoId ON detalles_pedido(pedidoId)")
    db.execSQL("CREATE INDEX index_detalles_pedido_servicioId ON detalles_pedido(servicioId)")
    db.execSQL("INSERT INTO usuarios VALUES(1,'Ana','U001','ana@correo.test','1234','CLIENTE')")
    db.execSQL("INSERT INTO servicios VALUES(1,'Lavado','','KG',8.0,24,1,'wash')")
    db.execSQL("INSERT INTO pedidos VALUES(1,1,'2026-10-20','2026-10-21','ENTREGADO',48.0,NULL,1,'conservar',1000)")
    db.execSQL("INSERT INTO detalles_pedido VALUES(1,1,1,6.0,8.0,48.0)")
    db.version=1
   }
   val room=Room.databaseBuilder(context,LavaDatabase::class.java,nombre).addMigrations(MIGRACION_1_2).build()
   try {
    val db=room.openHelper.writableDatabase
    db.query("SELECT total,subtotal,descuento,nota FROM pedidos WHERE id=1").use{c->assertTrue(c.moveToFirst());assertEquals(48.0,c.getDouble(0),0.0);assertEquals(48.0,c.getDouble(1),0.0);assertEquals(0.0,c.getDouble(2),0.0);assertEquals("conservar",c.getString(3))}
    db.query("SELECT subtotal,descuento FROM detalles_pedido WHERE id=1").use{c->assertTrue(c.moveToFirst());assertEquals(48.0,c.getDouble(0),0.0);assertEquals(0.0,c.getDouble(1),0.0)}
    db.query("PRAGMA foreign_key_check").use{assertFalse(it.moveToFirst())}
   } finally {room.close()}
  } finally {context.deleteDatabase(nombre)}
 }
}
