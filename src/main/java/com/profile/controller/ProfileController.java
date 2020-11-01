package com.profile.controller;

import com.profile.model.User;
import com.profile.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Controller
public class ProfileController {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${accessKey}")
    private String accesskey;

    @Value("${secretKey}")
    private String secretkey;

    @Value("${bucketName}")
    private String bucketName;

    @Autowired
    @Qualifier(value="userRepository")
    private UserRepository userRepository;

    @GetMapping("/home")
    public ModelAndView loginPage() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("homeUrl", baseUrl + "/home");
        return mv;
    }

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        ModelAndView mv = new ModelAndView("signup");
        mv.addObject("homeUrl", baseUrl + "/home");
        return mv;
    }

    @PostMapping(path="/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password) {

        User user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            ModelAndView mv = new ModelAndView("redirect");
            mv.addObject("message", "User or password incorrect, please login again.");
            mv.addObject("url", baseUrl + "/home");
            return mv;
        }

        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("user", user);
        mv.addObject("homeUrl", baseUrl + "/home");
        return mv;
    }

    @PostMapping(path="/signup")
    public ModelAndView signup(@RequestParam String username, @RequestParam String password) {

        ModelAndView mv = new ModelAndView("redirect");

        User user = userRepository.findByUsername(username);

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            mv.addObject("message", "Username or password cannot be blank, please go back to signup page.");
            mv.addObject("url", baseUrl + "/signup");
        } else if (user != null) {
            mv.addObject("message", "User already exist, please go back to signup page.");
            mv.addObject("url", baseUrl + "/signup");
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userRepository.save(newUser);

            mv.addObject("message", "User saved, please go to login page.");
            mv.addObject("url", baseUrl + "/home");
        }

        return mv;
    }

    @PostMapping(value = "/edit")
    public ModelAndView edit(@RequestParam MultipartFile picture,
                             @RequestParam String username,
                             @RequestParam String firstname,
                             @RequestParam String lastname,
                             @RequestParam String biography) {

        boolean shouldUpdate = false;
        User user = userRepository.findByUsername(username);

        if (picture.getOriginalFilename() != null && !picture.getOriginalFilename().isBlank()) {
            BasicAWSCredentials cred = new BasicAWSCredentials(accesskey, secretkey);
            AmazonS3 client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred))
                    .withRegion(Regions.US_EAST_2).build();
            try {
                PutObjectRequest put = new PutObjectRequest(bucketName, picture.getOriginalFilename(),
                        picture.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(put);

                String imgSrc = "http://" + bucketName + ".s3.amazonaws.com/" + picture.getOriginalFilename();
                user.setPicture(imgSrc);
                shouldUpdate = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (firstname != null && !firstname.equals(user.getFirstName())) {
            user.setFirstName(firstname);
            shouldUpdate = true;
        }
        if (lastname != null && !lastname.equals(user.getLastName())) {
            user.setLastName(lastname);
            shouldUpdate = true;
        }
        if (biography != null && !biography.equals(user.getBiography())) {
            user.setBiography(biography);
            shouldUpdate = true;
        }

        if (shouldUpdate) {
            userRepository.save(user);
        }

        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("user", user);
        mv.addObject("homeUrl", baseUrl + "/home");
        return mv;

    }


}