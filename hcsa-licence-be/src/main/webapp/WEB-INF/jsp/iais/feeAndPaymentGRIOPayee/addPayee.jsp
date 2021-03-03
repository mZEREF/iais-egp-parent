<%--
  Created by IntelliJ IDEA.
  User: mjy
  Date: 2021/3/2
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="center-content">
            <div class="intranet-content">
                <div class="bg-title col-xs-12 col-md-12">
                    <h2>Add a GIRO Payee</h2>
                </div>
                <div class="row">Note: This function is to add a GIRO Payee who submitted a manual application.</div>
                <div class="row">&nbsp;</div>
                <div class="row">The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.</div>
                <div class="row">&nbsp;</div>
                <div class="row">&nbsp;</div>
                <div class="row">Enter GIRO Payee Details</div>
                <div class="panel-body">
                    <div class="panel-main-content">
                        <iais:section title="" id = "supPoolList">
                            <iais:row>
                                <iais:field value="Application No."/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Application Type"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div id="application_type">
                                        <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${SearchParam.filters['appType']}" ></iais:select>
                                    </div>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Application Status"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div id="application_status">
                                        <iais:select name="application_status" options="appStatusOption" firstOption="Please Select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                                    </div>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Application Submitted Date From"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                    <span style="font-weight:normal;" id="error_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Application Submitted Date To"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                </div >
                            </iais:row>

                            <iais:row>
                                <iais:field value="Licence No."/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="24" style=" font-weight:normal;" name="licence_no" value="${SearchParam.filters['licence_no']}" />

                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="UEN No."/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="10" style=" font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />

                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Service Type"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div id="service_licence_type">
                                        <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please Select" value="${SearchParam.filters['svc_name']}" ></iais:select>
                                    </div>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Service Sub-Type"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div id="service_sub_type">
                                        <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please Select" value="${SearchParam.filters['serviceSubTypeName']}" ></iais:select>
                                    </div>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence Status"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div id="licence_status">
                                        <iais:select name="licence_status" options="licStatusOption" firstOption="Please Select" value="${SearchParam.filters['licence_status']}" ></iais:select>
                                    </div>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence Start Date From"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                    <span style="font-weight:normal;" id="error_start_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence Start Date To"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['start_to_date']}"></iais:datePicker>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence Expiry Date From"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['expiry_start_date']}" ></iais:datePicker>
                                    <span style="font-weight:normal;" id="error_expiry_date" name="iaisErrorMsg" class="error-msg" ></span>
                                </div >
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence Expiry Date To"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <iais:datePicker id = "expiry_date" name = "expiry_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                </div >
                            </iais:row>
                        </iais:section>
                    </div>
                </div>

            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
