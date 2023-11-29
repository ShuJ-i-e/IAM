package com.shujie;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IndexController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	private JavaMailSender mailSender;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	// Save operation
	@PostMapping("/users")
	public Mono<User> saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	// Read all operation
	@GetMapping("/users")
	public Flux<User> fetchUserList() {
		return userService.fetchUserList();
	}

	// Get user by id
	@GetMapping("{id}")
	public Mono<ResponseEntity<User>> getById(@PathVariable("id") final Long id) {
		Mono<User> user = userService.getById(id);
		return user.map(u -> ResponseEntity.ok(u)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Update operation
	@PutMapping("/users/{id}")
	public Mono<ResponseEntity<User>> updateUser(@RequestBody User user, @PathVariable("id") Long userId) {
		return userService.updateUser(user, userId).map(updatedUser -> ResponseEntity.ok(updatedUser))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	// Delete operation
	@DeleteMapping("/users/{id}")

	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id") Long userId) {
		return userService.deleteUserById(userId).map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Sign Up (Registration)
	@PostMapping("/signup")
	public Mono<ResponseEntity<Object>> signUp(@RequestBody User user) {
		return userService.signUp(user).map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

//    // Sign In (Authentication)
//    @PostMapping("/signin")
//    public Mono<ResponseEntity<String>> signIn(@RequestBody User user) {
//        // Assuming SignInRequest is a class that holds email/username and password
//        return userService.signIn(user.getEmail(), user.getPassword())
//                .map(token -> ResponseEntity.ok().body(token))
//                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
//    }
//    

//    @GetMapping("/forgot_password")
//    public String showForgotPasswordForm() {
// 
//    }

//    @PostMapping("/forgot_password")
//    public String processForgotPassword(ServerHttpResponse request, Model model) {
//    	String email = request.getParameter("email");
//        String token = RandomString.make(30);
//         
//        try {
//            userService.updateResetPasswordToken(token, email);
//            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
//            sendEmail(email, resetPasswordLink);
//            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
//             
//        } catch (RuntimeException ex) {
//            model.addAttribute("error", ex.getMessage());
//        } catch (UnsupportedEncodingException | MessagingException e) {
//            model.addAttribute("error", "Error while sending email");
//        }
//             
//        return "forgot_password_form";
//    }

	@PostMapping("/forgot_password")
	public String processForgotPassword(ServerHttpRequest request, @RequestBody String email) {
		String token = RandomString.make(30);
		userService.updateResetPasswordToken(token, email);
		String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;

		try {
			sendEmail(email, resetPasswordLink);
			logger.info("We have sent a reset password link to your email. Please check.");
//            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (RuntimeException ex) {
			logger.info("Error: " + ex.getMessage());
//            model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.info("Error" + e.getMessage());
//            model.addAttribute("error", "Error while sending email");
		}

		return resetPasswordLink;
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
//        helper.setFrom("shujwong@deloitte.com");
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
			logger.info("error"+ e);
//			e.printStackTrace(); // Handle the exception appropriately
		}
	}

//    @GetMapping("/reset_password")
//    public String showResetPasswordForm() {
// 
//    }

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		User user = userService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		} else {
			userService.updatePassword(user, password);

			model.addAttribute("message", "You have successfully changed your password.");
		}

		return "message";
	}

}