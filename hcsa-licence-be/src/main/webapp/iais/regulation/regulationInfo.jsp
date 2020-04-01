<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/30
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="currentValidateId" value="">
        <br><br>
        <div class="bg-title"><h2>Regulation Update</h2></div>
        <span id="error_customErrorMessage" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
        <div class="form-horizontal">


        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <a class="back" href="#" id="crud_cancel_link" value="doCancel"><i class="fa fa-angle-left"></i>Back</a>
            </div>
            <div class="col-xs-3 col-sm-3 col-md-offset-3">
                <div class="button-group">
                    <a class="btn btn-primary" href="#" onclick="Utils.submit('mainForm', 'doEdit', '${parameterRequestDto.id}')">Edit</a></div>
            </div>
        </div>
    </form>
</div>
<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>