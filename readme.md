# Hotel Reservation System (HoRS)

## Project Overview

The **Hotel Reservation System (HoRS)** is an enterprise-grade software system designed to manage room inventory, pricing, and reservations for the upcoming Merlion Hotel in Singapore. This system also integrates with external platforms such as price comparison websites and travel agencies, enabling seamless booking experiences.

## Features

### HoRS Management Client

- **Employee Management**: Add, view, and manage employees and their roles.
- **Room Type Management**: Create, update, view, and manage room types and configurations.
- **Room Management**: Add, update, and allocate rooms while ensuring business rule compliance.
- **Pricing Strategies**: Implement various room rate tiers (Published, Normal, Peak, Promotion).
- **Reservation Handling**: Efficient room allocation through daily batch processing and exception reporting.

### HoRS Reservation Client

- **Guest Registration**: Register new guests for streamlined reservation handling.
- **Room Search & Reservation**: Search for available rooms and make reservations.
- **Reservation Management**: View and manage personal reservations.

### HoRS Client

- **Partner Reservation**: Room search and booking capabilities for partners on behalf of customers.

## System Architecture

The system is built using **Jakarta EE** and consists of:

- **EJB and JPA**: For robust back-end management and persistence.
- **Relational Database (MySQL)**: To store and manage hotel data securely.
- **SOAP Web Services**: For external system interactions.

## Development Stack

- **Programming Language**: Java
- **Framework**: Jakarta Enterprise Edition (Jakarta EE)
- **Database**: MySQL
- **Development Tools**: NetBeans, GlassFish Server

## Contributors

- **Name**: Tan Jian Feng
- **Name**: Jovan Foo
