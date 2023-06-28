package rs.ac.bg.fon.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.ac.bg.fon.dto.UserRoleDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.service.UserService;
import rs.ac.bg.fon.utility.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static rs.ac.bg.fon.constants.SecretKeys.getAlgorithm;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable String username) {

        try {
            User user = userService.deleteUser(username);
            return ResponseEntity.ok().body(new ApiResponse<>(user, "Successfully deleted user!\nUsername: "+username, ""));
        } catch (Exception e) {
            System.out.println("Error in: deleteUser() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error while deleting user."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user) {

        try {
            User newUser = userService.registerUser(user);
            if(newUser==null){
                throw new Exception("Email or username is already in use!");
            }
            return ResponseEntity.ok().body(new ApiResponse<>(newUser, "Successfully registered!", ""));
        } catch (Exception e) {
            System.out.println("Error in: registerUser() \n" + e.getMessage());
            String errorMessage = e.getMessage().contains("Email or username is already in use!") ? e.getMessage() : "Error while registering new user.";
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", errorMessage));
        }

    }

    @GetMapping("/get/{username}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String username) {
        try {
            User user = userService.getUser(username);
            return ResponseEntity.ok().body(new ApiResponse<>(user, "", ""));
        } catch (Exception e) {
            System.out.println("Error in: getUser() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error while getting user."));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getUsers() {
        try {
            List<User> userList = userService.getUsers();
            return ResponseEntity.ok().body(new ApiResponse<>(userList, "", ""));
        } catch (Exception e) {
            System.out.println("Error in: getUsers() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error while getting all users."));
        }
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<ApiResponse<?>> addRoleToUser(@RequestBody UserRoleDTO userRole) {
        try {
            userService.addRoleToUser(userRole.getUsername(), userRole.getRoleName());
            return ResponseEntity.ok().body(new ApiResponse<>(null, "Successfully added role [ "+userRole.getRoleName()+" ] to user [ "+userRole.getUsername()+" ]", ""));
        } catch (Exception e) {
            System.out.println("Error in: addRoleToUser() \n" + e.getMessage());
            return ResponseEntity.internalServerError().body(new ApiResponse<>(null, "", "Error adding role [ "+userRole.getRoleName()+" ] to user [ "+userRole.getUsername()+" ]"));
        }
    }

//    @GetMapping("/token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//
//                // TODO -> UTILITY KLASA U KOJOJ IMAMO ALGORITAM
//                Algorithm algorithm = getAlgorithm();
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//
//                String username = decodedJWT.getSubject();
//                User user = userService.getUser(username);
//
//                String access_token = JWT.create()
//                        .withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +  1000))
//                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
//                        .sign(algorithm);
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception e) {
//                response.setHeader("error", e.getMessage());
//                response.setStatus(FORBIDDEN.value());
//
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", e.getMessage());
//
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        } else {
//            throw new RuntimeException("Refresh token is missing");
//        }
//    }

}
