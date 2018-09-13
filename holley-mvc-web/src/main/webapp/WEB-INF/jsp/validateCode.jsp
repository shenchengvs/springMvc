<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="base/top.jsp"%>
<html>
<head>
<%@include file="base/js.jsp"%>
</head>
<body>
<div class="form-group  col-lg-6">
    <label for="id" class="col-sm-4 control-label">
        验证码:
    </label>
    <div class="col-sm-8">
        <input type="text" id="code" name="code" class="form-control" style="width:250px;"/>
        <img id="imgObj" alt="验证码" src="common/validateCode" onclick="changeImg()"/>
        <a href="javascript:;" onclick="changeImg()">换一张</a>
    </div>
</div>
 
<script type="text/javascript">
    // 刷新图片
    function changeImg() {
        var imgSrc = $("#imgObj");
      //  var src = imgSrc.attr("src");
        var timestamp = (new Date()).valueOf();
        imgSrc.attr("src", "common/validateCode?timestamp="+timestamp);
    }
</script>
</body>
</html>
