<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<style>
    strong{
        font-size: 24px;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type" value="">
    <div class="instruction-content center-content">
        <div class="h3-with-desc">
            <strong>Licensee</strong>
            <p>The Licensee is accountable for the healthcare organisation's overall compliance with the HCS Bill and the appointment of key leadership roles.</p>

            <strong>Greenwood Clinic</strong>
            <p>Your company information is retrieved from ACRA.</p>

            <div class="form-horizontal">
                <iais:row>
                    <iais:field value="UEN (ACRA / ROS) No." width="11"/>
                    <iais:field value="${licensee.uenNo}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Name of the Company" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code  code="${licensee.name}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Postal Code" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${licensee.postalCode}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Block / House No." width="11"/>
                    <iais:field value="${licensee.blkNo}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Street Name" width="11"/>
                    <iais:field value="${licensee.streetName}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Floor No." width="11"/>
                    <iais:field value="${licensee.floorNo}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Unit No." width="11"/>
                    <iais:field value="${licensee.unitNo}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Building Name" width="11"/>
                    <iais:field value="${licensee.buildingName}" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Address Type" width="11"/>
                    <iais:field value="${licensee.addrType}" width="11"/>
                </iais:row>
            </div>
        </div>
        <c:forEach var="item" items="${person}" varStatus="status">
        <div class="h3-with-desc">
            <div class="form-horizontal">
                    <strong>Board Member ${status.index + 1}</strong>
                    <iais:row>
                        <iais:field value="Name" width="11"/>
                        <iais:field value="${item.getName()}" width="11"/>
                    </iais:row>
                    <iais:row>
                        <iais:field value="ID No." width="11"/>
                        <iais:field value="${item.getIdNo()}" width="11"/>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Designation" width="11"/>
                        <iais:field value="${item.getDesignation()}" width="11"/>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Designation Cessation Date" width="11"/>
                        <label class="col-xs-11 col-md-4 control-label">
                            <fmt:formatDate value="${item.getCessationDt()}" pattern="dd/MM/yyyy"/>
                        </label>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Designation Cessation Reason" width="11"/>
                        <iais:field value="${item.getCessationReason()}" width="11"/>
                    </iais:row>
            </div>
        </div>

        </c:forEach>

        <div class="row">
            <div class="col-xs-2 col-md-2">
                <a align="left" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $("#back").click(function () {
        SOP.Crud.cfxSubmit("mainForm","backToMenu");
    })
</script>