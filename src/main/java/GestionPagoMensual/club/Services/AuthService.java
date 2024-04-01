package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Usuario;
import GestionPagoMensual.club.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository userRepository;

    public ResponseEntity<?> login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Validar que las credenciales no sean nulas o vacías
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario y la contraseña son obligatorios");
        }

        Usuario usuario = userRepository.findByUsername(username);

        // Verificar si el usuario existe y la contraseña es correcta (si se encuentra)
        if (usuario != null) {
            // Comparar contraseñas utilizando BCrypt
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                // La contraseña coincide, devuelve el usuario
                usuario.setAccess(true);
                return ResponseEntity.ok(usuario);
            }
        }

        // El usuario no existe o la contraseña es incorrecta
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
    }
    public ResponseEntity<?> registro(Usuario newUser) {
        // Verificar si el nombre de usuario ya está en uso
        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario ya está en uso");
        }

        // Validar la contraseña (por ejemplo, longitud mínima)
        if (newUser.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener al menos 6 caracteres");
        }

        // Realizar el hashing de la contraseña (aquí se puede usar cualquier algoritmo de hashing, como BCrypt)
        String hashedPassword = hashPassword(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        // Guardar el usuario en la base de datos
        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // Método para realizar el hashing de la contraseña (ejemplo con BCrypt)
    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}

