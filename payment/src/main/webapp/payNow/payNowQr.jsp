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
<img id="payNowImg" alt="Scan me!" src="date:image/png;vase64,${imageStreamInBase64Format}" >
</body>
</html>
<script>
    function payNowImgStringRefresh(){
        $.ajax({
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
        });
    }
</script>
