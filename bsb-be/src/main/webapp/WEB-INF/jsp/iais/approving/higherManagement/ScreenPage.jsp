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
                                                    <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Screening</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/approving/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/approving/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabScreening" role="tabpanel">
                                                <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
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
                                                                    <div id="RiskLeveloftheBiologicalAgentOrToxin">
                                                                        <iais:row>
                                                                            <iais:field value="Risk Level of the Biological Agent/Toxin" required="false"></iais:field>
                                                                            <iais:value width="10"><p>Normal risk</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="CommentsOnRiskLevelAssessment">
                                                                        <iais:row>
                                                                            <iais:field value="Comments on Risk Level Assessment" required="false" width="12"/>
                                                                            <iais:value width="10"><p>Risk Level 1</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="DORecommendation">
                                                                        <iais:row>
                                                                            <iais:field value="DO Recommendation" required="false" width="12"/>
                                                                            <iais:value width="10"><p>Some Recommendation</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="AOReviewDecision">
                                                                        <iais:row>
                                                                            <iais:field value="AO Review Decision" required="false"/>
                                                                            <iais:value width="10"><p>Screened by DO</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="AORemarks">
                                                                        <iais:row>
                                                                            <iais:field value="AO Remarks" required="false" width="12"/>
                                                                            <iais:value width="10"><p>xxx</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="HMRemarks">
                                                                        <iais:row>
                                                                            <iais:field value="HM Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="hmRemarksId" name="hmRemarks" cols="70" rows="7" maxlength="500"></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ProcessingDecision">
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="6">
                                                                                <select id="ProcessingDecisionSelectId">
                                                                                    <option value="-1">Please Select</option>
                                                                                    <option value="1">Approve</option>
                                                                                    <option value="2">Reject</option>
                                                                                </select>
                                                                                <span class="error-msg" name="errorMsg" id="error_ProcessingDecision"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ValidityStartDate">
                                                                        <iais:row>
                                                                            <iais:field value="Validity Start Date" required="false"></iais:field>
                                                                            <iais:value width="10"><p>23/07/2021</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="ValidityEndDate">
                                                                        <iais:row>
                                                                            <iais:field value="Validity End Date" required="false"></iais:field>
                                                                            <iais:value width="10"><p>23/07/2021</p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="/system-admin-web/eservice/INTRANET/MohApprovingScreen?"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div align="right">
                                                                    <button name="clearBtn" id="clearButton" type="button" class="btn btn-secondary">Clear</button>
                                                                    <button name="finalAssessmentBtn" id="finalAssessmentButton" type="button" class="btn btn-primary">Final Assessment</button>
                                                                    <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/approving/screenHistory.jsp"%>
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
    <iais:confirm msg="GENERAL_ACK018" needCancel="false" callBack="tagConfirmCallbacksupportReport()" popupOrder="supportReport"></iais:confirm>
</div>
<%@include file="/WEB-INF/jsp/iais/approving/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $("#submitButton").click(function (){
        var index = $("#ProcessingDecisionSelectId option:selected").val();
        if(index == -1){
            $("#error_ProcessingDecision").text("This field is mandatory");
        }else if(index == 1){
            SOP.Crud.cfxSubmit("mainForm", "Approve");
        }else if(index == 2){
            SOP.Crud.cfxSubmit("mainForm", "Reject");
        }
    });

</script>