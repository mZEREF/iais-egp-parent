<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>

<%@ page contentType="text/html; charset=UTF-8"  %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<style>
    .column-sort{
        margin-top: 3px;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="msgQueryId" name="msgQueryId" value="">
    <br><br>

        <br>
        <div class="bg-title"><h2>Error Message Management</h2></div>
            <div class="form-horizontal">
                <div class="form-group">
                    <iais:field value="Type" required="true"/>
                    <iais:value width="7">
                        <iais:select name="domainType" id="domainType" value="${param.domainType}"  codeCategory="CATE_ID_ERR_MSG_TYPE"  firstOption="Please Select" onchange="displaySection()"></iais:select>
                    </iais:value>
                    <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                </div>

                <div id = "msgTypeRow" class="form-group" style="display:none">
                    <iais:field value="Message Type" />
                    <iais:value width="7">
                        <iais:select name="msgType" id="msgType" value="${param.msgType}"   codeCategory="CATE_ID_WRONG_TYPE"  filterValue="WRNTYPE003" firstOption="Please Select" onchange="displaySection()"></iais:select>
                    </iais:value>
                </div>

                <div id = "moduleTypeRow" class="form-group" style="display:none">
                    <iais:field value="Module" />
                    <iais:value width="7">
                        <iais:select name="module"  id="module" value="${param.module}" options="moduleTypeSelect" firstOption="Please Select" needSort="true"></iais:select>
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
                <h3>
                    <span>Search Results</span>
                </h3>
                <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>
                <br><br>
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr><th scope="col" style="display: none"></th>
                            <iais:sortableHeader style="width:1%" needSort="false"  field="" value="S/N" ></iais:sortableHeader>
                            <iais:sortableHeader style="width:5%" needSort="true"  field="domain_type" value="Type"></iais:sortableHeader>
                            <iais:sortableHeader style="width:5%" needSort="true"   field="msg_type" value="Message Type"></iais:sortableHeader>
                            <iais:sortableHeader style="width:10%" needSort="true"   field="module" value="Module"></iais:sortableHeader>
                            <iais:sortableHeader style="width:15%" needSort="true"   field="description" value="Description"></iais:sortableHeader>
                            <iais:sortableHeader style="width:5%" needSort="true"   field="message" value="Message"></iais:sortableHeader>
                            <iais:sortableHeader style="width:5%" needSort="true"   field="status" value="Status"></iais:sortableHeader>
                            <iais:sortableHeader style="width:5%" needSort="false"   field="action" value="Action"></iais:sortableHeader>
                        </tr>
                        </thead>
                        <tbody style="text-align: left">
                        <c:choose>
                            <c:when test="${empty msgSearchResult.rows}">
                                <tr>
                                    <td colspan="6">
                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <%-- message entity--%>
                                <c:forEach var = "msgQuery" items = "${msgSearchResult.rows}" varStatus="status">
                                    <tr>
                                        <td align="left" class="row_no" style="width: 5px">${(status.index + 1) + (msgSearchParam.pageNo - 1) * msgSearchParam.pageSize}</td>
                                        <td align="left" ><iais:code code="${msgQuery.domainType}"></iais:code></td>
                                        <td align="left" ><iais:code code="${msgQuery.msgType}"></iais:code></td>
                                        <td align="left" ><iais:code code="${msgQuery.module}"></iais:code></td>
                                        <td align="left" ><c:out value="${msgQuery.description}"></c:out></td>
                                        <td align="left" ><c:out value="${msgQuery.message}"></c:out></td>
                                        <td align="left" ><iais:code code="${msgQuery.status}"></iais:code></td>
                                        <td align="left">
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
        $("#msgQueryId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
    }

    function disable(id){
        $("#msgQueryId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
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
        var val =  $("#domainType").val();
        if(val == null || val == '' ){
            return;
        }
        $("#msgTypeRow").attr("style","display: block");
        if($("#msgType").val() == null || $("#msgType").val()== '' ){
            console.log("can not open div");
            return;
        }

        $("#moduleTypeRow").attr("style","display: block");

    }
</script>
