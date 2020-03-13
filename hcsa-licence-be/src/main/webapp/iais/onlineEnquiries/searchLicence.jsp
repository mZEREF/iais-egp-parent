<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria</span>
                    </h3>
                    <c:if test="${choose[2]==3||choose[1]==2}">
                        <h4>
                            <span>
                                Search By Application
                                <c:choose >
                                    <c:when test="${choose[2]==3||choose[1]==2}">
                                    <label>
                                        <input type="checkbox" checked name="application" />
                                    </label>
                                    </c:when>
                                    <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="application" />
                                    </label>
                                    </c:otherwise>
                                </c:choose>
                            </span>

                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Application No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Type"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${SearchParam.filters['appType']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Status"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Submitted Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="Application Submitted Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licence_no" value="${SearchParam.filters['licence_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="UEN No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Licence:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${serviceLicenceType}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Sub-Type:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please select" value="${SearchParam.filters['serviceSubTypeName']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Status:"/>
                                                <iais:value width="18">
                                                    <iais:select name="licence_status" options="licStatusOption" firstOption="Please select" value="${SearchParam.filters['licence_status']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Start Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="Licence Start Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Expiry Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>

                                                <iais:field value="Licence Expiry Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_date" name = "expiry_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[0]==1}">
                        <h4>
                        <span>Search By HCI
                            <c:choose >
                                <c:when test="${choose[0]==1}">
                                    <label>
                                        <input type="checkbox" checked name="hci" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="hci" />
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="HCI Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_code" value="${SearchParam.filters['hci_code']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_name" value="${SearchParam.filters['hci_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Street Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_street_name" value="${SearchParam.filters['hci_street_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Postal Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_postal_code" value="${SearchParam.filters['hci_postal_code']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>

                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[3]==4}">
                        <h4>
                        <span>Search By Licensee
                            <c:choose >
                                <c:when test="${choose[3]==4}">
                                    <label>
                                        <input type="checkbox" checked name="licensee" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="licensee" />
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Licensee ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_id" value="${SearchParam.filters['licensee_id']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_name" value="${SearchParam.filters['licensee_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_regn_no" value="${SearchParam.filters['licensee_regn_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[4]==5}">
                        <h4>
                        <span>Search By Service Personnel Details (CGO, PO, DPO & MedAlert)
                            <c:choose >
                                <c:when test="${choose[4]==5}">
                                    <label>
                                        <input type="checkbox" checked name="servicePersonnel" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="servicePersonnel" />
                                    </label>
                                </c:otherwise>
                            </c:choose>

                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Service Personnal ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_id" value="${SearchParam.filters['service_id']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_name" value="${SearchParam.filters['service_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_regn_no" value="${SearchParam.filters['service_regn_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Role:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_role" value="${SearchParam.filters['service_role']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <iais:action style="text-align:center;">
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicSearch()">Search</button>
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicBack()">Back</button>
                        <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicClear()">Clear</button>
                    </iais:action>
                </div>
            </div>
        </div>
    </iais:body>


</form>
<script type="text/javascript">
    function doLicSearch(){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function doLicBack(){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[type="text"]').val("");
        $("#service_licence_type option[text = 'Please select']").val("selected", "selected");
        $("#service_sub_type option[text = 'Please select']").val("selected", "selected");
        $("#licence_status option[text = 'Please select']").val("selected", "selected");
        $("#application_type option[text = 'Please select']").val("selected", "selected");
        $("#application_status option[text = 'Please select']").val("selected", "selected");
        $(".current").text("Please select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
        $('input[name="start_date"]').val("");
        $('input[name="start_to_date"]').val("");
        $('input[name="expiry_start_date"]').val("");
        $('input[name="expiry_date"]').val("");
    }
</script>