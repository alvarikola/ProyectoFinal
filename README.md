 # Proyecto-final
## AnteProyecto

### Descripción de la aplicación
Es una aplicación realizada en kotlin donde los usuarios puedan crear salas para escuchar música junto a otros usuarios. Estas salas pueden ser públicas o privadas, además los usuarios tienen su propio perfil donde tienen una lista con sus canciones más escuchadas.

### Figma con el prediseño de la aplicación:
![image](https://github.com/user-attachments/assets/d40abcd9-891d-42ff-b53a-708b208c571b)

### Requisitos funcionales
- Los usuarios pueden crear salas.
- Las salas pueden ser públicas o privadas.
- En las salas se reproduce música.
- La música de cada sala es seleccionada por el creador de la sala, pero los otros usuarios pueden sugerir canciones.
- Cada usuario tiene su perfil con el historial de canciones más escuchadas.

### Entidades y Relaciones

#### Usuario
- **Atributos**:  
  - ID (PK)  
  - Nombre  
  - FotoPerfil  
  - Email  
  - Contraseña  

- **Relaciones**:  
  - Crea -> Sala (1:N)  
  - Se conecta -> Sala (N:M)  
  - Propone -> Canción (N:M)  
  - Tiene -> Historial de Canciones (1:N)

#### Sala
- **Atributos**:  
  - ID (PK)  
  - Nombre  
  - Tipo (Pública/Privada)  
  - UsuarioLiderID (FK)  

- **Relaciones**:  
  - Tiene -> Usuarios (N:M)  
  - Contiene -> Canción (N:M)  
  - Es creada por -> Usuario (N:1)

#### Canción
- **Atributos**:  
  - ID (PK)  
  - Título  
  - Artista  
  - Duración  

- **Relaciones**:  
  - Es propuesta por -> Usuario (N:M)  
  - Se encuentra en -> Sala (N:M)  
  - Aparece en -> Historial de Canciones (N:1)

#### Historial de Canciones
- **Atributos**:  
  - ID (PK)  
  - UsuarioID (FK)  
  - CanciónID (FK)  
  - VecesEscuchada
  
- **Relaciones**:  
  - Pertenece a -> Usuario (N:1)  
  - Contiene -> Canción (N:1)
 
 ![image](https://github.com/user-attachments/assets/e5992d01-e36f-4bde-8103-d92d2150bfd3)


## Tecnologías a utilizar

- **Base de datos**:  
  - Supabase (PostgreSQL)

- **Desarrollo multiplataforma**:  
  - Kotlin multiplataforma

- **Desarrollo de servicios**:  
  - Supabase y la API de SoundCloud

- **Entornos de desarrollo**:  
  - Android Studio

### Descripción detallada del sistema

- **Usuario**:  
  El usuario puede crear una cuenta, personalizar su perfil con su foto y nombre, y acceder a su historial de canciones. Además, puede crear salas, sugerir canciones, y unirse a salas ya creadas.
  
- **Sala**:  
  Una sala es creada por un usuario y puede ser pública o privada. Los usuarios se pueden unir a salas para escuchar música. Solo el creador tiene el control inicial de las canciones, pero otros usuarios pueden sugerir canciones.
  
- **Canción**:  
  Las canciones son propuestas por los usuarios y pueden formar parte de una o varias salas.

- **Historial de canciones**:  
  Cada usuario tiene un historial que muestra las canciones más escuchadas.


## Cronograma
- **26/03/2025** - Realizar una acercación pequeña a la vista de la aplicación
- **09/04/2025** - Mejorar la interfaz 
- **30/04/2025** - Implementación de la base de datos en la aplicación
- **14/05/2025** - Corregir errores, terminar la interfaz y muestra de datos de la base de datos

