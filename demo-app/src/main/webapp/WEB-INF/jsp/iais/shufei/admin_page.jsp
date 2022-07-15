<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>

<%@ page contentType="text/html; charset=UTF-8"  %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <br><br>

        <br>
        <div class="bg-title"><h2>Room Person Message Management</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Room Type" required="true"/>
                <iais:value width="7">
                    <iais:select name="domainType" id="domainType" optionsSelections="${roomType}" value="${param.roomType}" firstOption="Please Select" onchange="displaySection()"></iais:select>
                </iais:value>
                <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
            </div>

            <div id = "msgTypeRow" class="form-group">
                <iais:field value="Room No" required="true"/>
                <iais:value width="7">
                    <iais:select name="msgType" id="msgType" optionsSelections="${roomNo}" value="${param.roomNo}" firstOption="Please Select" onchange="displaySection()"></iais:select>
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
                <span>Search results based on room information</span>
            </h3>
            <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>
            <br><br>
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr><th scope="col" style="display: none"></th>
                        <iais:sortableHeader style="width:1%" needSort="false"  field="" value="NO." ></iais:sortableHeader>
                        <iais:sortableHeader style="width:5%" needSort="false"   field="display_name" value="Display Name"></iais:sortableHeader>
                        <iais:sortableHeader style="width:10%" needSort="false"   field="module" value="Mobile No"></iais:sortableHeader>
                        <iais:sortableHeader style="width:10%" needSort="true"   field="office_tel_no" value="Office Tel No"></iais:sortableHeader>
                        <iais:sortableHeader style="width:5%" needSort="false"   field="email_no" value="Email No"></iais:sortableHeader>
                        <iais:sortableHeader style="width:5%" needSort="false"   field="action" value="action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: left">
<%--                    <c:choose>--%>
<%--                        <c:when test="${empty demoSearchResult}">--%>
<%--                            <tr>--%>
<%--                                <td colspan="6">--%>
<%--                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>--%>
<%--                                </td>--%>
<%--                            </tr>--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
                            <%-- message entity--%>
                            <c:forEach var = "s" items = "${msgSearchResult.rows}" varStatus="status">
                                <tr>
                                    <td align="left" class="row_no" style="width: 5px">${(status.index + 1) + (demoSearchParam.pageNo - 1) * demoSearchParam.pageSize}</td>
                                    <td align="left" ><iais:code code="${s.displayName}"></iais:code></td>
                                    <td align="left" ><iais:code code="${s.mobileNo}"></iais:code></td>
                                    <td align="left" ><iais:code code="${s.officeTelNo}"></iais:code></td>
                                    <td align="left" ><iais:code code="${s.emailAddr}"></iais:code></td>
                                    <td align="left">
                                        <a href="javascript:void(0);" onclick="javascript:doEdit('<iais:mask name="crud_action_value" value="${s.id}"/>');">Edit</a>
                                    </td>
                                </tr>
                            </c:forEach>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
                    </tbody>
                </table>
                <iais:action>
                    <button class="btn btn-lg btn-login-submit" type="button" onclick="javascript:doCreat();">Create</button>
                </iais:action>
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

    function doEdit(personId){
        $("#peronId").val(personId);
        window.alert(personId);
        SOP.Crud.cfxSubmit("mainForm", "doEdit", personId);
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
        $(".form-horizontal .current").text("Please Select");
    }

    $(document).ready(function() {
        displaySection()
    });

    function doCreat() {
        SOP.Crud.cfxSubmit("mainForm","doCreate");
    }

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
