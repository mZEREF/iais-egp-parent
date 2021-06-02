<%--
  Created by IntelliJ IDEA.
  User: hyr
  Date: 2021/5/19
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Merchant Page</title>
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
</head>
<body>
<div class="row">
<img id="payNowImg" alt="Scan me!" src="date:image/png;vase64,${imageStreamInBase64Format}" >
</div>
<div class="application-tab-footer">
    <div class="row" style="padding-top: 10px;">
        <a class="btn btn-primary" style="float:right"
           href="${payNowCallBackUrl}">Submit</a>
        <span style="float:right">&nbsp;</span>
        <a class="btn btn-secondary" style="float:right"
           href=${payNowCallBackUrl}>Cancel</a>
    </div>
</div>
<script>
    window.setTimeout(payNowImgStringRefresh, 120000);
</script>
</body>
</html>
<script>

    function payNowImgStringRefresh(){
        setInterval(function(){ $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/payNowRefresh",
            async:true,
            processData: false,
            contentType: false,
            dataType: "text",
            success: function (data) {
                $('#payNowImg').src("date:image/png;vase64,"+data);
            },
            error: function (msg) {
            }
        }); }, 9000);

    }
</script>
