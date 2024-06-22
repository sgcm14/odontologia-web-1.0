package clinica.sistemaReservaTurno.security;
import clinica.sistemaReservaTurno.entity.Usuario;
import clinica.sistemaReservaTurno.entity.UsuarioRole;
import clinica.sistemaReservaTurno.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements ApplicationRunner {
@Autowired
private UsuarioRepository usuarioRepository;
@Autowired
private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String passSinCifrar= "admin";
        String passCifrado= passwordEncoder.encode(passSinCifrar);

        String passSinCifrar2= "user";
        String passCifrado2= passwordEncoder.encode(passSinCifrar2);
        Usuario usuario= new Usuario("jorgito", UsuarioRole.ROLE_ADMIN,passCifrado,"admin@admin.com","jpereyradh");
        Usuario usuario2= new Usuario("usersito", UsuarioRole.ROLE_USER,passCifrado2,"user@user.com","userdh");
        System.out.println("pass cifrado: "+passCifrado);
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuario2);

    }
}
