<?php
header('Content-Type: application/json; charset=utf-8');

require_once __DIR__ . '/services/LocationTrackingService.php';

if ($_SERVER["REQUEST_METHOD"] === "POST" || $_SERVER["REQUEST_METHOD"] === "GET") {
    $service = new LocationTrackingService();
    $locations = $service->retrieveAllLocations();
    
    // Renvoyer les données au format JSON attendu par l'application
    echo json_encode(["positions" => $locations]);
}
?>
