<?php
class LocationModel {
    private $id;
    private $lat;
    private $lon;
    private $recordedTime;
    private $deviceIdentifier;

    public function __construct($id, $lat, $lon, $recordedTime, $deviceIdentifier) {
        $this->id = $id;
        $this->lat = $lat;
        $this->lon = $lon;
        $this->recordedTime = $recordedTime;
        $this->deviceIdentifier = $deviceIdentifier;
    }

    public function getId() { return $this->id; }
    public function getLat() { return $this->lat; }
    public function getLon() { return $this->lon; }
    public function getRecordedTime() { return $this->recordedTime; }
    public function getDeviceIdentifier() { return $this->deviceIdentifier; }
}
?>
