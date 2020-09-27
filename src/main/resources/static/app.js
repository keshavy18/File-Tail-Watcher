var stompClient = null;


function connect(){
	
	var socket = new SockJS('/Spring-Web-Socket');
	
	stompClient = Stomp.over(socket);
	
	var callback  = function(frame){
        console.log('Hurray! ' + frame);
		stompClient.subscribe('/topic/client',function(lines){
			document.getElementById("setLines").innerHTML = lines;
		});
	}
	
	stompClient.connect({},callback);
	
}


function send(){
	stompClient.send("/app/fileName",{},"Connection Check!");
}