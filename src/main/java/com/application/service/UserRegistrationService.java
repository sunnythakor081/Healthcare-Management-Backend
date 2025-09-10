package com.application.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.application.model.Admin;
import com.application.model.Doctor;
import com.application.model.User;
import com.application.repository.UserRegistrationRepository;

@Service
public class UserRegistrationService implements UserDetailsService
{
	@Autowired
	private UserRegistrationRepository userRegistrationRepo;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private DoctorRegistrationService doctorRegistrationService;
	
	public User saveUser(User user)
	{
		return userRegistrationRepo.save(user);
	}
	
	public User updateUserProfile(Long id,User user)
	{
      user.setId(id);
		return userRegistrationRepo.save(user);
	}
	
	public List<User> getAllUsers()
	{
		return (List<User>)userRegistrationRepo.findAll();
	}
	
	public User fetchUserByEmail(String email)
	{
		return userRegistrationRepo.findByEmail(email);
	}
	
	public User fetchUserByUsername(String username)
	{
		return userRegistrationRepo.findByUsername(username);
	}
	
	public User fetchUserByEmailAndPassword(String email, String password)
	{
		return userRegistrationRepo.findByEmailAndPassword(email, password);
	}
	
	public List<User> fetchProfileByEmail(String email)
	{
		return (List<User>)userRegistrationRepo.findProfileByEmail(email);
	}
	
	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException
	{
		User user = userRegistrationRepo.findByEmail(email);
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRegistrationRepo.findByEmail(username);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
		} else {
			// Try to find admin with the same email
			try {
				Admin admin = adminService.fetchAdminByEmail(username);
				if (admin != null) {
					return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), new ArrayList<>());
				}
			} catch (Exception e) {
				// Admin service not available or admin not found
			}
			
			// Try to find doctor with the same email
			try {
				Doctor doctor = doctorRegistrationService.fetchDoctorByEmail(username);
				if (doctor != null) {
					return new org.springframework.security.core.userdetails.User(doctor.getEmail(), doctor.getPassword(), new ArrayList<>());
				}
			} catch (Exception e) {
				// Doctor service not available or doctor not found
			}
			
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
	}
	
}