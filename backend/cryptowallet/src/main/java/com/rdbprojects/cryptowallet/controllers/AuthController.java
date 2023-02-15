package com.rdbprojects.cryptowallet.controllers;

import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.repositories.UsersRepository;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @PostMapping("/user/register")
    public ResponseEntity<String> userRegister(@RequestBody Users createUser) {
        // check request
        if (createUser == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (createUser.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // Check user with email
        Users user = usersDao.findUserByEmail(createUser);
        if (user != null) {
            return ResponseEntity.status(400).body("User already exist ...");
        }

        // Encode password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodedPassword = encoder.encode(createUser.getPassword());

        Users newUser = new Users(createUser.getEmail(), createUser.getName(),  createUser.getAvatar(),
                createUser.getAvatarUrl(), encodedPassword, createUser.getRole(), createUser.getIsAdmin());

        // Save newUser to database
        Users createdUser = usersDao.addUser(newUser);
        createdUser.setPassword("");

        // create web token string
        Optional<String> jwts = jsonWebToken.genJsonWebToken(createdUser);

        if (jwts.isEmpty()) {
            return ResponseEntity.status(500).body("Server error ...");
        }

        return ResponseEntity.status(200).body(jwts.get());
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> userLogin(@RequestBody Users loginUser) {

        // check request
        if (loginUser == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (loginUser.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }

        // Check user with email
        Users user = usersDao.findUserByEmail(loginUser);
        if (user == null) {
            return ResponseEntity.status(400).body("Bad user name or password ...");
        }



        // Encode password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        if(!encoder.matches(loginUser.getPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body("Bad user  or password ...");
        }

        user.setPassword("");

        // create web token string
        Optional<String> jwts = jsonWebToken.genJsonWebToken(user);

        if (jwts.isEmpty()) {
            return ResponseEntity.status(500).body("Server error ...");
        }

        return ResponseEntity.status(200).body(jwts.get());
    }
}
