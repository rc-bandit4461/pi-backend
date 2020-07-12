package enset.bdcc.pi.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import enset.bdcc.pi.backend.dao.EtudiantRepository;
import enset.bdcc.pi.backend.dao.UserRepository;
import enset.bdcc.pi.backend.entities.Etudiant;
import enset.bdcc.pi.backend.other.AuthRequest;
import enset.bdcc.pi.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AuthenticationController {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/authenticate")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, Object> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        //Creating the ObjectMapper object
        HashMap<String, Object> map = new HashMap<>();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
            map.put("auth", true);
            map.put("token", jwtUtils.generateToken(authRequest.getUserName()));
        } catch (Exception e) {
            map.put("auth", false);
        }
        return map;
    }
}
