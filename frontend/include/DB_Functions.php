<?php
 //adapted from Ravi Tamada's tutorial
/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	public function storeSteamid($email,$steamid){
		$stmt = $this->conn->prepare("INSERT INTO steamID(email,steamid,created_at) VALUES(?, ?, NOW())");
        $stmt->bind_param("ss", $email,$steamid);
        $result = $stmt->execute();
        $stmt->close();
		// check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM steamID WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
	}
	public function storeUserLocation($email,$steamid, $lat, $lon){
		$stmt = $this->conn->prepare("INSERT INTO locations(email, steamid, lat, lon, created_at) VALUES(?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssss", $email, $steamid, $lat, $lon);
        $result = $stmt->execute();
        $stmt->close();
		// check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM locations WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
	}
	public function alterSteamid($email,$steamid){
		$stmt = $this->conn->prepare("UPDATE steamID SET steamid=? WHERE email=?;");
        $stmt->bind_param("ss", $steamid,$email);
        $result = $stmt->execute();
        $stmt->close();
		// check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM steamID WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
	}
	public function alterUserLocation($email, $steamid, $lat, $lon){
		$stmt = $this->conn->prepare("UPDATE locations SET lat=? , lon = ? WHERE email=?; ");
        $stmt->bind_param("sss", $lat, $lon, $email);
        $result = $stmt->execute();
        $stmt->close();
		// check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM locations WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
	}
	public function FindNearBy($email,$lat, $lon){
		
		$stmt = $this->conn->prepare("SELECT * FROM locations WHERE lat-?<=50 AND lon -?<=50 AND email!=?");
        $stmt->bind_param("ffs", $lat, $lon, $email);
		$result = $stmt->execute();
		$resultsql=$stmt->get_result();
		while ($row = $result->fetch_assoc()) {
			$statics[] = $row;
		}
        $stmt->close();
		return $statics;

	}
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
	// Check if user has preregistered location info
	public function isLocationExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from locations WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
	/**check if user already has steamid set**/
	public function isEmailLinked($email){
		$stmt = $this->conn->prepare("SELECT email from steamID WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // email already linked
            $stmt->close();
            return true;
        } else {
            // email didn't link
            $stmt->close();
            return false;
        }
	}
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>