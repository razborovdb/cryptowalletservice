package com.rdbprojects.cryptowallet.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbprojects.cryptowallet.dao.CryptoCurrenciesDao;
import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.entities.CryptoCurrencies;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.utils.CloudinaryImages;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloudinary.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CryptoCurrenciesControler {
    @Autowired
    CryptoCurrenciesDao cryptoCurrenciesDao;

    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @Autowired
    CloudinaryImages cloudinaryImages;

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/cryptos")
    public ResponseEntity<List<CryptoCurrencies>> getCryptos(@RequestHeader(value="${json.token}") String token) {
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
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        List<CryptoCurrencies> cryptos = cryptoCurrenciesDao.findAll();
        return ResponseEntity.ok(cryptos);
    }

    @GetMapping("/crypto")
    public ResponseEntity<CryptoCurrencies> getCryptoByName(@RequestHeader(value="${json.token}") String token, @RequestParam String cryptoName) {

        // check request
        if (cryptoName == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        CryptoCurrencies findCrypto = new CryptoCurrencies();
        findCrypto.setCryptoName(cryptoName);
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

        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        // try to find crypto
        CryptoCurrencies findedCrypto = cryptoCurrenciesDao.findCryptoByName(findCrypto);
        if (findedCrypto == null) {
            return ResponseEntity.status(400).eTag("Crypto didn't find ...").body(null);
        }
        return ResponseEntity.ok(findedCrypto);
    }

    @PostMapping("/crypto")
    public ResponseEntity<CryptoCurrencies> addCryptos(@RequestHeader(value="${json.token}") String token, @RequestBody CryptoCurrencies cryptoDetails) {
        // check request
        if (cryptoDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (cryptoDetails.getCryptoName() == null) {
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
            if (!user.getIsAdmin() ) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        CryptoCurrencies findedCryptos = cryptoCurrenciesDao.findCryptoByName(cryptoDetails);
        if (findedCryptos == null) {

            if (cryptoDetails.getImageUrl() != null) {

                Cloudinary cloudinary = cloudinaryImages.getCloudinary();
                //

                try {
                    Map uploadResult = cloudinary.uploader().upload(cryptoDetails.getImageUrl(),  ObjectUtils.asMap(
                            "upload_preset", "cryptowallet"
                    ));


                    String json = new ObjectMapper().writeValueAsString(uploadResult);

                    String image = "";
                    if (uploadResult.containsKey("public_id")) {
                        image = uploadResult.get("public_id").toString();
                    }
                    cryptoDetails.setImage(image);

                    String imageUrl = "";
                    if (uploadResult.containsKey("url")) {
                        imageUrl = uploadResult.get("url").toString();
                    }

                    cryptoDetails.setImageUrl(imageUrl);

                } catch (Exception e) {
                    cryptoDetails.setImage("");
                    cryptoDetails.setImageUrl("");

                }

            }

            CryptoCurrencies addedCryptos = cryptoCurrenciesDao.addCrypto(cryptoDetails);
            if (addedCryptos != null) {
                return ResponseEntity.ok(addedCryptos);
            }

            return ResponseEntity.status(500).eTag("Internal server error ...").body(null);



        }
        return ResponseEntity.status(400).eTag("Crypto already exist ...").body(null);

    }

    @PutMapping("/crypto")
    public ResponseEntity<CryptoCurrencies> updateCryptos(@RequestHeader(value="${json.token}") String token, @RequestBody CryptoCurrencies cryptoDetails) {

        // check request
        if (cryptoDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (cryptoDetails.getCryptoName() == null) {
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
            if (!user.getIsAdmin() ) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //

        CryptoCurrencies findedCryptos = cryptoCurrenciesDao.findCryptoByName(cryptoDetails);
        if (findedCryptos != null) {
            findedCryptos.setCryptoDescription(cryptoDetails.getCryptoDescription());
            findedCryptos.setCryptoCost(cryptoDetails.getCryptoCost());

            //-----------------------------------------------
            if (cryptoDetails.getImageUrl() != null) {
                if (!cryptoDetails.getImageUrl().equals("")) {
                    Cloudinary cloudinary = cloudinaryImages.getCloudinary();
                    try {
                        Map destroyRsponse = cloudinary.uploader().destroy(cryptoDetails.getImage(), ObjectUtils.asMap());
                    } catch (Exception e) {

//                        cryptoDetails.setImage("");
//                        cryptoDetails.setImageUrl("");

                    }
                    try {

                        Map uploadResult = cloudinary.uploader().upload(cryptoDetails.getImageUrl(),
                                ObjectUtils.asMap(
                                "upload_preset", "cryptowallet"
                        ));


                        String json = new ObjectMapper().writeValueAsString(uploadResult);


                        String image = "";
                        if (uploadResult.containsKey("public_id")) {
                            image = uploadResult.get("public_id").toString();
                        }
                        cryptoDetails.setImage(image);
                        
                        String imageUrl = "";
                        if (uploadResult.containsKey("url")) {
                            imageUrl = uploadResult.get("url").toString();
                        }

                        cryptoDetails.setImageUrl(imageUrl);

                    } catch (Exception e) {

//                        cryptoDetails.setImage("");
//                        cryptoDetails.setImageUrl("");

                    }
                    findedCryptos.setImage(cryptoDetails.getImage());
                    findedCryptos.setImageUrl(cryptoDetails.getImageUrl());

                }

            }


            //-------------------------------------------------
            CryptoCurrencies updatedCryptos = cryptoCurrenciesDao.updateCrypto(findedCryptos);
            if (updatedCryptos == null) {
                ResponseEntity.status(500).eTag("Internal server error ...").body(null);
            }
            return ResponseEntity.ok(updatedCryptos);
        }
        return ResponseEntity.status(400).eTag("Crypto didn't find ...").body(null);

    }

    @DeleteMapping("/crypto")
    public ResponseEntity<String> deleteCrypto(@RequestHeader(value="${json.token}") String token, @RequestBody CryptoCurrencies deleteCrypto) {

        // check request
        if (deleteCrypto == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        if (deleteCrypto.getCryptoName() == null) {
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
            if (!user.getIsAdmin() ) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body("Not authorized ...");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }

        CryptoCurrencies findedCryptos = cryptoCurrenciesDao.findCryptoByName(deleteCrypto);
        if (findedCryptos != null) {
            if (findedCryptos.getImageUrl() != null) {
                if (!findedCryptos.getImageUrl().equals("")) {
                    Cloudinary cloudinary = cloudinaryImages.getCloudinary();
                    try {
                        Map destroyRsponse = cloudinary.uploader().destroy(findedCryptos.getImage(), ObjectUtils.asMap());


                    } catch (Exception e) {

//                        cryptoDetails.setImage("");
//                        cryptoDetails.setImageUrl("");

                    }


                }

            }
            //
            CryptoCurrencies deletedCrypto = cryptoCurrenciesDao.deleteCrypto(deleteCrypto);
            if (deletedCrypto != null) {
                return ResponseEntity.ok(deleteCrypto.getCryptoName());
            }
        }

        return ResponseEntity.status(400).eTag("Crypto didn't find ...").body("Crypto didn't find ...");


    }
}
