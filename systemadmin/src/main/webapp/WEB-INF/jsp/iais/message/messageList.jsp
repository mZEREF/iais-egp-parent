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
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="msgQueryId" name="msgQueryId" value="">
    <br><br>

        <br>
        <div class="bg-title"><h2>ErrorMessage management</h2></div>
            <div class="form-horizontal">
                <div class="form-group">
                    <iais:field value="Type" required="true"/>
                    <iais:value width="7">
                        <iais:select name="domainType" id="domainType" value="${domainType}" options="domainTypeSelect" firstOption="Please Select" onchange="displaySection()"></iais:select>
                    </iais:value>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>

                <div id = "msgTypeRow" class="form-group" style="display:none">
                    <iais:field value="Message Type" />
                    <iais:value width="7">
                        <iais:select name="msgType" id="msgType" value="${msgType}" options="msgTypeSelect" firstOption="Please Select" onchange="displaySection()"></iais:select>
                    </iais:value>
                </div>

                <div id = "moduleTypeRow" class="form-group" style="display:none">
                    <iais:field value="Module" />
                    <iais:value width="7">
                        <iais:select name="module"  id="module" value="${module}" options="moduleTypeSelect" firstOption="Please Select" ></iais:select>
                    </iais:value>
                </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <div class="text-right">
                                <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                                <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
                            </div>
                        </div>
                    </div>

            </div>



            <div class="components">
                <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>
                <br><br>
                <div class="table-gp">
                    <table class="table">
                        <thead>
                        <tr>
                            <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"  field="domain_type" value="Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="msg_type" value="Message Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="message" value="Message"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="status" value="Status"></iais:sortableHeader>
                            <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                        </tr>
                        </thead>
                        <tbody style="text-align: center">
                        <c:choose>
                            <c:when test="${empty msgSearchResult.rows}">
                                <tr>
                                    <td colspan="6">
                                        <iais:message key="ACK018" escape="true"></iais:message>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <%-- message entity--%>
                                <c:forEach var = "msgQuery" items = "${msgSearchResult.rows}" varStatus="status">
                                    <tr>
                                        <td class="row_no">${(status.index + 1) + (msgSearchParam.pageNo - 1) * msgSearchParam.pageSize}</td>
                                        <td>${msgQuery.domainType}</td>
                                        <td>${msgQuery.msgType}</td>
                                        <td>${msgQuery.module}</td>
                                        <td align="left">${msgQuery.description}</td>
                                        <td align="left">${msgQuery.message}</td>
                                        <td><iais:code code="${msgQuery.status}"></iais:code></td>
                                        <td>
                                            <button type="button"   onclick="disable('<iais:mask name="msgQueryId" value="${msgQuery.id}"/>')"  class="btn btn-default btn-sm" >Delete</button>
                                            <button type="button"   onclick="prepareEdit('<iais:mask name="msgQueryId" value="${msgQuery.id}"/>')"  class="btn btn-default btn-sm" >Edit</button>
                                            <%--<iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${msgQuery.id}');"/>
                                            <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${msgQuery.id}');"/>--%>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>

                </div>
            </div>



</form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(id){
        if(confirm('Are you sure you want to edit ? ')){
            $("#msgQueryId").val(id);
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('Are you sure you want to disable ?')){
            $("#msgQueryId").val(id);
            SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
        }
    }


    function doClear() {
        $("#domainType option[text = 'Please Select']").val("selected", "selected");
        $("#domainType").val("");
        $("#msgType option[text = 'Please Select']").val("selected", "selected");
        $("#msgType").val("");
        $("#module option[text = 'Please Select']").val("selected", "selected");
        $("#module").val("");
        $(".form-horizontal .current").text("Please Select");
    }

    $(document).ready(function() {
        displaySection()
    });

    function displaySection(){
        var val = document.getElementById("domainType").value;
        if(val == null || val == '' ){
            return;
        }

        document.getElementById("msgTypeRow").style = "block";

        if(document.getElementById("msgType").value == null || document.getElementById("msgType").value == '' ){
            console.log("can not open div");
            return;
        }

        document.getElementById("moduleTypeRow").style = "block";

    }
</script>
