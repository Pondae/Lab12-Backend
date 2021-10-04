package se331.lab.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.repository.OrganizerRepository;
import se331.lab.rest.security.entity.Authority;
import se331.lab.rest.security.entity.User;
import se331.lab.rest.security.repository.AuthorityRepository;
import se331.lab.rest.security.repository.UserRepository;
import se331.lab.rest.service.OrganizerService;
import se331.lab.rest.util.LabMapper;

@RestController
public class RegisterController {
    @Autowired
    OrganizerService organizerService;

    @Autowired
    OrganizerRepository organizerRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    ResponseEntity<?> getRegisters(@RequestBody User user){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        Organizer organizer = organizerRepository.save(Organizer.builder()
                .name(user.getUsername())
                .id(Long.parseLong(String.valueOf(organizerRepository.findAll().size()+1)))
                .build());
        user.setOrganizer(organizer);
        organizer.setUser(user);
        Authority authority = authorityRepository.getById(1L);
        user.getAuthorities().add(authority);
        User newuser = userRepository.save(user);
        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(newuser));
    }
}