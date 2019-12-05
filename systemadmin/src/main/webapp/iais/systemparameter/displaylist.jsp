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


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Domain Type</label>
                            <div class="col-xs-12 col-md-5">
                                <iais:select name="domainType" id="domainType" options="domainTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
                            </div>
                        </td>
                    </div>



                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Module</label>
                            <div class="col-xs-12 col-md-5">
                                <iais:select name="module" id="module" options="moduleTypeSelect" firstOption="Please select"></iais:select>
                            </div>
                        </td>
                    </div>

                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Description</label>
                            <div class="col-xs-12 col-md-5">
                                <input id="desciption" type="text">
                            </div>
                        </td>
                    </div>

                    <div class="col-xs-12">
                        <td>
                            <label class="col-xs-12 col-md-3 control-label">Status</label>
                            <div class="col-xs-12 col-md-5">
                                <iais:select name="status" id="status" options="statusTypeSelect" firstOption="Please select"></iais:select>
                            </div>
                        </td>
                    </div>

                </div>

                    <c:if test = "${not empty errorMap}">
                        <div class="error">
                            <c:forEach items="${errorMap}" var="map">
                                ${map.key}  ${map.value} <br/>
                            </c:forEach>
                        </div>
                    </c:if>

                </br>
                <iais:pagination  param="systemParamSearch" result="systemParamSearchResult"/>
                <div class="table-responsive" id="no-more-tables">
                    <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
                        <colgroup>
                            <col style="width: 10%;"/>
                            <col style="width: 20%;"/>
                            <col style="width: 20%;"/>
                            <col style="width: 20%;"/>
                            <col style="width: 20%;"/>
                            <col style="width: 10%;"/>
                        </colgroup>
                        <thead>
                        <tr>
                            <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"  field="domain_type" value="Domain Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="valueType" value="Value Of Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="value" value="Value"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="status" value="Status"></iais:sortableHeader>
                        </tr>
                        </thead>

                        <tbody style="text-align: center">
                        <c:choose>
                            <c:when test="${empty systemParamSearchResult.rows}">
                                <tr>
                                    <td colspan="6">
                                        No Record!!
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <%-- message entity--%>
                                <c:forEach var = "resultRow" items = "${systemParamSearchResult.rows}" varStatus="status">
                                    <tr>
                                        <td class="row_no">${(status.index + 1) + (systemParamSearchParam.pageNo - 1) * systemParamSearchParam.pageSize}</td>
                                        <td>${resultRow.domainType}</td>
                                        <td>${resultRow.module}</td>
                                        <td>${resultRow.description}</td>
                                        <td>${resultRow.valueType}</td>
                                        <td>${resultRow.value}</td>
                                        <td>${resultRow.status}</td>
                                        <td>
                                            <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${resultRow.id}');"/>
                                            <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${resultRow.id}');"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>

            </div>
            <div class="application-tab-footer">
                <td>
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doQuery();">Search</a>

                    </div>

                </td>
            </div>
        </div>
    </div>
</>


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
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('are sure you want to disable ? ')){
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