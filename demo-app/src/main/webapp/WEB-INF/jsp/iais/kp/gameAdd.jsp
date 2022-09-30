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
        <input type="hidden" id="categoryId" name="categoryId" value="">
        <br><br>
        <div class="bg-title"><h2>ADD Game</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Game_Name" />
                <iais:value width="7">
                    <iais:input name="gameName" id="gameName" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Game_Description" />
                <iais:value width="7">
                    <iais:input name="gameDescription" id="gameDescription" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Issue_Date" />
                <iais:value width="7">
                    <iais:datePicker name="issueDate" id="issueDate" value=""></iais:datePicker>
                </iais:value>
            </div>

            <div class="form-group">
                <iais:field value="Price" />
                <iais:value width="7">
                    <iais:input name="price" id="price" value=""></iais:input>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="back" onclick="Utils.submit('mainForm','doBack')">back</a>
                    <a class="btn btn-primary next" onclick="doAdd('<iais:mask name="categoryId" value="${categoryRequestDto.id}"/>')">ADD</a>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>


<script type="text/javascript">
    function doAdd(id){
        $("#categoryId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "addGame", id);
    }
</script>
