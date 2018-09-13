var $ = layui.$;
/*layui.use('jquery', function(){ 
	   $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
});
*/
//websocket工具
var MY_WEBSOCKET={
		webSocket:null,
		webSocketUrl:basePath.replace("http://","ws://")+"webSocketServer",
		isConnecting:function(){
			return this.webSocket?(this.webSocket.readyState == 0?true:false):null;
		},
		isOPen:function(){
			return this.webSocket?(this.webSocket.readyState == 1?true:false):null;
		},
		isClosing:function(){
			return this.webSocket?(this.webSocket.readyState == 2?true:false):null;	
		},
		isClosed:function(){
			return this.webSocket?(this.webSocket.readyState == 3?true:false):null;
		},
		status:function(){
			return this.webSocket?this.webSocket.readyState:null;
		},
		initWebSocket:function(){
				if(this.isOPen()){
					this.stop();
				}
				if ('WebSocket' in window) {
			    	this.webSocket = new WebSocket(this.webSocketUrl);  
				} else if ('MozWebSocket' in window) {  
					this.webSocket = new MozWebSocket(this.webSocketUrl);  
				} else {
					this.webSocket = new SockJS(this.webSocketUrl);  
				} 
		},
		bindWebSocketEvent:function(onOpen,onMessage,onClose,onError,isAutoClose){
			this.initWebSocket();
			if(this.webSocket){
				this.webSocket.onopen = function(event) {
					if(onOpen){
						onOpen(event);  
					}
				};  
				
				this.webSocket.onmessage = function(event) {  
					if(onMessage){
						onMessage(event)  
					}
					if(isAutoClose){
						MY_WEBSOCKET.stop();
					}
				};  
				
				this.webSocket.onclose = function(){
					if(onClose){
						onClose();
					}
				}
				this.webSocket.onerror = function(event) { 
					if(onError){
						onError(event) 
					}
				};  
			}
		},
		stop: function(){
			if(this.webSocket){
				this.webSocket.close();	
			}
		},
		send:function(sendMsg){
			if(this.webSocket){
				var tempMsg ="";
				if(sendMsg instanceof Object){
					tempMsg = MY_JSON.getJsonStr(sendMsg);
				}else{
					tempMsg = sendMsg;
				}
				this.webSocket.send(tempMsg); 
			}
		}
		
}
//JSON转换工具
var MY_JSON = {
	getJsonObj:function(jsonStr){
		return JSON.parse(jsonStr);
	},
	getJsonStr:function(jsonObj){
		return JSON.stringify(jsonObj);
	},
	sendJson:function(url,param,success,error,complete){
		 $.ajax({
			    url:url,
			    type:'POST', //GET
			    async:true,    //或false,是否异步
			    data:param,
			    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
			  //  contentType:'application/json',
			    success:function(data){
			    	if(success){
			    		success(data);
			    	}
			    },
			    error:function(xhr,textStatus){
			    	if(error){
			    		error();
			    	}
			    },
			    complete:function(){
			    	if(complete){
			    		complete();
			    	}
			    }
			})
	}
}

//生成原生String replaceAll函数
String.prototype.replaceAll = function(s1,s2){ 
	return this.replace(new RegExp(s1,"gm"),s2); 
	}