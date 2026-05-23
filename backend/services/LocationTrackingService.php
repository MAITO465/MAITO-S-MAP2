<?php
require_once __DIR__ . '/../dao/TrackingDao.php';
require_once __DIR__ . '/../models/LocationModel.php';
require_once __DIR__ . '/../config/DbConnection.php';

class LocationTrackingService implements TrackingDao {
    private $db;

    public function __construct() {
        $conn = new DbConnection();
        $this->db = $conn->getPdo();
    }

    public function insertLocation($locationModel) {
        $query = "INSERT INTO location_history (lat, lon, recorded_time, device_identifier) VALUES (?, ?, ?, ?)";
        $stmt = $this->db->prepare($query);
        $stmt->execute([
            $locationModel->getLat(),
            $locationModel->getLon(),
            $locationModel->getRecordedTime(),
            $locationModel->getDeviceIdentifier()
        ]);
        return true;
    }

    public function retrieveAllLocations() {
        $query = "SELECT * FROM location_history";
        $stmt = $this->db->prepare($query);
        $stmt->execute();
        return $stmt->fetchAll();
    }
}
?>
