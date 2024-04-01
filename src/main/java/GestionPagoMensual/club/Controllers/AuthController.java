package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Entitys.Usuario;
import GestionPagoMensual.club.Repositories.UsuarioRepository;
import GestionPagoMensual.club.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/adm_clubes/usuario")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        return authService.login(credentials);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registro(@RequestBody Usuario newUser) {
        return authService.registro(newUser);
    }
}
