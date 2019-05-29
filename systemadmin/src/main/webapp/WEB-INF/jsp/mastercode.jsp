<%@ page import="com.ecquaria.cloud.moh.iais.entity.MasterCode" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Master Code List</title>
</head>
<body>
    <%
        List<MasterCode> lc = (ArrayList)request.getAttribute("masterCodeList");

        for(MasterCode c : lc){
            System.out.println(c.getId());
        }
    %>

</body>
</html>
