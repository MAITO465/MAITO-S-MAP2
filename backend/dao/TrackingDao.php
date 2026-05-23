<?php
interface TrackingDao {
    public function insertLocation($locationModel);
    public function retrieveAllLocations();
}
?>
