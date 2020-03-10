<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>

<form id="mainForm" method="post" action="https://egp.sit.inter.iais.com/main-web/eservice/INTERNET/FE_Croppass_Landing/1/croppassCallBack">
    <%@ include file="/include/formHidden.jsp" %>

</form>


<script>
    window.onload = function(){
        $("#mainForm").submit()
    }


</script>