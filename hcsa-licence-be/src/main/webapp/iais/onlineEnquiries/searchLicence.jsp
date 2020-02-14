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
                    <br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria</span>
                    </h3>
                    <c:if test="${choose[2]==3||choose[1]==2}">
                        <h4>
                            <span>
                                Search By Application
                                <label>
                                    <input type="checkbox"  checked name="application" />
                                </label>
                            </span>

                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Application No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="application_no" value="${SearchParam.filters['appNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Type"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${appTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Status"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${appStatusOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Submitted Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="To:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="licence_no" value="${SearchParam.filters['licence_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="UEN No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="uen_no" value="${SearchParam.filters['uen_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Licence:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Sub-Type:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Status:"/>
                                                <iais:value width="18">
                                                    <iais:select name="licence_status" options="licStatusOption" firstOption="Please select" value="${licStatusOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Start Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="To:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Expiry Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>

                                                <iais:field value="To:"/>
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
                        <label>
                            <input type="checkbox"  checked checkedname="hci" />
                        </label></span>
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
                                                    <input type="text" name="hci_code" value="${SearchParam.filters['hci_code']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Name:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="hci_name" value="${SearchParam.filters['hci_name']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Street Name:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="hci_street_name" value="${SearchParam.filters['hci_street_name']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Postal Code:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="hci_postal_code" value="${SearchParam.filters['hci_postal_code']}" />
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
                        <label>
                            <input type="checkbox"  checked checkedname="licensee" />
                        </label></span>
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
                                                    <input type="text" name="licensee_id" value="${SearchParam.filters['licensee_id']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licensee Name:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="licensee_name" value="${SearchParam.filters['licensee_name']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licensee Professional Regn No:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="licensee_regn_no" value="${SearchParam.filters['licensee_regn_no']}" />
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
                        <label>
                            <input type="checkbox"  checked checkedname="servicePersonnel" />
                        </label></span>
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
                                                    <input type="text" name="service_id" value="${SearchParam.filters['service_id']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Personnel Name:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="service_name" value="${SearchParam.filters['service_name']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Professional Regn No:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="service_regn_no" value="${SearchParam.filters['service_regn_no']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Professional Role:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="service_role" value="${SearchParam.filters['service_role']}" />
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
        SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function doLicBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[name="licence_no"]').val("");
        $("#licence_type option:first").val("");
        $("#licence_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").val("");
        $("#licence_status option:first").prop("selected", 'selected');
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }
</script>