<?php
class DbConnection {
    private $pdo;

    public function __construct() {
        $host = 'localhost';
        $dbname = 'maito_tracking_db';
        $user = 'root';
        $pass = '';

        try {
            $this->pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pass, [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
            ]);
        } catch (PDOException $e) {
            die("Database connection error: " . $e->getMessage());
        }
    }

    public function getPdo() {
        return $this->pdo;
    }
}
?>
