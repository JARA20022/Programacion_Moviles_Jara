package com.jara.lavacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jara.lavacampus.data.local.dao.DetalleDao
import com.jara.lavacampus.data.local.dao.PedidoDao
import com.jara.lavacampus.data.local.dao.ServicioDao
import com.jara.lavacampus.data.local.dao.UsuarioDao
import com.jara.lavacampus.data.local.entity.DetallePedido
import com.jara.lavacampus.data.local.entity.Pedido
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.entity.Usuario

@Database(
    entities = [
        Servicio::class,
        Usuario::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 5,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detalleDao(): DetalleDao

    companion object {
        val MIGRACION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE usuarios ADD COLUMN apellido TEXT NOT NULL DEFAULT ''")
                db.execSQL("""
                    INSERT INTO servicios(nombre, descripcion, unidad, precio, horasEntrega, activo, icono)
                    SELECT 'OTROS SERVICIOS', 'Servicio pendiente de configuración', 'UNIDAD', 0.0, 24, 0, 'wash'
                    WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE nombre = 'OTROS SERVICIOS')
                """)
            }
        }

        val MIGRACION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN descripcionOtros TEXT DEFAULT NULL")
            }
        }

        val MIGRACION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN estadoAtencion TEXT NOT NULL DEFAULT 'PENDIENTE'")
                db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN respuestaSoporte TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN soporteId INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN fechaRespuesta TEXT DEFAULT NULL")
            }
        }

        @Volatile
        private var INSTANCE: LavaDatabase? = null

        fun obtener(context: Context): LavaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LavaDatabase::class.java,
                    "lavacampus.db"
                ).addMigrations(MIGRACION_1_2, MIGRACION_2_3, MIGRACION_3_4, MIGRACION_4_5).build().also { INSTANCE = it }
            }
    }
}
