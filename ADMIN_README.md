# Healthcare Management System - Admin Mode

## Overview
The Admin Mode in the Healthcare Management System provides administrative capabilities to manage doctors, patients, appointments, and other system resources.

## Features

### Authentication
- Admin login with secure JWT authentication
- Default admin credentials:
  - Email: admin@healthcare.com
  - Password: admin123

### Doctor Management
- View all doctors in the system
- Add new doctors to the system
- Accept or reject doctor registration requests

### User/Patient Management
- View all users/patients in the system
- Access patient details and history

### Appointment Management
- View all appointments in the system
- Check available doctor slots

### Prescription Management
- View all prescriptions in the system

### Dashboard Statistics
- Total Users/Patients count
- Total Doctors count
- Total Slots available
- Total Appointments booked
- Total Prescriptions given

## API Endpoints

### Authentication
- POST `/loginadmin` - Admin login
- POST `/registeradmin` - Register new admin (restricted use)

### User Management
- GET `/admin/userlist` - Get all users

### Doctor Management
- GET `/admin/doctorlist` - Get all doctors
- PUT `/admin/acceptdoctor/{email}` - Accept doctor registration
- PUT `/admin/rejectdoctor/{email}` - Reject doctor registration
- POST `/admin/adddoctor` - Add a new doctor

### Slot Management
- GET `/admin/slotlist` - Get all slots

### Appointment Management
- GET `/admin/appointmentlist` - Get all appointments

### Prescription Management
- GET `/admin/prescriptionlist` - Get all prescriptions

### Dashboard
- GET `/admin/dashboard` - Get dashboard statistics

## Security
All admin endpoints are secured and require proper authentication with valid JWT token.