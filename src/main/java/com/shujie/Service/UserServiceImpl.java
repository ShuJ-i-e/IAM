package com.shujie.Service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shujie.TokenExpiredException;
import com.shujie.Controller.IndexController;
import com.shujie.Repository.RoleRepository;
import com.shujie.Repository.UserRepository;

import Entity.Roles;
import Entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JavaMailSender mailSender;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Override
	public Mono<User> saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Mono<User> updateUser(User user, Long userId) {
		return userRepository.findById(userId).flatMap(dbUser -> {
			dbUser.setFirstName(user.getFirstName());
			dbUser.setLastname(user.getLastname());
			dbUser.setEmail(user.getEmail());
			dbUser.setAbout(user.getAbout());
			dbUser.setRoles(user.getRoles());
			dbUser.setLanguages(user.getLanguages());
			dbUser.setSkills(user.getSkills());
			dbUser.setProject_experiences(user.getProject_experiences());
			dbUser.setAssignments(user.getAssignments());
			dbUser.setProfilePic(user.getProfilePic());
			return userRepository.save(dbUser);
		});
	}

	@Override
	public Mono<User> deleteUserById(Long userId) {
		return userRepository.findById(userId)
				.flatMap(existingUser -> userRepository.delete(existingUser).then(Mono.just(existingUser)));
	}

	@Override
	public Mono<User> getById(Long userId) {

		return userRepository.findById(userId);
	}

	@Override
	public Flux<User> fetchUserList() {
		return userRepository.findAll();
	}

	public void updateResetPasswordToken(String token, String email) throws RuntimeException {
		Mono<User> user = userRepository.findByEmail(email);
		if (user != null) {
			user.flatMap(dbUser -> {
				dbUser.setResetPasswordToken(dbUser.getResetPasswordToken());
				return userRepository.save(dbUser);
			});

		} else {
			throw new RuntimeException("Could not find any customer with the email " + email);
		}
	}

	public void updatePassword(User user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		user.setResetPasswordToken(null);
		userRepository.save(user);
	}

	@Override
	public Mono<Object> registerUser(String username, String email, String password, String role) {
	    User user = new User();
	    user.setUsername(username);
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    user.setPassword(passwordEncoder.encode(password));
	    user.setEmail(email);
	    user.setVerificationToken(UUID.randomUUID().toString());
	    user.setVerificationTokenCreationTime(LocalDateTime.now());

	    return userRepository.findByEmail(user.getEmail())
	            .flatMap(existingUser -> Mono.error(new RuntimeException("User with this email already exists")))
	            .switchIfEmpty(Mono.defer(() ->
	                    userRepository.save(user)
	                            .flatMap(savedUser -> {
	                                Roles roles = new Roles();
	                                roles.setUserId(savedUser.getId());
	                                roles.setRole(role);

	                                return roleRepository.save(roles)
	                                        .doOnSuccess(roleSaved -> sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken()))
	                                        .thenReturn(savedUser);
	                            })
	            )); 
	}

	@Override
	public Mono<User> verifyEmail(String verificationToken) {
		return userRepository.findByVerificationToken(verificationToken).flatMap(user -> {
			if (isTokenExpired(user.getVerificationTokenCreationTime())) {
				return Mono.error(new TokenExpiredException("Verification token has expired."));
			}

			user.setVerificationToken(null);
			user.setVerified(true);
			return userRepository.save(user);
		});
	}

	private void sendVerificationEmail(String to, String verificationToken) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String subject = "Email Verification";
		String link = "http://localhost:8000/verify_email/" + verificationToken;

		try {
			helper.setSubject(subject);
			helper.setText("<p>Thank you for registering. Please click on the link to verify your email: </p>"
					+ "<p><a href=\"" + link + "\">Activate Account</a></p>", true);
			helper.setTo(to);
			mailSender.send(message);
		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	private boolean isTokenExpired(LocalDateTime creationTime) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(creationTime, now);
		return duration.toMinutes() > 5; // Token expires after 5 minutes
	}

	public Mono<User> initiatePasswordReset(String email) {
		return userRepository.findByEmail(email).flatMap(user -> {
			String resetToken = UUID.randomUUID().toString();
			user.setResetPasswordToken(resetToken);

			return userRepository.save(user).doOnSuccess(savedUser -> {
				String resetLink = "http://localhost:8000/reset_password/" + resetToken;
				try {
					sendEmail(email, resetLink);
				} catch (UnsupportedEncodingException | MessagingException e) {

					e.printStackTrace();
				}
			}).thenReturn(user);
		}).switchIfEmpty(Mono.error(new RuntimeException("User not found")));
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(recipientEmail);
			String subject = "Here's the link to reset your password";

			String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
					+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
					+ "\">Change my password</a></p>" + "<br>"
					+ "<p>Ignore this email if you do remember your password, "
					+ "or you have not made the request.</p>";
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			logger.info("error" + e);
		}
	}

	public Mono<Void> resetPassword(String token, String newPassword) {
		return userRepository.findByResetPasswordToken(token).flatMap(user -> {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetPasswordToken(null);
			return userRepository.save(user);
		}).then();
	}

}
