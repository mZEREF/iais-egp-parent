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



<webui:setLayout name="iais-intranet"/>


<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="currentValidateId" value="">
        <div class="bg-title"><h2>System Parameters</h2></div>

        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-md-1">Domain Type:
                </label>
                <div class="col-md-3">
                    <iais:select name="domainType" id="domainType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE" value="${domainType}"></iais:select>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-1">Module:
                </label>
                <div class="col-md-3">
                    <iais:select name="module" id="module"  codeCategory = "CATE_ID_SYSTEM_PARAMETER_MODULE" firstOption="Please select" value="${module}"></iais:select>
                    <span id="error_module" name="iaisErrorMsg" class="error-msg"></span>
                </div>

            </div>

            <div class="form-group">
                <label class="col-md-1">Description:
                </label>
                <div class="col-md-3">
                    <input id="description" name="description" type="text" value="${description}">
                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-1">Status:
                </label>
                <div class="col-md-3">
                    <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                                 firstOption="Select Status" filterValue="CMSTAT002" value="${status}"></iais:select>
                </div>
                <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
            </div>

            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="tab-content">
                    <div class="row">
                        <br><br>
                        <div class="col-xs-12">
                            <iais:pagination  param="systemParamSearch" result="systemParamSearchResult"/>
                            <div class="components">
                                <div class="table-gp">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"  field="domain_type" value="Domain Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="paramType" value="Param Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="value" value="Value"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="status" value="Status"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${empty systemParamSearchResult.rows}">
                                                <tr>
                                                    <td colspan="6">
                                                        No Record!!
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="resultRow" items="${systemParamSearchResult.rows}" varStatus="status">
                                                    <tr>
                                                        <td class="row_no">${(status.index + 1) + (systemParamSearchParam.pageNo - 1) * systemParamSearchParam.pageSize}</td>
                                                        <td><iais:code code="${resultRow.domainType}"></iais:code></td>
                                                        <td><iais:code code="${resultRow.module}"></iais:code></td>
                                                        <td>${resultRow.description}</td>
                                                            <td><iais:code code="${resultRow.paramType}"></iais:code></td>
                                                        <td>${resultRow.value}</td>
                                                        <td><iais:code code="${resultRow.status}"></iais:code></td>
                                                        <td>
                                                            <c:if test="${resultRow.update == true }">
                                                                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${resultRow.id}');"/>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>


                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                    <div class="text-right text-center-mobile">
                                        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doQuery();">Search</a>
                                    </div>


                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>



    </>

</div>



<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function doQuery(){
        SOP.Crud.cfxSubmit("mainForm", "doQuery");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(id){
        if(confirm('Are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('Are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
        }
    }

    function displaySection(){
        var val = document.getElementById("domainType").value;
        if(val == null){
            return;
        }

        var msgTypeRow = document.getElementById("msgTypeRow");
        var moduleTypeRow = document.getElementById("moduleTypeRow");
        if(msgTypeRow && moduleTypeRow){
            msgTypeRow.style = "block";
            moduleTypeRow.style = "block";
        }
    }

</script>
