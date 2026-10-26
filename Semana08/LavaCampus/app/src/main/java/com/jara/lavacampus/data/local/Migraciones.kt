package com.jara.lavacampus.data.local
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
val MIGRACION_1_2 = object:Migration(1,2) {
    override fun migrate(db:SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE pedidos ADD COLUMN descuento REAL NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE pedidos ADD COLUMN subtotal REAL NOT NULL DEFAULT 0")
        db.execSQL("UPDATE pedidos SET subtotal = total")
        db.execSQL("ALTER TABLE detalles_pedido ADD COLUMN descuento REAL NOT NULL DEFAULT 0")
    }
}
