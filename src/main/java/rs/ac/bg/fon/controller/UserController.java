package rs.ac.bg.fon.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.ac.bg.fon.dtos.User.RoleToUserForm;
import rs.ac.bg.fon.dtos.User.UserDTO;
import rs.ac.bg.fon.dtos.User.UserRegistrationDTO;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.service.UserService;
import rs.ac.bg.fon.utility.ApiResponse;
import rs.ac.bg.fon.utility.ApiResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static rs.ac.bg.fon.constants.SecretKeys.getAlgorithm;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(userService.deleteUserApiResponse(username));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO user) {
        if (user == null) {
            return ApiResponseUtil.errorApiResponse("Invalid user data!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(userService.registerUserApiResponse(user));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ApiResponseUtil.errorApiResponse("User data is missing!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(userService.getUserApiResponse(username));
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getUsers(Pageable pageable) {
        ApiResponse<?> response = userService.getUsersApiResponse(pageable);
        return ApiResponseUtil.handleApiResponse(response);
    }

    @GetMapping("/users/filter")
    public ResponseEntity<?> getFilteredUsers(@RequestParam String filterUsername, Pageable pageable) {
        ApiResponse<?> response = userService.getFilteredUsersApiResponse(filterUsername, pageable);
        return ApiResponseUtil.handleApiResponse(response);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user) {
        if (user == null) {
            return ApiResponseUtil.errorApiResponse("Invalid user data!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(userService.updateUserApiResponse(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        if (role == null || role.getName() == null || role.getName().isBlank()) {
            return ApiResponseUtil.errorApiResponse("Invalid role data!\nContact support for more information!");
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ApiResponseUtil.handleApiResponse(userService.saveRoleApiResponse(role), uri);
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        if (form == null) {
            return ApiResponseUtil.errorApiResponse("Invalid data!\nContact support for more information!");
        }
        return ApiResponseUtil.handleApiResponse(userService.addRoleToUserApiResponse(form.getUsername(), form.getRoleName()));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = getAlgorithm();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
