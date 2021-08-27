<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="dynamicContent" role="presentation">
                                                <a href="#tabDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabScreening" aria-controls="tabScreening" role="tab" data-toggle="tab">Screening</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDynamicContent" id="doDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Screening</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/common/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/common/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDynamicContent" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/process/common/dynamicContent.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabScreening" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <iais:section title="">
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <iais:value width="10"><p>Normal risk</p></iais:value>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <iais:value width="10"><p>xxx</p></iais:value>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Processing Decision" required="false"/>
                                                                            <iais:value width="6">
                                                                                <iais:value width="10"><p>Screened by DO</p></iais:value>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="AO Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="aoRemarks" cols="70" rows="7" maxlength="500"></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Reviewing Decision" required="true"/>
                                                                            <iais:value width="6">
                                                                                <select>
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Approve for Inspection</option>
                                                                                    <option value="2">Reject</option>
                                                                                    <option value="3">Route Back to Duty Officer</option>
                                                                                    <option value="4">Route to Higher Management</option>
                                                                                </select>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="ERP Report" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="eRPReport" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Red Teaming Report" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="redTeamingReport" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Lentivirus Report" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="lentivirusReport" value=""></iais:datePicker>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Internal Inspection Report" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="internalInspectionReport" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Selected Approved Facility Certifier" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <select>
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Approve</option>
                                                                                    <option value="2">Reject</option>
                                                                                </select>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="validityStartDate" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="true"></iais:field>
                                                                            <iais:value width="6">
                                                                                <iais:datePicker name="validityEndDate" value=""></iais:datePicker>
                                                                                <span class="error-msg" name="errorMsg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Final Remarks" required="false"></iais:field>
                                                                            <iais:value width="6">
                                                                                <select>
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Yes</option>
                                                                                    <option value="2">No</option>
                                                                                </select>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div align="right">
                                                                    <button name="clearBtn" id="clearButton" type="button" class="btn btn-secondary">Clear</button>
                                                                    <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/process/common/processingHistory.jsp"%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/iais/process/common/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>