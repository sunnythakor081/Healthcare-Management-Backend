package com.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.model.Admin;
import com.application.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService 
{
	@Autowired
	private AdminRepository adminRepository;
	
	public Admin saveAdmin(Admin admin)
	{
		return adminRepository.save(admin);
	}

	public List<Admin> findalladmin(){
		return (List<Admin>) adminRepository.findAll();
	}
	
	public Admin fetchAdminByEmail(String email)
	{
		return adminRepository.findByEmail(email);
	}

	
	public Admin fetchAdminByUsername(String username)
	{
		return adminRepository.findByUsername(username);
	}
	
	public Admin fetchAdminByEmailAndPassword(String email, String password)
	{
		return adminRepository.findByEmailAndPassword(email, password);
	}
}