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
            <strong>Authorised User</strong>
            <p>The Authorised Person is responsible for liaising with MOH on all licensing matters and for the administration and management of the healthcare organisation. </p>

            <strong>${feuser.displayName},${nric}</strong>

            <div class="form-horizontal">
                <iais:row>
                    <iais:field value="Name of the Company" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code  code="${feuser.displayName}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="UEN (ACRA / ROS) No." width="11"/>
                    <iais:field value="${feuser.identityNo}" width="11"/>
                </iais:row>

                <iais:row>
                    <iais:field value="Designation" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.designation}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Professional Regn Type" width="11"/>
                    <iais:field value="-" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Professional Regn No." width="11"/>
                    <iais:field value="-" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Mobile No." width="11"/>
                    <iais:field value="-" width="11"/>
                </iais:row>
                <iais:row>
                    <iais:field value="Email Address" width="11"/>
                    <iais:field value="${feuser.email}" width="11"/>
                </iais:row>
            </div>

        </div>

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