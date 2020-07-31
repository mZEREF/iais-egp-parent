<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/17
  Time: 14:01
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<style>
    .center{
        　　text-align:center;
    }
    center_text{
        　　display:inline-block;
        　　width:500px
    }

</style>

<webui:setLayout name="iais-intranet"/>

<br><br><br><br>
<div class="center col-xs-12 col-sm-12" style="text-align: center;height: 30%">

</div>
<div class="center col-xs-12 col-sm-12" style="text-align: center;height:45%">
        <span class="center_text">
            <strong style="font-size: 30px"> <c:out value="${errorMsg}"></c:out></strong>
        </span>
</div>

<div class="text-right text-center-mobile col-xs-12 col-sm-12" >
    <a class="btn btn-primary next" href="/main-web/" >Exit</a>
</div>
<script>
    $(function () {
        $("body").children("div").css('height',$(window).height()-100)
    })

</script>
