<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp" %>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="main-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="prelogin-content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="center-content">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-12">
                                            <div class="self-assessment-gp">
                                                <div class="self-assessment-item completed">
                                                    <div class="amendLicence">
                                                        <div class="form-check-gp">
                                                            <p class="form-check-title">What would you like to do today?</p>
                                                            <div class="form-check progress-step-check" style="width: 900px">
                                                                <input class="form-check-input" id="applyLicence"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label" for="applyLicence">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Apply for a new licence<a
                                                                            class="btn-tooltip styleguide-tooltip"
                                                                            data-toggle="tooltip" data-html="true"
                                                                            title="" data-original-title=""
                                                                            aria-describedby="tooltip883061">i</a></span>
                                                                    <span>| For new services or premises</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input" id="renewLicence"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label" for="renewLicence">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Renew my licence</span>
                                                                    <span>| For expiring licences</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input" id="amendLicence"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label" for="amendLicence">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Amend my licence<a
                                                                            class="btn-tooltip styleguide-tooltip"
                                                                            data-toggle="tooltip" data-html="true"
                                                                            title="" data-original-title=""
                                                                            aria-describedby="tooltip883061">i</a></span>
                                                                    <span>| For changes in address, personnel, or subsumed services</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input" id="ceaseLicence"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label" for="ceaseLicence">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Cease my licence<a
                                                                            class="btn-tooltip styleguide-tooltip"
                                                                            data-toggle="tooltip" data-html="true"
                                                                            title="" data-original-title=""
                                                                            aria-describedby="tooltip883061">i</a></span>
                                                                    <span>| For service cessation and premises removal</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input" id="withdrawApplication"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label"
                                                                       for="withdrawApplication">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Withdraw my application</span>
                                                                    <span>| For withdrawing my application</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input"
                                                                       id="resumeDraftApplication" type="radio"
                                                                       name="selfAssessmentType" aria-invalid="false">
                                                                <label class="form-check-label"
                                                                       for="resumeDraftApplication">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Resume my draft application</span>
                                                                    <span>| For resuming a draft application</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input" id="submitDataMoh"
                                                                       type="radio" name="selfAssessmentType"
                                                                       aria-invalid="false">
                                                                <label class="form-check-label" for="submitDataMoh">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Submit data to MOH</span>
                                                                    <span>| For submission of data to MOH (e.g. Lab Development Tests)</span>
                                                                </label>
                                                            </div>
                                                            <div class="form-check progress-step-check"
                                                                 style="width: 900px">
                                                                <input class="form-check-input"
                                                                       id="updateAdminPersonnel" type="radio"
                                                                       name="selfAssessmentType" aria-invalid="false">
                                                                <label class="form-check-label"
                                                                       for="updateAdminPersonnel">
                                                                    <span class="check-circle"></span>
                                                                    <span class="left-content">Update admin personnel</span>
                                                                    <span>| For licensee/authorised persons to add/remove admin personnel</span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="self-assessment-item">
                                                    <div class="applyLicence hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/applyLicence.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="renewLicence hidden">
                                                        <div class="form-check-gp">
                                                            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/renewLicence.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="ceaseLicence hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/ceaseLicence.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="withdrawApplication hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/withdrawApplication.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="resumeDraftApplication hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/resumeDraftApplication.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="submitDataMoh hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/submitDataMoh.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="updateAdminPersonnel hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/updateAdminPersonnel.jsp" %>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="self-assessment-item">
                                                    <div class="renewLicence1 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/renewLicence1.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="renewLicence2 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include file="/WEB-INF/jsp/iais/selfassessment/schematics/renewLicence2.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence1 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence1.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence2 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence2.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence3 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence3.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence4 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence4.jsp" %>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="self-assessment-item">
                                                    <div class="amendLicence1_1 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence1_1.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence1_2 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence1_2.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence3_1 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence3_1.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence3_2 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence3_2.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence4_1 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence4_1.jsp" %>
                                                        </div>
                                                    </div>
                                                    <div class="amendLicence4_2 hidden">
                                                        <div class="form-check-gp">
                                                            <%@include
                                                                    file="/WEB-INF/jsp/iais/selfassessment/schematics/amendLicence4_2.jsp" %>
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
            </div>
        </div>

    </form>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script>
    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "page");
    }

    function sortRecords(sortFieldName, sortType) {
        SOP.Crud.cfxSubmit("mainForm", "sort", sortFieldName, sortType);
    }

</script>

