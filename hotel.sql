CREATE DATABASE hotel_booking;

USE hotel_booking;

CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number INT UNIQUE,
    room_type VARCHAR(50),
    price DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);


CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100)
);
select*from customers;

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    status VARCHAR(20) DEFAULT 'BOOKED',
    total_amount DECIMAL(10,2),

    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
select*from bookings;
INSERT INTO rooms (room_number, room_type, price) VALUES
(101, 'Single', 1500),
(102, 'Double', 2500),
(103, 'Deluxe', 3500),
(104, 'Suite', 5000);
select*from rooms;
