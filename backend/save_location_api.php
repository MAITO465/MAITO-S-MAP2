<?php
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Only POST is allowed"]);
    exit;
}

require_once __DIR__ . '/services/LocationTrackingService.php';
require_once __DIR__ . '/models/LocationModel.php';

$lat = $_POST['latitude'] ?? null;
$lon = $_POST['longitude'] ?? null;
$time = $_POST['date'] ?? null;
$deviceId = $_POST['imei'] ?? null;

if ($lat === null || $lon === null || $time === null || $deviceId === null) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Missing required parameters"]);
    exit;
}

try {
    $service = new LocationTrackingService();
    $location = new LocationModel(null, $lat, $lon, $time, $deviceId);
    $service->insertLocation($location);
    echo json_encode(["success" => true, "message" => "Location saved successfully"]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Server error: " . $e->getMessage()]);
}
?>
