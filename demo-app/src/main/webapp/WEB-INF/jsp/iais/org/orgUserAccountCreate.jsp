
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setLayout name="iais-internet"/>



<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="row">
            <div class="col-xs-12">
                <div class="form-horizontal">
    <c:choose>
        <c:when test="${empty orgUserAccountDto}">
            <h2>Org Account Create</h2>
        </c:when>
        <c:otherwise>
            <h2>Org Account Edit</h2>
        </c:otherwise>
    </c:choose>
    <iais:error>
        <c:if test = "${not empty errorMap}">
             <div class="error">
                 <c:forEach items="${errorMap}" var="map">
                     ${map.key}  ${map.value} <br/>
                 </c:forEach>
             </div>
        </c:if>
    </iais:error>
        <iais:row>
          <iais:field value="name" required="true"></iais:field>
          <iais:value width="7">
              <input type="text" name="name" value="${orgUserAccountDto.name}" />
          </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="nirc No" required="true"></iais:field>
            <iais:value width="7">
                <input type="text" name="nircNo" value="${orgUserAccountDto.nircNo}" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="corp Pass Id No" required="true"></iais:field>
            <iais:value width="7">
                <input type="text" name="corpPassId" value="${orgUserAccountDto.corpPassId}" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Status" required="true"></iais:field>
            <iais:value width="7">
                <iais:select name="status" options="statusSelect" firstOption="Please select" value="${orgUserAccountDto.status}" ></iais:select>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Test" required="true"></iais:field>
            <iais:value width="7">
                <iais:select name="test" codeCategory="CATE_ID_NATIONALITY"/>
            </iais:value>
        </iais:row>
        <iais:action>
            <button type="button"  class="btn btn-default" onclick="javascript:doCancel();">Cancel</button>
            <c:choose>
                <c:when test="${empty orgUserAccountDto || !orgUserAccountDto.editFlag}">
                    <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doCreate('${orgId}');">Submit</button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doEdit('');">Edit</button>
                </c:otherwise>
            </c:choose>
        </iais:action>
                </div></div></div></div>
</form>

<script type="text/javascript">
function doCancel(){
SOP.Crud.cfxSubmit("mainForm","cancel");
}
function doCreate(orgId){
SOP.Crud.cfxSubmit("mainForm","save",orgId);
}
function doEdit(rowguid){
    SOP.Crud.cfxSubmit("mainForm","edit",rowguid);
}

</script>



























