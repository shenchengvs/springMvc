<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="base/top.jsp"%>
<html>
<head>
<%@include file="base/js.jsp"%>
</head>
<body>
<p id="messages"></p>
<button id='sendBtn'>send</button>
</body>
<script type="text/javascript">
/* var webSocket;
var isOpen=false; */
$("#sendBtn").on("click",function(){
		$("#messages").html("");
		MY_WEBSOCKET.bindWebSocketEvent(onOpen, onMessage, onClose, onError,true);
});

function onMessage(event) {
	writeContent("websocket返回信息："+event.data);
}

function writeContent(content){
	$("#messages").append(content+"</br>");
}

function onOpen() {  
	writeContent("websocket链接打开");
	var sendMsg = {};
	sendMsg.type=1;
	writeContent("client发送信息："+MY_JSON.getJsonStr(sendMsg));
	MY_WEBSOCKET.send(sendMsg);
}

function onClose(){
	writeContent("websocket链接关闭");
}

function onError() {  
  	writeContent("websocket链接错误"); 
} 

</script>
</html>
