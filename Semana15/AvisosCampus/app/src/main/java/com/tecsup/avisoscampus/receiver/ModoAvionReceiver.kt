package com.tecsup.avisoscampus.receiver

import android.content.*
class ModoAvionReceiver(private val alCambiar: (Boolean) -> Unit) : BroadcastReceiver() {
 override fun onReceive(context: Context, intent: Intent) { if(intent.action==Intent.ACTION_AIRPLANE_MODE_CHANGED) alCambiar(intent.getBooleanExtra("state",false)) }
}
