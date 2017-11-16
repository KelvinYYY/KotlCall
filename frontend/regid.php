regid.php
<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

//json response array

$response = array("error" => FALSE);

if(isset($_POST['email']) && isset($_POST['steamid'])){
	$email = $_POST['email'];
	$steamid = $_POST['steamid'];
	
	//check if the user is already existed with the same email
	if ($db->isEmailLinked($email)) {
        // email already linked to an email
		
        $user = $db->alterSteamid($email, $steamid);
        if ($user) {
            // steamid update successfully
            $response["error"] = FALSE;
            $response["user"]["email"] = $user["email"];
			$response["user"]["steamid"] = $user["steamid"];
            $response["user"]["created_at"] = $user["created_at"];
			$response["error_msg"] = "success created";
            echo json_encode($response);
        } else {
            // teamid link failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in steamid link!";
            echo json_encode($response);
        }
    } else {
		//email didn't linked to any email
		$user = $db->storeSteamid($email, $steamid);
        if ($user) {
            // steamid stored successfully
            $response["error"] = FALSE;
            $response["user"]["email"] = $user["email"];
			$response["user"]["steamid"] = $user["steamid"];
            $response["user"]["created_at"] = $user["created_at"];
			$response["error_msg"] = "success created";
            echo json_encode($response);
        } else {
            // teamid link failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in steamid link!";
            echo json_encode($response);
        }
	}
}




?>