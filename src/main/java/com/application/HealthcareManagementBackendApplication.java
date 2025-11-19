package com.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.model.Admin;
import com.application.service.AdminService;

@SpringBootApplication
public class HealthcareManagementBackendApplication implements CommandLineRunner {

	@Autowired
	private AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(HealthcareManagementBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Initialize default admin user if not exists
		Admin existingAdmin = adminService.fetchAdminByEmail("admin@gmail.com");
		if (existingAdmin == null) {
			Admin admin = new Admin();
			admin.setEmail("admin@gmail.com");
			admin.setUsername("admin");
			admin.setPassword("admin123");
			admin.setRole("ADMIN");
			adminService.saveAdmin(admin);
			System.out.println("Default admin user created");
		}
	}
}
