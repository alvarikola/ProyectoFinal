# 🎧 Sala404 – Proyecto Final

Aplicación móvil desarrollada en **Kotlin Multiplataforma** que permite a los usuarios **crear salas de música sincronizada** con otros usuarios. Incluye funcionalidades sociales como **chat con emotes** y **perfiles personalizables**.

---

## 📚 Índice

- [🔍 Descripción General](#-descripción-general)
- [🖼 Diseño de la Aplicación](#-diseño-de-la-aplicación)
- [✅ Requisitos Funcionales](#-requisitos-funcionales)
- [🧩 Modelo de Datos](#-modelo-de-datos)
- [🛠 Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [🔧 Detalles del Sistema](#-detalles-del-sistema)
- [📆 Cronograma](#-cronograma)
- [📌 Créditos / Recursos](#-créditos--recursos)

---

## 🔍 Descripción General

**Sala404** es una app en la que los usuarios pueden crear salas virtuales para **escuchar música en sincronía** con otros participantes. Además de la música, la app incluye:

- **Chat en tiempo real** con emotes (estáticos y animados)
- **Perfiles de usuario personalizables** (nombre, país, avatar)

---

## 🖼 Diseño de la Aplicación

### 📱 Figma (Prediseño)

> ![Figma](https://github.com/user-attachments/assets/d40abcd9-891d-42ff-b53a-708b208c571b)

### 🪩 Logo de la App

> <img src="https://github.com/user-attachments/assets/4d8be488-fa33-4366-97a0-9dc4381b81cd" width="200" />

---

## ✅ Requisitos Funcionales

- [x] Los usuarios pueden crear salas.
- [x] En las salas se reproduce música sincronizada.
- [x] El creador de la sala elige la música.
- [x] Perfiles de usuario personalizables.
- [x] Chat con soporte para emotes.

---

## 🧩 Modelo de Datos

### 🔸 Entidades principales

#### 🧑 Perfil

- `ID` (PK)
- `created_at`
- `nombre`
- `email`
- `track_id`
- `fecha_inicio_cancion`
- `pais`
- `emote_id`

#### 🎵 Canción

- `ID` (PK)
- `created_at`
- `nombre`
- `estilo`
- `cantante`
- `imagen_url`

#### 😊 Emote

- `ID` (PK)
- `animado` (booleano)

### 🔗 Diagrama Entidad-Relación

> ![ERD](https://github.com/user-attachments/assets/6f67f621-fba4-401a-8fb2-3bbe385d7e3f)

---

## 🛠 Tecnologías Utilizadas

| Categoría           | Herramienta / Servicio |
|---------------------|------------------------|
| Base de datos       | [Supabase (PostgreSQL)](https://supabase.com/) |
| Backend/Servicios   | Supabase REST, [Jamendo](https://www.jamendo.com/start) (API de música), [7TV](https://7tv.app/emotes) (API de emotes) |
| Desarrollo móvil    | Kotlin Multiplataforma |
| IDE                 | Android Studio |

---

## 🔧 Detalles del Sistema

### 👤 Perfil

- El usuario puede registrarse, y editar su nombre, país y foto.
- Puede convertirse en "host" de una sala al seleccionar una canción.
- Otros usuarios pueden unirse a su sala para escuchar la música sincronizada.

### 🎵 Canción

- Seleccionada por el host.
- Incluye atributos como nombre, estilo, cantante e imagen.

### 😄 Emote

- Emotes animados: usados en el chat.
- Emotes estáticos: pueden usarse como avatar del perfil.

---

## 📆 Cronograma

| Fecha        | Entregable                                    |
|--------------|-----------------------------------------------|
| 26/03/2025   | Primer boceto visual de la app                |
| 09/04/2025   | Mejoras de interfaz                           |
| 30/04/2025   | Integración de base de datos con la app       |
| 14/05/2025   | Corrección de errores y finalización de UI    |

---

## 📌 Créditos / Recursos

- Música: [Jamendo API](https://www.jamendo.com/start)
- Emotes: [7TV API](https://7tv.app/emotes)
- Backend: [Supabase](https://supabase.com/)
- Diseño UI: [Figma](https://www.figma.com/)

---

Desarrollado por: Álvaro Eugenio García
