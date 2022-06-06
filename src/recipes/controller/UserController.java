package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.common.User;
import recipes.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/register", produces="application/json")
public class UserController {
    private  final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserController(@Autowired UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @PostMapping()
    public void register(@Valid @RequestBody User user){
        var res=userRepository.findById(user.getEmail());
        if(!res.isEmpty())
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }


    }
}
