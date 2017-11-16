location.php
<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email']) && isset($_POST['steamid']) && isset($_POST['lat']) && isset($_POST['lon']) {
 
    // receiving the post params
    $email = $_POST['email'];
    $steamid = $_POST['steamid'];
    $lat = $_POST['lat'];
	$lon = $_POST['lon'];
    // check if user location is already existed with the same email
    if ($db->isLocationExisted($email)) {
        // user already existed
        $user = $db->alterUserLocation($email, $steamid, $lat, $lon);
		$nearby = $db->FindNearBy($email,$lat,$lon);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["email"] = $user["name"];
            $response["user"]["steamid"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
			$response["nearby"]=$nearby;
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in location registration!";
            echo json_encode($response);
        }
    } else {
        // store new user location
        $user = $db->storeUserLocation($email, $steamid, $lat, $lon);
		$nearby = $db->FindNearBy($lat,$lon);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["email"] = $user["name"];
            $response["user"]["steamid"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
			$response["nearby"]=$nearby;
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in location registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (email, steamid or location parameters) is missing!";
    echo json_encode($response);
}
?>
