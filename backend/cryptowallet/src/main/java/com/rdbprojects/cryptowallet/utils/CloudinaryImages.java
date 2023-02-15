package com.rdbprojects.cryptowallet.utils;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class CloudinaryImages {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jwt.secret}")
    private String secret;

    @Value("${CLOUDINARY_NAME}")
    private String cloudinaryName;

    @Value("${CLOUDINARY_API_KEY}")
    private String cloudinaryApi;

    @Value("${CLOUDINARY_API_SECRET}")
    private String cloudinarySecret;

    public Cloudinary getCloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryName,
                "api_key", cloudinaryApi,
                "api_secret", cloudinarySecret,
                "secure", true));


    }

}
