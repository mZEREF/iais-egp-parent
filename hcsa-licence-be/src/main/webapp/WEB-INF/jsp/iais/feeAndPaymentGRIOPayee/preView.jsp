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
                    <strong>Add a GIRO Payee</strong>
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
                                <iais:field value="HCI Code(s) :"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="HCI Name(s) :"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Account Name :" mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Bank Code :"  mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Branch Code :"  mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>

                            <iais:row>
                                <iais:field value="Bank Name :" mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Bank Account No. :" mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Customer Reference No. :" mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="GIRO Form :" mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Application No." mandatory="ture"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">

                                    <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                                </div>
                            </iais:row>
                        </iais:section>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button class="btn btn-primary" type="button"  onclick="javascript:doLicSearch()">Back</button>
                                <button class="btn btn-primary" type="button"  onclick="javascript:doLicSearch()">Submit</button>
                            </iais:action>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
