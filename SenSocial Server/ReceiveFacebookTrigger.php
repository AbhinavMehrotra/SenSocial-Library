<?php  

define('VERIFY_TOKEN', 'sensocialforfacebook');  

$method = $_SERVER['REQUEST_METHOD'];   
                                                    
   
if ($method == 'GET' && $_GET['hub_mode'] == 'subscribe' &&  $_GET['hub_verify_token'] == VERIFY_TOKEN) {
 	echo $_GET['hub_challenge'];
    	exit;  
}  
else if ($method == 'POST') {
        $data = file_get_contents("php://input");
	$json = json_decode($data); 	

	/*Format of data received from facebook-		
	{"object":"user","entry"[{"uid":"100000609105612","id":"100000609105612","time":1363643824,"changed_fields":["statuses"]}]}';
	*/
	$message = '{"name":"facebook", "facebook":'.$json.'}';
	$service_port = '4444';
	$address = gethostbyname('localhost');
	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	$result = socket_connect($socket, $address, $service_port);
	socket_write($socket, $message, strlen($message));
	socket_close($socket);	
}

?>
