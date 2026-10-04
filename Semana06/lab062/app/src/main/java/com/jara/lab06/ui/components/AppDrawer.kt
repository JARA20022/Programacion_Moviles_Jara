package com.jara.lab06.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.lab06.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    cantidadFavoritos: Int,
    rutaActual: String?,
    onSeleccionar: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.widthIn(max = 320.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Encabezado del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color(0xFFEDE3F4)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "IJ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5B2C83)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Ivan Jara Ayala",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF242128)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "ivan.jara.a@tecsup.edu.pe",
                        fontSize = 12.sp,
                        color = Color(0xFF665176)
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFDEDEE2)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Items del menú lateral con separación vertical de 12dp entre filas
            val itemModifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .heightIn(min = 56.dp)

            val drawerColors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFFEEE5F5),
                unselectedContainerColor = Color.Transparent,
                selectedIconColor = Color(0xFF5B2C83),
                unselectedIconColor = Color(0xFF242128),
                selectedTextColor = Color(0xFF5B2C83),
                unselectedTextColor = Color(0xFF242128)
            )

            val itemShape = RoundedCornerShape(16.dp)

            // Inicio
            val inicioSelected = rutaActual == "inicio" || rutaActual == "detalle/{productoId}"
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Inicio",
                        fontSize = 16.sp,
                        fontWeight = if (inicioSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = inicioSelected,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_home),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = itemShape,
                colors = drawerColors,
                modifier = itemModifier,
                onClick = { onSeleccionar("inicio") }
            )

            // Mis pedidos
            val pedidosSelected = rutaActual == "pedidos"
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Mis pedidos",
                        fontSize = 16.sp,
                        fontWeight = if (pedidosSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = pedidosSelected,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_shopping_cart),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = itemShape,
                colors = drawerColors,
                modifier = itemModifier,
                onClick = { onSeleccionar("pedidos") }
            )

            // Favoritos
            val favoritosSelected = rutaActual == "favoritos"
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Favoritos",
                        fontSize = 16.sp,
                        fontWeight = if (favoritosSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = favoritosSelected,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_favorite),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                badge = {
                    Badge(
                        modifier = Modifier
                            .height(24.dp)
                            .defaultMinSize(minWidth = 24.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        containerColor = Color(0xFF5B2C83),
                        contentColor = Color.White
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cantidadFavoritos.toString(),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                },
                shape = itemShape,
                colors = drawerColors,
                modifier = itemModifier,
                onClick = { onSeleccionar("favoritos") }
            )

            // Perfil
            val perfilSelected = rutaActual == "perfil"
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Perfil",
                        fontSize = 16.sp,
                        fontWeight = if (perfilSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = perfilSelected,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_person),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                shape = itemShape,
                colors = drawerColors,
                modifier = itemModifier,
                onClick = { onSeleccionar("perfil") }
            )
        }
    }
}
