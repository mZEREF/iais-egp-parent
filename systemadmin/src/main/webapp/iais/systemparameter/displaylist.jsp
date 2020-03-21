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
<%@ page contentType="text/html; charset=UTF-8"  %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>



<webui:setLayout name="iais-intranet"/>


<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="currentValidateId" value="">

        <br><br>
        <div class="bg-title"><h2>System Parameters</h2></div>

        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Type of System Parameter:"   required="true"/>
                <div class="col-md-3">
                    <iais:select name="domainType" id="domainType"
                                 firstOption="Please select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE" value="${domainType}"></iais:select>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Module:"  />
                <div class="col-md-3">
                    <iais:select name="module" id="module"  codeCategory = "CATE_ID_SYSTEM_PARAMETER_MODULE" firstOption="Please select" value="${module}"></iais:select>
                    <span id="error_module" name="iaisErrorMsg" class="error-msg"></span>
                </div>

            </div>

            <div class="form-group">
                <iais:field value="Parameter Description:"  />
                <div class="col-md-3">
                    <input id="description" name="description" maxlength="500" type="text" value="${description}">
                    <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Status:"  />
                <div class="col-md-3">
                    <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                                 firstOption="Select Status" filterValue="CMSTAT002,CMSTAT004" value="${status}"></iais:select>
                </div>
                <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
            </div>


            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-11">
                        <div class="text-right">
                            <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                            <a class="btn btn-primary" id="crud_search_button" value="doQuery" href="#">Search</a>
                        </div>
                    </div>
                </div>
            </div>

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
                                            <iais:sortableHeader needSort="true"  field="domain_type" value="System Parameter Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="description" value="Parameter Description"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"   field="param_type" value="Type of Value"></iais:sortableHeader>
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
                                                        <iais:message key="ACK018" escape="true"></iais:message>
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

                                                        <c:choose>
                                                            <c:when test="${resultRow.paramType == 'TPOF00007'
                                                            || resultRow.paramType == 'TPOF00008'}">
                                                                <c:if test="${resultRow.value == '1'}">
                                                                    <td>Yes</td>
                                                                </c:if>
                                                                <c:if test="${resultRow.value == '0'}">
                                                                    <td>No</td>
                                                                </c:if>

                                                            </c:when>
                                                            <c:otherwise>
                                                                <td>${resultRow.value}</td>
                                                            </c:otherwise>
                                                        </c:choose>


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
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>





    </>

</div>



<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">
    function prepareEdit(id){
        if(confirm('Are you sure you want to edit ? ')){
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
