
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
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <br><br>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="row">
            <div class="col-xs-12">
                <div class="form-horizontal">
                 <h2>Person Create</h2>
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
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Room Type" required="true"></iais:field>
                        <iais:value width="7">
                            <iais:select name="roomType"  firstOption="Please select" options="roomType" value="${ShuFeiCreateSampleDto.roomType}"></iais:select>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Display Name" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="displayName" value="${ShuFeiCreateSampleDto.displayName}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Mobile NO" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="mobileNo" value="${ShuFeiCreateSampleDto.mobileNo}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Office Tel NO" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="officeTelNo" value="${ShuFeiCreateSampleDto.officeTelNo}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Email Addr" required="true"></iais:field>
                        <iais:value width="7">
                            <input type="text" name="emailAddr" value="${ShuFeiCreateSampleDto.emailAddr}" />
                        </iais:value>
                    </iais:row>

                </div></div></div></div>
                    <iais:action>
                        <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doCreate('${ShuFeiCreateSampleDto.roomType}','${ShuFeiCreateSampleDto.roomNo}');">Submit</button>
                    </iais:action>
                </div></div></div></div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function doCreate(type,no){
        no = $('#roomNo').val();
        type = $('#roomType option:selected').val();
        SOP.Crud.cfxSubmit("mainForm","save",type,no);
    }
</script>



























