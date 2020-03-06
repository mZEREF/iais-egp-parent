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
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="currentValidateId" value="">
        <div class="bg-title"><h2>System Parameters</h2></div>

      <span id="error_customErrorMessage" name="iaisErrorMsg" class="error-msg"></span>
      <br><br>

        <div class="form-horizontal">
            <%--<div class="form-group">
                <label class="col-md-1">System Parameter Type:
                </label>
                <div class="col-md-3">
                    <iais:select name="domainType" id="domainType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE"
                                 value="${parameterRequestDto.domainType}"></iais:select>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-1">Module:
                </label>
                <div class="col-md-3">
                    <iais:select name="module" id="module"  codeCategory = "CATE_ID_SYSTEM_PARAMETER_MODULE" firstOption="Please select" value="${parameterRequestDto.module}"></iais:select>
                    <span id="error_module" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>--%>

           <%-- <div class="form-group">
                <label class="col-md-1">Type of Value:
                </label>
                <div class="col-md-3">
                    <iais:select name="paramType" id="paramType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE_OF_VALUE" value="${parameterRequestDto.paramType}"></iais:select>
                    <span id="error_paramType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>--%>

            <div class="form-group">
                <label class="col-md-1">Value:
                </label>
                <div class="col-md-3">
                    <c:choose>
                        <c:when test="${parameterRequestDto.paramType == 'TPOF00007' || parameterRequestDto.paramType == 'TPOF00008'}">
                            <c:if test="${parameterRequestDto.value == '1'}">
                                Yes <input name="value" type="radio" value="1" checked>
                                No <input name="value" type="radio" value="0">
                            </c:if>

                            <c:if test="${parameterRequestDto.value == '0'}">
                                Yes <input name="value" type="radio" value="1" >
                                No <input name="value" type="radio" value="0" checked>
                            </c:if>

                        </c:when>
                        <c:otherwise>
                            <input name="value" type="text" maxlength="5" value="${parameterRequestDto.value}">
                        </c:otherwise>
                    </c:choose>
                    <span id="error_value" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-1">Parameter Description:
                </label>
                <div class="col-md-3">
                    <input name="description" type="text" maxlength="500" value="${parameterRequestDto.description}">
                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
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
        <td>
            <div class="text-right text-center-mobile">
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doCancel();">Cancel</a>
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doEdit('${parameterRequestDto.id}');">Edit</a>
            </div>

        </td>





    </form>
</div>
<%@include file="/include/validation.jsp"%>
<script type="text/javascript">


    function doEdit(id){
        if(confirm('Are you sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "doEdit", id);
        }
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }

</script>
