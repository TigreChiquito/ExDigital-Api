# ExdigitalApi

## Configuración de Variables de Entorno

Este proyecto utiliza variables de entorno para proteger datos sensibles como credenciales de base de datos y secretos JWT.

### Pasos para configurar:

1. **Copia el archivo de ejemplo:**
   ```bash
   cp .env.example .env
   ```

2. **Edita el archivo `.env`** con tus credenciales reales:
   ```properties
   DB_URL=jdbc:postgresql://db.tu-proyecto.supabase.co:5432/postgres
   DB_USERNAME=postgres
   DB_PASSWORD=tu-password-real-de-supabase
   JWT_SECRET=tu-clave-secreta-jwt-muy-segura
   JWT_EXPIRATION=86400000
   CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
   SERVER_PORT=8080
   ```

3. **Nunca subas el archivo `.env` al repositorio** - ya está incluido en `.gitignore`

### Ejecutar la aplicación:

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# Con Maven instalado
mvn spring-boot:run
```

### Notas importantes:

- El archivo `.env` contiene información sensible y NO debe ser versionado
- El archivo `.env.example` es una plantilla sin datos reales y SÍ debe estar en el repositorio
- Asegúrate de cambiar el `JWT_SECRET` por un valor aleatorio y seguro en producción
- Para producción, usa variables de entorno del sistema o servicios como Azure Key Vault, AWS Secrets Manager, etc.
