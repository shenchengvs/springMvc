<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="base/top.jsp"%>
<html>
<head>
<%@include file="base/js.jsp"%>
</head>
<body>
<div>
    <form action="ent/uploadFile" name="Form" id="Form" method="post" enctype="multipart/form-data">
        <div>
            <table id="table_report" class="table table-striped table-bordered table-hover">

                <tr>
                    <td style="width:70px;text-align: right;padding-top: 13px;">${name}名称:</td>
                    <td>
                        <input type="text" name="name" id="nameID"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:70px;text-align: right;padding-top: 13px;">头像:</td>
                    <td><input type="file" name="headImg" id="headImgID"/></td>
                </tr>

                <tr>
                    <td style="text-align: center;" colspan="10">
                        <input class="btn btn-primary" type="submit"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <form action="ent/queryEntExcel" method="post">
    	 <input class="btn btn-primary" type="submit" value="excel"/>
    </form>
    <input type="button" value="test" id="testBtn"/>
</div>
</body>
<script type="text/javascript">
var param={};
function success(data){
	alert(MY_JSON.getJsonStr(data))
}

$("#testBtn").on("click",function(){
	param.pageNum = 1;
	param.pageSize = 1;
	param.ages=[1,2]
	MY_JSON.sendJson('ent/queryEntInfo',param, success);
});
</script>
</html>
