package com.automa.services.implementation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.config.GoogleConfig;
import com.automa.dto.MessageResponse;
import com.automa.dto.auth.ForgotPasswordRequest;
import com.automa.dto.auth.ResetPasswordRequest;
import com.automa.dto.auth.SignInRequest;
import com.automa.dto.auth.SignInResponse;
import com.automa.dto.auth.SignUpRequest;
import com.automa.entity.ApplicationUser;
import com.automa.entity.Role;
import com.automa.repository.ApplicationUserRepository;
import com.automa.services.interfaces.IAuth;

@Service
@Validated
public class AuthService implements IAuth {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            ApplicationUserRepository applicationUserRepository,
            GoogleConfig googleConfig,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Value("${user.profile_image_uri}")
    private String profileImageUrl;

    @Override
    public ResponseEntity<MessageResponse> signUp(SignUpRequest request) {
        if (request.getPassword().equals(request.getConfirmPassword())) {
            if (!applicationUserRepository.findByUsername(request.getEmail()).isPresent()) {
                if (!applicationUserRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
                    ApplicationUser user = new ApplicationUser();
                    BeanUtils.copyProperties(request, user, "password");
                    user.setUsername(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setProfileImageUrl(profileImageUrl);
                    user.setRole(Role.USER);
                    applicationUserRepository.save(user);
                    return new ResponseEntity<>(new MessageResponse("User has been Signup Successfully!!!"), HttpStatus.OK);
                }
                return new ResponseEntity<>(new MessageResponse("Phone Number Already Exists!!!"),
                        HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new MessageResponse("Email Already Exists!!!"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new MessageResponse("Passwords and Confirm Password do not match!!!"),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid Email or Password!!!");
        }
        ApplicationUser user = applicationUserRepository.findByUsername(request.getEmail()).orElseThrow(
                () -> new RuntimeException("User Not Found with this Email : " + request.getEmail()));
        String accessToken = jwtService.generateAccessToken(user);
        return new SignInResponse(accessToken);
    }

    @Override
    public ResponseEntity<MessageResponse> forgotPassword(ForgotPasswordRequest request) {
        ApplicationUser user = applicationUserRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found with this Email : " + request.getEmail()));

        // Nullify existing password reset token if present in ApplicationUser
        if (user.getPasswordResetToken() != null) {
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiresAt(null);
            applicationUserRepository.save(user);
        }

        // Generate new password reset token
        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusHours(1));

        // emailService.sendPasswordResetEmail(user.getEmail(), token);

        return new ResponseEntity<>(new MessageResponse("Password Reset Link has been Sent to your Email!!!"),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageResponse> resetPassword(ResetPasswordRequest request) {
        ApplicationUser user = applicationUserRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or Expired Password Reset Token!!!"));

        if (user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiresAt(null);
            throw new RuntimeException("Invalid or Expired Password Reset Token!!!");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new ResponseEntity<>(new MessageResponse("Passwords and Confirm Password do not match!!!"),
                    HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        applicationUserRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("Password has been reset Successfully!!!"), HttpStatus.OK);
    }

}
