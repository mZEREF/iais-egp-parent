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
        <input type="hidden" id="categoryId" name="categoryId" value="${categoryRequestDto.id}">
        <br><br>
        <div class="bg-title"><h2>Add Game</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Game Name" />
                <iais:value width="8">
                    <iais:input name="gameName" id="gameName" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Description" />
                <iais:value width="7">
                    <iais:input name="gameDescription" id="gameDescription" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Price" />
                <iais:value width="8">
                    <iais:input name="price" id="price" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Issue Date" />
                <iais:value width="7">
                    <iais:datePicker name="issueDate" id="issueDate" value=""></iais:datePicker>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="back btn btn-secondary" onclick="doBack1('<iais:mask name="categoryId" value="${sampleGameCategoryDto.id}"/>')">back</a>
                    <a class="btn btn-primary next" onclick="doAdd('<iais:mask name="categoryId" value="${sampleGameCategoryDto.id}"/>')">ADD</a>
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
    function doBack1(id){
        $("#categoryId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
    }
</script>
