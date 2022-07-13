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
        <input type="hidden" id="roomId" name="roomId" value="">
        <br>
        <br>
        <div class="bg-title"><h2>Rooms</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Type" required="true"/>
                <iais:value width="7">
                    <iais:select name="roomType" id="roomType" options="roomTypeSelect" firstOption="Please Select" onchange="displaySection()"></iais:select>
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
            <iais:pagination  param="roomSearchParam" result="roomSearchResult"/>
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader style="width:40%" needSort="true"  field="id" value="id" ></iais:sortableHeader>
                        <iais:sortableHeader style="width:20%" needSort="true"  field="roomType" value="room_type"></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="true"   field="roomNo" value="room_no"></iais:sortableHeader>
                        <iais:sortableHeader style="width:10%" needSort="false"  field="" value=""></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: left">
                        <%-- rooms entity--%>
                        <c:forEach var = "room" items = "${roomSearchResult.rows}" varStatus="status">
                            <tr>
                                <td align="left" style="width: 40%"><iais:code code="${room.id}"></iais:code></td>
                                <td align="left" style="width: 30%"><iais:code code="${room.roomType}"></iais:code></td>
                                <td align="left" style="width: 20%"><iais:code code="${room.roomNo}"></iais:code></td>
                                <td align="left" style="width: 10%">
                                    <button type="button" value="${room.id}" onclick="prepareEdit('<iais:mask name="roomId" value="${room.id}"/>')"  class="btn btn-default btn-sm" >Edit</button>
                                </td>
                            </tr>
                        </c:forEach>
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

    function prepareEdit(id){
        $("#roomId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
    }

    $(document).ready(function() {
        displaySection()
    });

    function displaySection(){
        var val =  $("#roomType").val();
    }
</script>
