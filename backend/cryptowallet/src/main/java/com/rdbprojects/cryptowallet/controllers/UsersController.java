package com.rdbprojects.cryptowallet.controllers;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.repositories.UsersRepository;
import com.rdbprojects.cryptowallet.utils.CloudinaryImages;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UsersController {
    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @Autowired
    CloudinaryImages cloudinaryImages;

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(@RequestHeader(value="${json.token}") String token) {
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!user.getIsAdmin()) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        List<Users> users = usersDao.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity<Users> getUserById(@RequestHeader(value="${json.token}") String token,
                                             @RequestParam(name = "email") String email) {
        // check request
        if (email == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(email))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        // try to find user
        Users findUser = new Users();
        findUser.setEmail(email);
        Users findedUser = usersDao.findUserByEmail(findUser);
        if (findedUser == null) {
            return ResponseEntity.status(400).eTag("User didn't find ...").body(null);
        }
        findedUser.setPassword("");
        return ResponseEntity.ok(findedUser);
    }

    @PutMapping("/user")
    public ResponseEntity<Users> updateUsers(@RequestHeader(value="${json.token}") String token, @RequestBody Users userDetails) {
        // check request
        if (userDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (userDetails.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(userDetails.getEmail()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Users findedUser = usersDao.findUserByEmail(userDetails);
        if (findedUser != null) {

            //-----------------------------------------------
            if (userDetails.getAvatarUrl() != null) {
                if (!userDetails.getAvatarUrl().equals("")) {
                    Cloudinary cloudinary = cloudinaryImages.getCloudinary();
                    try {
                        Map destroyRsponse = cloudinary.uploader().destroy(userDetails.getAvatar(), ObjectUtils.asMap());
                    } catch (Exception e) {

                    }
                    try {

                        Map uploadResult = cloudinary.uploader().upload(userDetails.getAvatarUrl(),
                                ObjectUtils.asMap(
                                        "upload_preset", "cryptowallet"
                                ));


                        String json = new ObjectMapper().writeValueAsString(uploadResult);


                        String image = "";
                        if (uploadResult.containsKey("public_id")) {
                            image = uploadResult.get("public_id").toString();
                        }
                        userDetails.setAvatar(image);

                        String imageUrl = "";
                        if (uploadResult.containsKey("url")) {
                            imageUrl = uploadResult.get("url").toString();
                        }

                        userDetails.setAvatarUrl(imageUrl);

                    } catch (Exception e) {

                    }
                    findedUser.setAvatar(userDetails.getAvatar());
                    findedUser.setAvatarUrl(userDetails.getAvatarUrl());

                }

            }


            //-------------------------------------------------



            findedUser.setName(userDetails.getName());

            if (user.getIsAdmin()) {
                findedUser.setRole(userDetails.getRole());
                findedUser.setIsAdmin(userDetails.getIsAdmin());
            }
            Users updatedUser = usersDao.updateUser(findedUser);
            updatedUser.setPassword("");
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(400).eTag("User didn't find ...").body(null);

    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestHeader(value="${json.token}") String token, @RequestBody Users userDetails) {

        // check request
        if (userDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        if (userDetails.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(userDetails.getEmail()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body("Not authorized ...");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        //
        Users deletedUser = usersDao.deleteUser(userDetails);
        if (deletedUser != null) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(400).eTag("User didn't find ...").body("User didn't find ...");


    }

}
