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
    <div class="container center-content">
        <br>
        <div class="row">
            <%@ include file="../common/dashboardDropDown.jsp" %>
        </div>
        <div class="h3-with-desc">
            <strong>Authorised Person</strong>
            <p style="margin-top: 30px;margin-bottom: 20px">
                An Authorised Person is an admin personnel appointed by the licensee who is responsible for transacting with MOH
                (e.g. communicating and/or submitting information) on behalf of the licensee.
                This includes but is not limited to the renewal/ cessation/ surrendering of licences
                or updating of information relating to the healthcare institution.
            </p>

            <strong>${feuser.displayName}, ${feuser.identityNo} (<iais:code code="${feuser.idType}"/>)</strong>

            <div class="form-horizontal" style="margin-top: 20px">
                <iais:row>
                    <iais:field value="Name" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.salutation}"/> <iais:code code="${feuser.displayName}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="ID Type" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.idType}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="ID No." width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.identityNo}"/> (<iais:code code="${feuser.idType}"/>)
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Designation" width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.designation}"/>
                    </label>
                </iais:row>

                <iais:row>
                    <iais:field value="Mobile No." width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.mobileNo}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Office Telephone No." width="11"/>
                    <label class="col-xs-11 col-md-4 control-label">
                        <iais:code code="${feuser.officeTelNo}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <iais:field value="Email Address" width="11"/>
                    <iais:field value="${feuser.email}" width="11"/>
                </iais:row>
            </div>

        </div>
        <c:if test="${'common'.equals(flag)}">
            <div class="row">
                <div class="col-xs-2 col-md-2">
                    <a align="left" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </c:if>
    </div>
</form>
<script type="text/javascript">
    $("#back").click(function () {
        $("[name='crud_action_type']").val('backToMenu');
        $('#mainForm').submit();
    })

</script>