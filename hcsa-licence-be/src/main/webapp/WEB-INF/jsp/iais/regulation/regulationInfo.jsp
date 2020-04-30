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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="currentValidateId" value="">
        <br><br>
        <div class="bg-title">
            <c:choose>
                <c:when test="${isUpdate eq 'Y'}">
                    <h2>Regulation Update</h2>
                </c:when>
                <c:otherwise>
                    <h2>Regulation Create</h2>
                </c:otherwise>
            </c:choose>

        </div>
        <span id="error_customErrorMessage" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Regulation Clause Number" required="true"/>
                <div class="col-xs-5 col-md-3" >
                    <input type="text" name="regulationClauseNo" maxlength="100" value="${regulationAttr.clauseNo}" />
                    <span id="error_clauseNo" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>


            <div class="form-group">
                <iais:field value="Regulations" required="true" />
                <div class="col-xs-5 col-md-3" >
                    <textarea cols="70" rows="7" name="regulationClause" id="regulationClause" maxlength="2000"><c:out value="${regulationAttr.clause}"> </c:out></textarea>
                    <span id="error_clause" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <a class="back" href="#" id="crud_cancel_link" value="doBack"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-3 col-sm-3 col-md-offset-3">
                <div class="button-group">
                    <c:choose>
                        <c:when test="${isUpdate eq 'Y'}">
                             <a class="btn btn-primary" href="#" onclick="Utils.submit('mainForm', 'doCreateOrUpdate', 'update')">Update</a></div>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-primary" href="#" onclick="Utils.submit('mainForm', 'doCreateOrUpdate', 'create')">Create</a></div>
                        </c:otherwise>
                    </c:choose>

            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>