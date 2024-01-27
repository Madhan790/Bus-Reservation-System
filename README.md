# Bus Reservation System

A simple command-line Bus Reservation System built using Java, JDBC, and MySQL.

## Table of Contents

- Features
- Prerequisites
- Setup
  - Database Configuration
  - Run the Application
- Database Schema
- Usage
  - View Buses
  - Make a Reservation
  - Register
  - Exit
- Contributors

## Features

- **View Buses:** Display details of available buses, including AC status, capacity, driver name, and seat availability.

- **Make a Reservation:** Reserve a seat on a selected bus, ensuring availability and updating the booking records.

- **Register:** Create a new user account to make reservations.

## Prerequisites

- Java Development Kit (JDK)
- MySQL Database
- MySQL Connector/J JDBC Driver

## Setup

### Database Configuration

1. Create a MySQL database named `busreservation`.
2. Update database connection details in `DbConnection.java`.

### Run the Application

1. Compile and run `BusRerservation.java`.
2. Follow the command-line interface to interact with the system.

## Database Schema

- **Tables:**
  - `bus`: Stores information about buses.
  - `user`: Manages user accounts.
  - `booking`: Records reservations.

## Usage

### 1. View Buses

Choose option 1 to display available buses.

### 2. Make a Reservation

Choose option 2, enter your username, and select a bus to reserve.

### 3. Register

Choose option 3 to create a new user account.

### 4. Exit

Choose option 4 to exit the application.

## Contributors

- Madhan M


