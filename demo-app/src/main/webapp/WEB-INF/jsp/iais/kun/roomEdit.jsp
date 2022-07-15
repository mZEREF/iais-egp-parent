<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="bg-title"><h2>Room Update</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="room_Type" />
                <iais:value width="7">
                    <iais:select name="roomType" id="roomType" options="roomTypeSelect" firstOption="${roomRequestDto.roomType}"></iais:select>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="room_No" />
                <iais:value width="7">
                    <iais:input name="roomNO" id="roomNO" value="${roomRequestDto.roomNO}"></iais:input>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="back" onclick="Utils.submit('mainForm','doBack')">back</a>
                    <a class="btn btn-primary next" onclick="doEdit()">Update</a>
                </div>
            </div>
        </div>

        <div class="components">
            <h3>
                <span>Persons in this room</span>
            </h3>
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader style="width:1%" needSort="false"  field="" value="S/N" ></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="true"  field="displayName" value="displayName"></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="true"   field="mobileNo" value="mobileNo"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: left">
                    <%-- person entity--%>
                    <c:forEach var = "person" items = "${personResult.rows}" varStatus="status">
                        <tr>
                            <td align="left" class="row_no" style="width: 5px">${(status.index + 1) + (msgSearchParam.pageNo - 1) * msgSearchParam.pageSize}</td>
                            <td align="left" ><iais:code code="${person.displayName}"></iais:code></td>
                            <td align="left" ><iais:code code="${person.mobileNo}"></iais:code></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <button type="button"  onclick="doAdd()"  class="bbtn btn-primary" >ADD</button>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>


<script type="text/javascript">
    function doEdit(){
        if ($("#roomType").val()==null||""==$("#roomType").val()){
            $("#roomType").val(${roomRequestDto.roomType})
        }
        Utils.submit('mainForm','doEdit', '${roomRequestDto.id}')
    }

    function doAdd(){
        SOP.Crud.cfxSubmit("mainForm", "prepareAddPerson");
    }

</script>
