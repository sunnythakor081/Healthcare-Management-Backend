package com.application.controller;

import java.util.List;

import com.application.model.Admin;
import com.application.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.AuthRequest;
import com.application.model.Doctor;
import com.application.model.User;
import com.application.service.DoctorRegistrationService;
import com.application.service.UserRegistrationService;
import com.application.util.JwtUtils;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController
{
	@Autowired
	private AdminService adminService;

	@Autowired
	private UserRegistrationService userRegisterService;

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DoctorRegistrationService doctorRegisterService;

	@GetMapping("/")
	public String welcomeMessage()
	{
		return "Welcome to HealthCare Management system !!!";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception
	{
		try
		{
			System.out.println(authRequest.getEmail());
			System.out.println(authRequest.getPassword());
			List<User> users = userRegisterService.getAllUsers();
			String currentEmail = "";
			for(User obj:users)
			{
				if(obj.getEmail().equalsIgnoreCase(authRequest.getEmail()))
				{
					currentEmail = obj.getUsername();
				}
			}
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentEmail, authRequest.getPassword()));
		}
		catch (Exception ex)
		{
			throw new Exception("Invalid Username/password");
		}
		return new ResponseEntity<String>(jwtUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
	}

	@PostMapping("/loginuser")
	public User loginUser(@RequestBody User user) throws Exception
	{
		String currEmail = user.getEmail();
		String currPassword = user.getPassword();

		User userObj = null;
		if(currEmail != null && currPassword != null)
		{
			userObj = userRegisterService.fetchUserByEmailAndPassword(currEmail, currPassword);
		}
		if(userObj == null)
		{
			throw new Exception("User does not exists!!! Please enter valid credentials...");
		}
		return userObj;
	}

	@PostMapping("/logindoctor")
	public Doctor loginDoctor(@RequestBody Doctor doctor) throws Exception
	{
		String currEmail = doctor.getEmail();
		String currPassword = doctor.getPassword();

		Doctor doctorObj = null;
		if(currEmail != null && currPassword != null)
		{
			doctorObj = doctorRegisterService.fetchDoctorByEmailAndPassword(currEmail, currPassword);
		}
		if(doctorObj == null)
		{
			throw new Exception("User does not exists!!! Please enter valid credentials...");
		}
		return doctorObj;
	}

	// UPDATED ADMIN LOGIN METHOD - Ye main change hai
	@PostMapping("/loginadmin")
	public String loginAdmin(@RequestBody AuthRequest authRequest) throws Exception
	{
		String inputEmail = authRequest.getEmail();
		String inputPassword = authRequest.getPassword();

		// Step 1: Email aur password se direct admin fetch kar (verify ke liye)
		Admin admin = adminService.fetchAdminByEmailAndPassword(inputEmail, inputPassword);
		if (admin == null) {
			throw new Exception("Invalid username/password");
		}

		// Step 2: Role check kar (optional, safe ke liye)
		if (!"ADMIN".equals(admin.getRole())) {
			throw new Exception("Invalid credentials or not an admin");
		}

		// Step 3: JWT generate kar email se (jaise pehle tha)
		return jwtUtil.generateToken(inputEmail);
	}
}