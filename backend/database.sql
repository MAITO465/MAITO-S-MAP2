CREATE DATABASE IF NOT EXISTS maito_tracking_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE maito_tracking_db;

CREATE TABLE location_history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  lat DOUBLE NOT NULL,
  lon DOUBLE NOT NULL,
  recorded_time DATETIME NOT NULL,
  device_identifier VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
