<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Licensee Details</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type" id="crud_action_type" value="refresh">
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <h3>Company Details</h3>
                                    <iais:row>
                                        <iais:field value="Who is the licensee" width="11"/>
                                        <iais:field value="${licensee.getLicenseeType()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="UEN No." width="11"/>
                                        <iais:field value="${organization.getUenNo()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Name of the licensee" width="11"/>
                                        <iais:field value="${licensee.getName()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Postal Code" width="11"/>
                                        <iais:field value="${licensee.getPostalCode()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Address Type" width="11"/>
                                        <iais:field value="${licensee.getAddrType()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Blk No." width="11"/>
                                        <iais:field value="${licensee.getBlkNo()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Floor No." width="11"/>
                                        <iais:field value="${licensee.getFloorNo()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Unit No." width="11"/>
                                        <iais:field value="${licensee.getUnitNo()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Street Name" width="11"/>
                                        <iais:field value="${licensee.getStreetName()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Building Name" width="11"/>
                                        <iais:field value="${licensee.getBuildingName()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office Telephone No." width="11"/>
                                        <iais:field value="${licensee.getOfficeTelNo()}" width="11"/>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Office Email Address" width="11"/>
                                        <iais:field value="${licensee.getEmilAddr()}" width="11"/>
                                    </iais:row>

                                    <c:forEach var="item" items="${person}" varStatus="status">
                                        <h3>Board Member ${status.index + 1}</h3>

                                        <iais:row>
                                            <iais:field value="Name" width="11"/>
                                            <iais:field value="${item.getName()}" width="11"/>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Salutation" width="11"/>
                                            <label class="col-xs-11 col-md-4 control-label">
                                                <iais:code  code="${item.getSalutation()}"/>
                                            </label>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="ID Type" width="11"/>
                                            <label class="col-xs-11 col-md-4 control-label">
                                                <iais:code  code="${item.getIdType()}"/>
                                            </label>
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
                                            <iais:field value="Designation Appointment Date" width="11"/>
                                            <label class="col-xs-11 col-md-4 control-label">
                                                    <fmt:formatDate value="${item.getApptDt()}" pattern="dd/MM/yyyy"/>
                                            </label>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Designation Cessation Date" width="11"/>
                                            <label class="col-xs-11 col-md-4 control-label">
                                                <fmt:formatDate value="${item.getCessationDt()}" pattern="dd/MM/yyyy"/>
                                            </label>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Designation Cessation" width="11"/>
                                            <iais:field value="${item.getCessationReason()}" width="11"/>
                                        </iais:row>
                                    </c:forEach>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-2 col-md-2">
                                                <a   style="padding-left: 90px;" align="left" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                                            </div>
                                            <div class="text-right col-xs-9 col-md-9">
                                                <button class="btn btn-primary" id="refresh" >Refresh Data</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $("#back").click(function () {
        SOP.Crud.cfxSubmit("mainForm","back");
    })

    $("#refresh").click(function () {
        $("#crud_action_type").val("refresh");
        SOP.Crud.cfxSubmit("mainForm","refresh");
    })
</script>