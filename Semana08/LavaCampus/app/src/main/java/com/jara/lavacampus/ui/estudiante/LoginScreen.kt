package com.jara.lavacampus.ui.estudiante

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.Rol
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.ui.theme.colorDelRol
import com.jara.lavacampus.viewmodel.SesionViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(vm: SesionViewModel) {
 var correo by rememberSaveable { mutableStateOf("") }
 var clave by remember { mutableStateOf("") }
 var confirmacion by remember { mutableStateOf("") }
 var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
 val error by vm.error.collectAsStateWithLifecycle()
 val cargando by vm.cargando.collectAsStateWithLifecycle()
 val foco=LocalFocusManager.current
 val ingresar:()->Unit={
     foco.clearFocus()
     if (rol == Rol.SOPORTE && clave != confirmacion) {
         vm.setError("Las contraseñas no coinciden.")
     } else {
         vm.login(correo,clave,rol)
         clave = ""
         confirmacion = ""
     }
 }
 Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(horizontal=24.dp,vertical=20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(22.dp)) {
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
   IconoStudio("servicios",color=MaterialTheme.colorScheme.primary)
   Text("LavaCampus",style=MaterialTheme.typography.titleLarge)
  }
  EntradaStudio {
   Column(Modifier.widthIn(max=560.dp).fillMaxWidth()) {
    Box(Modifier.fillMaxWidth().height(136.dp),contentAlignment=Alignment.Center) { OrbitaStudio(Modifier.size(200.dp)) }
    Text("Un pendiente\nmenos en tu día.",style=MaterialTheme.typography.displaySmall)
    Spacer(Modifier.height(10.dp))
    Text("Tu ropa en buenas manos. Tu tiempo, de vuelta.",color=MaterialTheme.colorScheme.onSurfaceVariant,style=MaterialTheme.typography.bodyLarge)
   }
  }
  EntradaStudio(1) {
   Surface(modifier=Modifier.widthIn(max=560.dp).fillMaxWidth(),shape=RoundedCornerShape(30.dp),color=MaterialTheme.colorScheme.surface) {
    Column(Modifier.padding(22.dp).animateContentSize(),verticalArrangement=Arrangement.spacedBy(16.dp)) {
     Text("Entra a tu espacio",style=MaterialTheme.typography.titleLarge)
     FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Rol.entries.forEach { opcion ->
       FilterChip(selected=rol==opcion,onClick={
           rol=opcion;
           vm.limpiarError();
           clave="";
           confirmacion=""
       },enabled=!cargando,label={Text(opcion.titulo)},leadingIcon={IconoStudio(if(opcion==Rol.CLIENTE)"persona" else if(opcion==Rol.SOPORTE) "servicios" else "panel",Modifier.size(18.dp))})
      }
     }
     LavaTextField(valor=correo,onCambio={correo=it;vm.limpiarError()},etiqueta="Correo electrónico",habilitado=!cargando,maxLongitud=Validaciones.MAX_CORREO,onSiguiente={foco.moveFocus(FocusDirection.Down)})
     LavaTextField(valor=clave,onCambio={clave=it;vm.limpiarError()},etiqueta="Contraseña",esClave=true,habilitado=!cargando,maxLongitud=Validaciones.MAX_CLAVE,imeAction=if (rol == Rol.SOPORTE) ImeAction.Next else ImeAction.Done,onSiguiente={foco.moveFocus(FocusDirection.Down)},onListo=ingresar)
     if (rol == Rol.SOPORTE) {
         LavaTextField(valor=confirmacion,onCambio={confirmacion=it;vm.limpiarError()},etiqueta="Confirmar contraseña",esClave=true,habilitado=!cargando,maxLongitud=Validaciones.MAX_CLAVE,imeAction=ImeAction.Done,onListo=ingresar)
         Text("Vuelve a escribir tu clave para confirmar.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(start=16.dp))
     }
     error?.let { Text(it,color=MaterialTheme.colorScheme.error,modifier=Modifier.semantics{liveRegion=LiveRegionMode.Polite}) }
     LavaButton(texto="Ingresar →",onClick=ingresar,color=colorDelRol(rol),cargando=cargando)
    }
   }
  }
  Text("Lavandería del campus · Cuidado sin complicaciones",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
 }
}
