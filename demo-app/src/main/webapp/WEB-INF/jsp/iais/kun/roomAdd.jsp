<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="bg-title"><h2>ADD Room</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="room_Type" />
                <iais:value width="7">
                    <iais:input name="roomType" id="roomType" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="room_No" />
                <iais:value width="7">
                    <iais:input name="roomNO" id="roomNO" value=""></iais:input>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="btn btn-primary next" onclick="doAdd()">ADD</a>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>


<script type="text/javascript">
    function doAdd(){
        Utils.submit('mainForm','addRoom')
    }
</script>
