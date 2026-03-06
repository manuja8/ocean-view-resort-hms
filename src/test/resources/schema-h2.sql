DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS room_types;
DROP TABLE IF EXISTS guests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;

CREATE TABLE user_roles (
  role_id INT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  address VARCHAR(300) NOT NULL,
  contact_no VARCHAR(20) NOT NULL UNIQUE,
  role_id INT NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  is_blocked BOOLEAN NOT NULL DEFAULT FALSE,

  effective_date TIMESTAMP NULL,
  expiry_date TIMESTAMP NULL,
  last_login TIMESTAMP NULL,

  created_by_user_id INT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES user_roles(role_id)
);

CREATE TABLE guests (
  guest_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  gender VARCHAR(20) NULL,
  date_of_birth DATE NULL,
  address VARCHAR(300) NOT NULL,
  contact_no VARCHAR(20) NOT NULL,
  email VARCHAR(100) NULL,
  identification_no VARCHAR(30) UNIQUE,
  identification_type VARCHAR(50),
  created_by_user_id INT NOT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE room_types (
  room_type_id INT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(50) NOT NULL UNIQUE,
  price DECIMAL(10,2) NOT NULL
);

CREATE TABLE rooms (
  room_id INT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(10) NOT NULL UNIQUE,
  room_type_id INT NOT NULL,
  status VARCHAR(50) NOT NULL,

  created_by_user_id INT NOT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_room_type FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id)
);

CREATE TABLE reservations (
  reservation_id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_number VARCHAR(20) NOT NULL UNIQUE,
  guest_id INT NOT NULL,
  room_id INT NOT NULL,

  check_in_date TIMESTAMP NOT NULL,
  check_out_date TIMESTAMP NOT NULL,
  status VARCHAR(50) NOT NULL,

  created_by_user_id INT NOT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_res_guest FOREIGN KEY (guest_id) REFERENCES guests(guest_id),
  CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE bills (
  bill_id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_id INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  discount DECIMAL(10,2) NOT NULL DEFAULT 0,
  tax DECIMAL(10,2) NOT NULL DEFAULT 0,
  num_nights INT NOT NULL,
  bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_canceled BOOLEAN NOT NULL DEFAULT FALSE,

  created_by_user_id INT NOT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_bill_res FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

CREATE TABLE complaints (
  complaint_id INT AUTO_INCREMENT PRIMARY KEY,
  guest_id INT NOT NULL,
  reservation_id INT NULL,

  subject VARCHAR(150) NOT NULL,
  description CLOB NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'Open',
  priority VARCHAR(50) NOT NULL DEFAULT 'Normal',

  created_by_user_id INT NOT NULL,
  updated_by_user_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_comp_guest FOREIGN KEY (guest_id) REFERENCES guests(guest_id)
);