<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inventory.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <br><br><br>
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" role="presentation"><a href="#tabTransactionHistoryInfo"
                                                                                      aria-controls="tabTransactionHistoryInfo"
                                                                                      role="tab" data-toggle="tab">Transaction
                                                History</a></li>
                                            <li class="complete" role="presentation"><a href="#transactionAdjustment"
                                                                                        aria-controls="transactionAdiustment"
                                                                                        role="tab"
                                                                                        data-toggle="tab">Adjustment</a>
                                            </li>

                                        </ul>
                                        <div class="tab-content ">
                                            <div class="tab-pane active" id="tabTransactionHistoryInfo" role="tabpanel">
                                                <div class="panel panel-default">
                                                    <!-- Default panel contents -->
                                                    <div class="panel-heading"><strong>Transaction Details</strong>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-xs-12" style="height: 100%">
                                                            <div class="table-gp">
                                                                <table aria-describedby="" class="table table-bordered">
                                                                    <tbody>
                                                                    <tr>
                                                                        <th scope="col" style="display: none"></th>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-6">S/N</td>
                                                                        <td class="col-xs-6" style="padding-left: 15px;">1</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Biological Agent/Toxin</td>
                                                                        <td style="padding-left: 15px;">${historyDto.bat}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Type of Transfer</td>
                                                                        <td style="padding-left: 15px;">${historyDto.transferType}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Quantity Transferred</td>
                                                                        <td style="padding-left: 15px;">${historyDto.transferredQTY}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Sending Facility</td>
                                                                        <td style="padding-left: 15px;">${historyDto.sendFacility}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Receiving Facility</td>
                                                                        <td style="padding-left: 15px;">${historyDto.recFacility}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Date of Transfer</td>
                                                                        <td style="padding-left: 15px;">${historyDto.transferDate}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Expected Arrival time at receiving facility</td>
                                                                        <td style="padding-left: 15px;">${historyDto.exceptedArrivalDate}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td >Name of Courier of Service Provider</td>
                                                                        <td style="padding-left: 15px;">${historyDto.providerName}</td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="tab-pane" id="transactionAdjustment" role="tabpanel">
                                                <div class="panel panel-default">
                                                    <!-- Default panel contents -->
                                                    <div class="panel-heading"><strong>Transaction Adjustment</strong>
                                                    </div>
                                                    <div class="row" style="margin: 10px;">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">

                                                                <iais:section title="">
                                                                    <iais:row>
                                                                        <iais:field value="Adjustment Type"
                                                                                    required="true"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <iais:select name="adjustment_type" options="adjustmentType"
                                                                                         firstOption="Please Select"></iais:select>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row style="text-align:center">
                                                                        <span id="errMsg01" style="color: red"></span>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Type of Transfer"
                                                                                    required="true"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <iais:select name="type_of_transfer" options="typeOfTransfer"
                                                                                         firstOption="Please Select"></iais:select>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row style="text-align:center">
                                                                        <span id="errMsg02" style="color: red"></span>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Biological Agent/Toxin"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <input type="text"
                                                                                   style="width:180%; font-weight:normal;"
                                                                                   name="biological_agent"
                                                                                   maxlength="100"/>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Facility Name"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <input type="text"
                                                                                   style="width:180%; font-weight:normal;"
                                                                                   name="facility_name"
                                                                                   maxlength="100"/>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row><iais:field value="Initial Quantity"
                                                                                          required="true"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <input id="iq" type="text"
                                                                                   style="width:180%; font-weight:normal;"
                                                                                   name="initial_quantity"
                                                                                   maxlength="100"/>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row style="text-align:center">
                                                                        <span id="errMsg03" style="color: red"></span>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Quantity to Change"
                                                                                    required="true"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <input id="qtc" type="number"
                                                                                   style="width:180%; font-weight:normal;"
                                                                                   name="quantity_to_change"
                                                                                   maxlength="100"/>
                                                                        </div>
                                                                    </iais:row>
                                                                    <iais:row style="text-align:center">
                                                                        <span id="errMsg04" style="color: red"></span>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Remarks"/>
                                                                        <div class="col-sm-7 col-md-4 col-xs-10">
                                                                            <textarea cols="32" rows="5"
                                                                                      maxlength="2000"></textarea>
                                                                        </div>
                                                                    </iais:row>
                                                                </iais:section>
                                                            </div>
                                                        </div>
                                                        <div class="col-xs-12 col-md-12">
                                                            <iais:action style="text-align:right;">
                                                                <button class="btn btn-secondary" type="button"
                                                                        onclick="javascript:doClear()">Clear
                                                                </button>
                                                                <button class="btn btn-primary" type="button"
                                                                        onclick="javascript:doAdjust()">
                                                                    Submit
                                                                </button>
                                                            </iais:action>
                                                        </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-xs-12" style="height: 100%">
                                                                <div class="components" style="margin-top: 50px">
                                                                    <h3>
                                                                        <span>Adjustment History</span>
                                                                    </h3>
                                                                    <iais:pagination param="SearchParam"
                                                                                     result="SearchResult"/>
                                                                    <div class="table-responsive">
                                                                    <div class="table-gp">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr>
                                                                                <th scope="col" style="display: none"></th>
                                                                            </tr>
                                                                            <tr align="center">
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="S/N"
                                                                                                     value="S/N"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Facility Name"
                                                                                                     value="Facility Name"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Type of Transfer (Before)"
                                                                                                     value="Type of Transfer (Before)"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Type of Transfer (After)"
                                                                                                     value="Type of Transfer (After)"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Adjustment Type"
                                                                                                     value="Adjustment Type"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Adjustment Date time"
                                                                                                     value="Adjustment Date time"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Adjusted by"
                                                                                                     value="Adjusted by"/>
                                                                                <iais:sortableHeader needSort="false"
                                                                                                     field="Quantity (Before)"
                                                                                                     value="Quantity (After)"/>
                                                                            </tr>
                                                                            </thead>
                                                                            <tbody class="form-horizontal">
                                                                            <td colspan="12">
                                                                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                                                <!--No Record!!-->
                                                                            </td>
                                                                            </tbody>
                                                                        </table>
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
                                    <a onclick="doBack()"><em
                                            class="fa fa-angle-left"> </em> Back</a>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>