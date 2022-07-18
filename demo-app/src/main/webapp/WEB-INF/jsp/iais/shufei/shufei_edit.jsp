
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
                    <h2>Person Edit</h2>
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
                        <iais:field value="Room No" required="true"></iais:field>
                        <iais:value width="7">
                            <iais:select name="roomNo" options="roomNo" firstOption="Please select" value="${ShuFeiCreateSampleDto.roomNo}" ></iais:select>
                            <span id="error_roomNo" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Room Type" required="true"></iais:field>
                        <iais:value width="7">
                            <iais:select name="roomType"  firstOption="Please select" options="roomType" value="${ShuFeiCreateSampleDto.roomType}"></iais:select>
                            <span id="error_roomType" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Display Name" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="displayName" maxlength="12" value="${ShuFeiCreateSampleDto.displayName}" />
                            <br><br><span id="error_displayName" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Mobile NO" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="mobileNo" maxlength="8" value="${ShuFeiCreateSampleDto.mobileNo}" />
                            <br><br><span id="error_mobileNo" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Office Tel NO" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="officeTelNo" maxlength="5" value="${ShuFeiCreateSampleDto.officeTelNo}" />
                            <br><br><span id="error_officeTelNo" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Email Addr" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="emailAddr" maxlength="18" value="${ShuFeiCreateSampleDto.emailAddr}" />
                            <br><br><span id="error_emailAddr" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:action>
                        <button type="button"   onclick="doEdit('<iais:mask name="personId" value="${ShuFeiCreateSampleDto.id}"/>')"  class="btn btn-default btn-sm" >Edit</button>
                    </iais:action>
                </div></div></div></div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function doEdit(personId){
        $("#personId").val(personId);
        window.alert(personId);
        SOP.Crud.cfxSubmit("mainForm","edit",personId);
    }

</script>



























