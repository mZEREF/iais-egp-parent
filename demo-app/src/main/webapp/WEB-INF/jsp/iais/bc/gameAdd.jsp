<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">
<head>
    <script type="text/css">
        .add{
            font-size: 1.6rem;
            font-weight: 700;
            background: #428bca;
            border: 1px solid #357ebd;
            color: #fff;
            padding: 12px 40px;
            text-transform: uppercase;
            border-radius: 30px;
        }
    </script>
</head>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" id="categoryId" name="categoryId" value="${categoryRequestDto.id}">
        <br><br>
        <div class="bg-title"><h2>ADD GAME</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Game Name" />
                <iais:value width="7">
                    <iais:input name="gameName" id="gameName" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Description" />
                <iais:value width="7">
                    <iais:input name="description" id="description" value=""></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Issue Date" />
                <iais:value width="7">
                    <iais:datePicker name="issueDate" id="issueDate" value=""></iais:datePicker>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="price" />
                <iais:value width="7">
                    <iais:input name="price" id="price" value=""></iais:input>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="btn btn-danger" onclick="Utils.submit('mainForm','doBack')">back</a>
                </div>
            </div>
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="add" onclick="doAdd('<iais:mask name="categoryId" value="${categoryRequestDto.id}"/>')">ADD</a>
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
