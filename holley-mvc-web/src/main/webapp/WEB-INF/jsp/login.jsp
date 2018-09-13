<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="base/top.jsp"%>
<html>
<head>
<%@include file="base/js.jsp"%>
</head>
<body>
<div>${errorMsg}</div>
<form  id="loginForm" action="frame/login" method="post" >
用户名:
<input type="text" name="username"  id="username" value="" style="background-color: white;border: 1px solid #a9a9a9" size="20" maxlength="20">

密码:
<input name="password" id="password" value="" style="background-color: white;border: 1px solid #a9a9a9" type="password" size="20" maxlength="20">

验证码：
<input type="text" value=""  style="background-color: white;border: 1px solid #a9a9a9" name="validCode" class="txtCode" id="validCode" size="10" />&nbsp; &nbsp; 

<img onclick="javascript:changeImg();" title="换一张试试" name="validCodeImg" id="validCodeImg" src="common/validateCode" width="60" height="20" border="1" align="absmiddle">
<input type="checkbox" id="rememberMe" name="rememberMe">记住我</input>
<input type="submit" value="submit"/>
</form>
</body>
<script type="text/javascript">
function changeImg() {
    var imgSrc = $("#validCodeImg");
 //   var src = imgSrc.attr("src");
    var timestamp = (new Date()).valueOf();
    imgSrc.attr("src", "common/validateCode?timestamp="+timestamp);
}
</script>
</html>
