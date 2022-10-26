<%@ page import="sop.webflow.rt.api.BaseProcessClass" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%
    BaseProcessClass process = (BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-afc-selection.js"></script>
<script>
    <% String jsonStr = (String) request.getAttribute("lastTwoAfcJson");
       if (jsonStr == null || "".equals(jsonStr)) {
           jsonStr = "undefined";
       }
       Boolean haveSuitableDraftData = (Boolean) request.getAttribute("haveSuitableDraftData");
    %>
    var lastTwoAfcJson = <%=jsonStr%>;
    var haveSuitableDraftData = <%=haveSuitableDraftData%>
</script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="action_load_draft" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <div class="form-group" style="margin-top: 50px">
                            <div class="col-sm-5 control-label">
                                <label>Has the facility appointed an Approved Facility Certifier </label>
                                <span class="mandatory otherQualificationSpan">*</span>
                                <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>1. The facility shall inform MOH of the selected certifier at least 1 month in advance of the certification date. </p><br/><p>2.When selecting the certifier, the facility shall ensure that the company or member of the certifying team has not provided any design, construction, commissioning, or maintenance services to the facility in the 12 months preceding the certification date. </p><br/><p>3.The same certifier shall not be appointed for more than two consecutive years, unless approval/consent has been obtained by MOH. </p>">i</a>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                    <label for="hasAppointedCertifier">Yes</label>
                                    <input type="radio" name="appointed" id="hasAppointedCertifier" value="Y" <c:if test="${afc.appointed eq 'Y'}">checked="checked"</c:if> />
                                </div>
                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                    <label for="notAppointedCertifier">No</label>
                                    <input type="radio" name="appointed" id="notAppointedCertifier" value="N" <c:if test="${afc.appointed eq 'N'}">checked="checked"</c:if> />
                                </div>
                                <span data-err-ind="appointed" class="error-msg"></span>
                            </div>
                        </div>
                        <div id="appointedCertifierSection" <c:if test="${afc.appointed ne 'Y'}">style="display: none" </c:if>>
                            <div class="form-group">
                                <div class="col-sm-5 control-label">
                                    <label for="certifierSelection">Select Approved Facility Certifier </label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-6 col-md-7">
                                    <select name="afc"  class="afcDropDown" id="certifierSelection">
                                        <option value="">Please Select</option>
                                        <c:forEach var="item" items="${afcOps}">
                                            <option value="${item.value}" <c:if test="${afc.afc eq item.value}">selected="selected"</c:if>>${item.text}</option>
                                        </c:forEach>
                                    </select>
                                    <span data-err-ind="afc" class="error-msg"></span>
                                </div>
                            </div>

                            <div id="afcReasonSection" <c:if test="${!last2AfcSet.contains(afc.afc)}">style="display: none"</c:if>>
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="selectReason">Reasons for choosing this AFC </label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <textarea maxLength="250" class="col-xs-12" name="selectReason" id="selectReason" rows="5"><c:out value="${afc.selectReason}"/></textarea>
                                        <span data-err-ind="selectReason" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 ">
                                <c:choose>
                                    <c:when test="${confirmRfi ne null && 'Y' == confirmRfi}">
                                        <a class="back" id="previous" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="back" id="previous" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="button-group">
                                    <c:if test="${canSaveDraftJudge}">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                    </c:if>
                                    <a class="btn btn-primary submit" id="submit" >SUBMIT</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <input type="hidden" id="haveSuitableDraftData" name="haveSuitableDraftData" value="${haveSuitableDraftData}" readonly disabled/>
                    <c:if test="${haveSuitableDraftData}">
                        <div class="modal fade" role="dialog" aria-labelledby="myModalLabel" id="existDraftModal">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-md-12"><span style="font-size: 2rem">There is an existing draft for the chosen service; if you choose to continue with a new application, the draft application will be discarded.</span></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal" id="modal-continue" >CONTINUE</button>
                                        <button type="button" class="btn btn-primary" data-dismiss="modal" id="modal-resume" >RESUME FROM DRAFT</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                </div>
            </div>
        </div>
    </div>
</form>
