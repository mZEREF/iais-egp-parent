<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>

    <br><br>
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="domainType" id="domainType" value="${domainType}" options="domainTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>

                    </div>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>

                <div id = "msgTypeRow" class="form-group" >
                    <label class="col-xs-4 col-md-2 control-label" >Message Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="msgType" value="${msgType}" options="msgTypeSelect" firstOption="Please select" ></iais:select>
                    </div>
                </div>

                <div id = "moduleTypeRow" class="form-group" >
                    <label class="col-xs-4 col-md-2 control-label" >Module</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="module"  value="${module}" options="moduleTypeSelect" firstOption="Please select" ></iais:select>
                    </div>
                </div>
            </div>

            <iais:action style="text-align:center;">
                <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doSearch()">Search</button>
                <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doClear()">Clear</button>
            </iais:action>

            <div class="components">
                <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>
                <h2 class="component-title">Search &amp; Results</h2>
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
                                        No Record!!
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
                                            <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${msgQuery.id}');"/>
                                            <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${msgQuery.id}');"/>
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
</form>
<%@include file="/include/validation.jsp"%>
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
        if(confirm('Are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
        }
    }


    function doClear() {
        $("#domainType option[text = 'Please select']").val("selected", "selected");
        $("#domainType").val("");
        $("#msgType option[text = 'Please select']").val("selected", "selected");
        $("#msgType").val("");
        $("#module option[text = 'Please select']").val("selected", "selected");
        $("#module").val("");
        $(".current").text("Please select");
    }

</script>
