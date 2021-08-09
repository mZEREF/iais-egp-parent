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
        <input itype="textKey13" style="display:none" />
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="currentValidateId" value="">

        <br><br>
        <div class="bg-title"><h2>System Parameters</h2></div>

        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Type of System Parameter:"   required="true"/>
                <div class="col-md-3">
                    <iais:select name="domainType" id="domainType"
                                 firstOption="Please Select" codeCategory="CATE_ID_SYSTEM_PARAMETER_TYPE" value="${domainType}"></iais:select>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>

            <div class="form-group">
                <iais:field value="Module:"  />
                <div class="col-md-3">
                    <iais:select name="module" id="module"  codeCategory = "CATE_ID_SYSTEM_PARAMETER_MODULE" firstOption="Please Select" value="${module}"></iais:select>
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
                                 firstOption="Please Select" filterValue="CMSTAT002,CMSTAT004,DRAFT001" value="${status}"></iais:select>
                </div>
                <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
            </div>


            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-11">
                        <div class="text-right">
                            <a class="btn btn-secondary" onclick="clearStatus()"  href="#">Clear</a>
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

                            <h3>
                                <span>Search Results</span>
                            </h3>
                            <iais:pagination  param="systemParamSearch" result="systemParamSearchResult"/>
                            <div class="components">
                                <div >
                                    <div class="table-gp">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr><th scope="col" style="display: none"></th>
                                            <iais:sortableHeader needSort="false" style="width:5%; " field="" value="No."></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true"  style="width:18%" field="domain_type" value="System Parameter Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" style="width:10%"  field="module" value="Module"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" style="width:30%"  field="description" value="Parameter Description"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" style="width:10%"  field="param_type" value="Type of Value"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" style="width:10%"  field="value" value="Value"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" style="width:10%"  field="status" value="Status"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" style="width:10%; "  field="action" value="Action"></iais:sortableHeader>
                                        </tr>
                                        </thead>
                                        <tbody style="text-align: left">
                                        <c:choose>
                                            <c:when test="${empty systemParamSearchResult.rows}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
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
            </div>





    </>

</div>



<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
    function prepareEdit(id){
        SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
    }

    function clearStatus() {
        $('#domainType').val("");
        $('#module').val("");
        $('#description').val("");
        $('#status').val("");
        $(".form-horizontal option[text = 'Please Select']").val("selected", "selected");
        $(".form-horizontal").val("");
        $(".form-horizontal .current").text("Please Select");
        $(".error-msg").text("");
    }

    function disable(id){
        SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
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
