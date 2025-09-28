package com.application.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.Admin;
import com.application.model.Appointments;
import com.application.model.AuthRequest;
import com.application.model.Doctor;
import com.application.model.Prescription;
import com.application.model.Slots;
import com.application.model.User;
import com.application.service.AdminService;
import com.application.service.AppointmentBookingService;
import com.application.service.DoctorRegistrationService;
import com.application.service.PrescriptionService;
import com.application.service.UserRegistrationService;
import com.application.util.JwtUtils;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController 
{
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserRegistrationService userRegisterService;
    
    @Autowired
    private DoctorRegistrationService doctorRegisterService;
    
    @Autowired
    private AppointmentBookingService appointmentBookingService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private JwtUtils jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    // Admin login endpoint
//    @PostMapping("/loginadmin")
//    @CrossOrigin(origins = "http://localhost:4205")
//    public String loginAdmin(@RequestBody AuthRequest authRequest) throws Exception
//    {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
//            );
//        } catch (Exception ex) {
//            throw new Exception("Invalid username/password");
//        }
//        Admin admin = adminService.fetchAdminByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
//        if (admin != null) {
//            return jwtUtil.generateToken(authRequest.getEmail());
//        } else {
//            throw new Exception("Invalid credentials");
//        }
//    }
    
    // Register admin endpoint
//    @PostMapping("/registeradmin")
//    @CrossOrigin(origins = "http://localhost:4205")
//    public Admin registerAdmin(@RequestBody Admin admin) throws Exception
//    {
//        String tempEmail = admin.getEmail();
//        if (tempEmail != null && !tempEmail.isEmpty()) {
//            Admin adminObj = adminService.fetchAdminByEmail(tempEmail);
//            if (adminObj != null) {
//                throw new Exception("Admin with " + tempEmail + " already exists");
//            }
//        }
//        admin.setRole("ADMIN");
//        Admin adminObj = null;
//        adminObj = adminService.saveAdmin(admin);
//        return adminObj;
//    }
    
    // Get all users
    @GetMapping("/admin/userlist")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<User>> getAllUsers() throws Exception 
    {
        List<User> users = userRegisterService.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    @GetMapping("/getadmin")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<Admin>> findalladmin() throws Exception
    {
        System.out.println("contoller call");
        List<Admin> admins  = adminService.findalladmin();
        return new ResponseEntity<List<Admin>>(admins, HttpStatus.OK);
    }
    
    // Get all doctors
    @GetMapping("/admin/doctorlist")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<Doctor>> getAllDoctors() throws Exception 
    {
        List<Doctor> doctors = doctorRegisterService.getAllDoctors();
        return new ResponseEntity<List<Doctor>>(doctors, HttpStatus.OK);
    }
    
    // Accept doctor registration
    @PutMapping("/admin/acceptdoctor/{email}")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<String> acceptDoctor(@PathVariable String email) throws Exception 
    {
        doctorRegisterService.updateStatus(email);
        return new ResponseEntity<String>("Doctor accepted successfully", HttpStatus.OK);
    }
    
    // Reject doctor registration
    @PutMapping("/admin/rejectdoctor/{email}")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<String> rejectDoctor(@PathVariable String email) throws Exception 
    {
        doctorRegisterService.rejectStatus(email);
        return new ResponseEntity<String>("Doctor rejected successfully", HttpStatus.OK);
    }
    
    // Get all slots
    @GetMapping("/admin/slotlist")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<Slots>> getAllSlots() throws Exception 
    {
        List<Slots> slots = appointmentBookingService.getSlotList();
        return new ResponseEntity<List<Slots>>(slots, HttpStatus.OK);
    }
    
    // Get all appointments
    @GetMapping("/admin/appointmentlist")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<Appointments>> getAllAppointments() throws Exception 
    {
        List<Appointments> appointments = appointmentBookingService.getAllAppointments();
        return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
    }
    
    // Get all prescriptions
    @GetMapping("/admin/prescriptionlist")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<List<Prescription>> getAllPrescriptions() throws Exception 
    {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return new ResponseEntity<List<Prescription>>(prescriptions, HttpStatus.OK);
    }
    
    // Get dashboard statistics
    @GetMapping("/admin/dashboard")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<Map<String, Integer>> getDashboardStats() throws Exception 
    {
        Map<String, Integer> stats = new HashMap<>();
        
        // Get total users
        List<User> users = userRegisterService.getAllUsers();
        stats.put("totalUsers", users.size());
        
        // Get total doctors
        List<Doctor> doctors = doctorRegisterService.getAllDoctors();
        stats.put("totalDoctors", doctors.size());
        
        // Get total slots
        List<Slots> slots = appointmentBookingService.getSlotList();
        stats.put("totalSlots", slots.size());
        
        // Get total appointments
        List<Appointments> appointments = appointmentBookingService.getAllAppointments();
        stats.put("totalAppointments", appointments.size());
        
        // Get total prescriptions
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        stats.put("totalPrescriptions", prescriptions.size());

        int totalPatients = (int) appointments.stream().map(Appointments::getPatientname).distinct().count(); // Unique kar
        stats.put("totalPatients", totalPatients);
        return new ResponseEntity<Map<String, Integer>>(stats, HttpStatus.OK);
    }
    
    // Add a new doctor
    @PostMapping("/admin/adddoctor")
//    @CrossOrigin(origins = "http://localhost:4202")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) throws Exception 
    {
        Doctor doctorObj = doctorRegisterService.saveDoctor(doctor);
        return new ResponseEntity<Doctor>(doctorObj, HttpStatus.CREATED);
    }
}