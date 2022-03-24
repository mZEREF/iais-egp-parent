<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/4/2019
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    .form-check-gp{
        width: 50%;
        float:left;
    }

</style>


<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="currentValidateId" value="">
        <br><br>
        <div class="bg-title"><h2>System Parameters Update</h2></div>

        <br><br>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Type of System Parameter:"  />
                </label>
                <div class="col-md-3">
                    <iais:select name="domainType" id="domainType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE"
                                 value="${parameterRequestDto.domainType}" disabled="true"></iais:select>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Module:"  />
                </label>
                <div class="col-md-3">
                    <iais:select name="module" id="module"  codeCategory = "CATE_ID_SYSTEM_PARAMETER_MODULE" disabled="true" firstOption="Please select" value="${parameterRequestDto.module}"></iais:select>
                    <span id="error_module" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Type of Value:"  />
                <div class="col-md-3">
                    <iais:select name="paramType" id="paramType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE_OF_VALUE" disabled="true" value="${parameterRequestDto.paramType}"></iais:select>
                    <span id="error_paramType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Value:" required="true" />
                <div class="col-md-3">
                    <c:choose>
                        <c:when test="${parameterRequestDto.paramType == 'TPOF00007' || parameterRequestDto.paramType == 'TPOF00008'}">
                            <c:if test="${parameterRequestDto.value == '1'}">
                                <input name="value" type="radio" value="1" checked> Yes &nbsp;
                                <input name="value" type="radio" value="0"> No
                            </c:if>

                            <c:if test="${parameterRequestDto.value == '0'}">
                                <input name="value" type="radio" value="1" > Yes &nbsp;
                                <input name="value" type="radio" value="0" checked> No
                            </c:if>

                        </c:when>
                        <c:otherwise>
                            <input name="value" type="text" maxlength="100" value="${parameterRequestDto.value}">
                        </c:otherwise>
                    </c:choose>
                    <span id="error_value" name="iaisErrorMsg" class="error-msg"></span>
                    <span id="error_customErrorMessage" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Parameter Description:" required="true" />
                <div class="col-md-3">
                    <textarea cols="70" rows="7" name="description" id="description" maxlength="1000"><c:out value="${parameterRequestDto.description}"></c:out></textarea>
                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Update On:"  />
                <div class="col-md-3">
                    <p><fmt:formatDate value="${parameterRequestDto.modifiedAt}" pattern="dd/MM/yyyy"/></p>
                </div>
            </div>


            <div class="form-group">
                <iais:field value="Update By:"  />
                <div class="col-md-3">
                    <p><input name="modifiedAt" type="text" id="modifiedBy" title=""  readonly value="${parameterRequestDto.modifiedByName}"></p>
                </div>
            </div>


           <%-- <div class="form-group">
                <label class="col-md-1">Status:
                </label>
                <div class="col-md-3">
                    <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                                 firstOption="Select Status" filterValue="CMSTAT002" value="${parameterRequestDto.status}"></iais:select>
                    <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>--%>
        </div>


        <%--<td>
            <div class="text-right text-center-mobile">
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doCancel();">Cancel</a>
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doEdit('${parameterRequestDto.id}');">Edit</a>
            </div>

        </td>--%>



        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <a class="back" href="#" id="crud_cancel_link" value="doCancel" ><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-3 col-sm-3 col-md-offset-3">
                <div class="button-group">
                    <a class="btn btn-primary" href="#" onclick="Utils.submit('mainForm', 'doEdit', '${parameterRequestDto.id}')">Update</a></div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>