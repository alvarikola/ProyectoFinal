 # Proyecto-final

### Descripción de la aplicación
Es una aplicación realizada en kotlin donde los usuarios puedan crear salas para escuchar música junto a otros usuarios, la música esta sincronizada entre todos los usuarios de la sala. Estas salas tienen un chat que permite escribir con los otros usuarios e interactuar con emotes, además los usuarios tienen su propio perfil que pueden editar su nombre, país y foto de perfil.

### Figma con el prediseño de la aplicación:
![image](https://github.com/user-attachments/assets/d40abcd9-891d-42ff-b53a-708b208c571b)

### Nombre y logo de la aplicación
<div>
  <p>Sala404</p>
  <img src="https://github.com/user-attachments/assets/4d8be488-fa33-4366-97a0-9dc4381b81cd " width="100" />
</div>

### Requisitos funcionales
- Los usuarios pueden crear salas.
- En las salas se reproduce música.
- La música de cada sala es seleccionada por el creador de la sala.
- Cada usuario tiene su perfil personalizable.
- Las salas contienen un chat para que los usuarios interactuen.

### Entidades

#### Perfil
- **Atributos**:  
  - ID (PK)
  - Created_at  
  - Nombre  
  - Email  
  - Trackid
  - Fecha_inicio_cancion
  - Pais
  - Emoteid  

#### Cancion
- **Atributos**:  
  - ID (PK)
  - Created_at
  - Nombre  
  - Estilo  
  - Cantante
  - ImagenUrl

#### Emote
- **Atributos**:  
  - ID (PK)  
  - Animado
 
 ### Modelo Entidad Relación
 
![image](https://github.com/user-attachments/assets/6f67f621-fba4-401a-8fb2-3bbe385d7e3f)


## Tecnologías a utilizar

- **Base de datos**:  
  - Supabase (PostgreSQL)

- **Desarrollo multiplataforma**:  
  - Kotlin multiplataforma

- **Desarrollo de servicios**:  
  - Supabase: https://supabase.com/
  - Jamendo(música): https://www.jamendo.com/start
  - 7TV(emotes): https://7tv.app/emotes

- **Entornos de desarrollo**:  
  - Android Studio

### Descripción detallada del sistema

- **Perfil**:  
  El usuario puede crear una cuenta, personalizar su perfil con su foto, nombre y país. Además cuando selecciona una música para escuchar el usuario se transforma en una sala donde otros usuarios se pueden unir para escuchar los mismo. Por otro lado puede unirse a otros usuarios y escuchar lo que ellos pongan.
  
- **Canción**:  
  Las canciones son seleccionadas por los usuarios y están divididas por estilo, cada una tiene su nombre, cantante, foto y estilo de música.

- **Emote**:  
  Los emotes son escritos por los usuarios en el chat para interactuar, estos pueden ser animados o no. Los emotes animados se usan en el chat mientras que los estaticos se usan como foto de perfil para los usuarios.


## Cronograma
- **26/03/2025** - Realizar una acercación pequeña a la vista de la aplicación
- **09/04/2025** - Mejorar la interfaz 
- **30/04/2025** - Implementación de la base de datos en la aplicación
- **14/05/2025** - Corregir errores, terminar la interfaz

