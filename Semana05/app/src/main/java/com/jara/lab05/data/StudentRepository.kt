package com.jara.lab05.data

import com.jara.lab05.R

data class Student(
    val id: Int,
    val fullName: String,
    val shortName: String,
    val email: String,
    val career: String,
    val phone: String,
    val cycle: String,
    val studentIdCode: String,
    val faculty: String,
    val bio: String,
    val avatarResId: Int
)

object StudentRepository {
    val mainStudent = Student(
        id = 1,
        fullName = "Iván Jara Ayala",
        shortName = "Iván",
        email = "ivan.jara.a@tecsup.edu.pe",
        career = "Desarrollo de Software",
        phone = "902104462",
        cycle = "No registrado",
        studentIdCode = "2024-0001",
        faculty = "Ingeniería y Tecnología",
        bio = "Estudiante de Desarrollo de Software con interés en desarrollo Android.",
        avatarResId = R.drawable.perfil_ivan_jara
    )

    val students = listOf(
        mainStudent,
        Student(
            id = 2,
            fullName = "María García",
            shortName = "María",
            email = "maria.garcia@tecsup.edu.pe",
            career = "Arquitectura",
            phone = "912345678",
            cycle = "V Ciclo",
            studentIdCode = "2024-0002",
            faculty = "Diseño y Arquitectura",
            bio = "Estudiante de Arquitectura enfocada en diseño sostenible y proyectos urbanos.",
            avatarResId = R.drawable.perfil_maria_garcia
        ),
        Student(
            id = 3,
            fullName = "Carlos Perez",
            shortName = "Carlos",
            email = "carlos.perez@tecsup.edu.pe",
            career = "Medicina",
            phone = "923456789",
            cycle = "VI Ciclo",
            studentIdCode = "2024-0003",
            faculty = "Ciencias de la Salud",
            bio = "Estudiante de Medicina enfocado en investigación clínica y anatomía.",
            avatarResId = R.drawable.perfil_carlos_perez
        ),
        Student(
            id = 4,
            fullName = "Ana Lopez",
            shortName = "Ana",
            email = "ana.lopez@tecsup.edu.pe",
            career = "Derecho",
            phone = "934567890",
            cycle = "IV Ciclo",
            studentIdCode = "2024-0004",
            faculty = "Ciencias Jurídicas",
            bio = "Estudiante de Derecho interesada en derecho constitucional y corporativo.",
            avatarResId = R.drawable.perfil_ana_lopez
        ),
        Student(
            id = 5,
            fullName = "Luis Ramirez",
            shortName = "Luis",
            email = "luis.ramirez@tecsup.edu.pe",
            career = "Administración",
            phone = "945678901",
            cycle = "III Ciclo",
            studentIdCode = "2024-0005",
            faculty = "Gestión y Negocios",
            bio = "Estudiante de Administración apasionado por las finanzas y la gestión de proyectos.",
            avatarResId = R.drawable.perfil_luis_ramirez
        )
    )

    fun getStudentById(id: Int): Student? {
        return students.find { it.id == id }
    }
}