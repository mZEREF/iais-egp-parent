<c:forEach items="${iais_Login_User_Info_Attr.privileges}" var="privilege">
    <c:if test="${privilege.id == 'HALP_HCSA_INBOX'}">
        <c:set var="hcsaPrivilege" value="1"/>
    </c:if>
    <c:if test="${privilege.id == 'HALP_MOH_DS_LDT'}">
        <c:set var="dataSubLDTPrivilege" value="1"/>
    </c:if>
</c:forEach>

<div class="self-assessment-item completed">
    <div class="amendLicence">
        <div class="form-check-gp">
            <style>
                
                .form-check input.form-check-input:hover + .form-check-label a {
                    text-decoration: none;
                    color: #FFFFFF;
                }
                .form-check .form-check-label:hover a, .form-check .form-check-label:focus a, .form-check .form-check-label:active a {
                    text-decoration: none;
                    color: #FFFFFF;
                }
                .tooltip .tooltip-inner {
                    width: 300px;
                    max-width: 300px;
                }
            </style>
            <p class="form-check-title">What would you like to do today?</p>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <input class="form-check-input" style="position: relative;" id="applyLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="applyLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Apply for a new licence <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                          title='${inbox_ack016}'
                                                                          style="z-index: 10"
                                                                          data-original-title=""
                    >i</a></span>
                    <span class="right-content">${self_ack001}</span>
                </label>

            </div>
            </c:if>
            <%--
                2
            --%>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check"
                 style="width: 70%">
                <input class="form-check-input" style="position: relative;" id="renewLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="renewLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Renew my licence <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                   title='${self_ack014}'
                                                                   style="z-index: 10"
                                                                   data-original-title=""
                    >i</a></span>
                    <span class="right-content">${self_ack002}</span>
                </label>
            </div>
            </c:if>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check"
                 style="width: 70%">
                <input class="form-check-input" style="position: relative;"
                       id="resumeDraftApplication" type="radio"
                       name="selfAssessmentType" aria-invalid="false">
                <label class="form-check-label"
                       for="resumeDraftApplication">
                    <span class="check-circle"></span>
                    <span class="left-content">Resume my draft application</span>
                    <span class="right-content">${self_ack003}</span>
                </label>
            </div>
            </c:if>
            <%--
                3
            --%>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <div style="padding-top: 0">
                    <input class="form-check-input" style="position: relative;" id="amendLicence"
                           type="radio" name="selfAssessmentType"
                           aria-invalid="false">
                    <label class="form-check-label" for="amendLicence">
                        <span class="check-circle"></span>
                        <span class="left-content">Amend my licence <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                       title='${inbox_ack017}'
                                                                       style="z-index: 10"
                                                                       data-original-title=""
                        >i</a></span>
                        <span class="right-content">${self_ack004}</span>
                    </label>
                </div>

<%--                <div class="visible-lg">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack017}'--%>
<%--                       style="position: absolute;left: 200px; top: 60px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-md">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack017}'--%>
<%--                       style="position: absolute;left: 200px; top: 105px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-sm">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack017}'--%>
<%--                       style="position: absolute;left: 200px; top: 110px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-xs">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack017}'--%>
<%--                       style="position: absolute;left: 175px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
            </div>
            </c:if>
            <%--
                8
            --%>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <input class="form-check-input" style="position: relative;"
                       id="updateAdminPersonnel" type="radio"
                       name="selfAssessmentType" aria-invalid="false">
                <label class="form-check-label"
                       for="updateAdminPersonnel">
                    <span class="check-circle"></span>
                    <span class="left-content">Manage admin personnel <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                         title='${inbox_ack023}'
                                                                         style="z-index: 10"
                                                                         data-original-title=""
                    >i</a></span>
                    <span class="right-content">${self_ack005}</span>
                </label>
<%--                <div class="visible-lg">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack023}'--%>
<%--                       style="position: absolute;left: 258px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-md">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack023}'--%>
<%--                       style="position: absolute;left: 255px; top: 20px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-sm">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack023}'--%>
<%--                       style="position: absolute;left: 170px; top: 12px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-xs">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack023}'--%>
<%--                       style="position: absolute;left: 225px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
            </div>
            </c:if>
            <c:if test="${dataSubLDTPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <input class="form-check-input" style="position: relative;" id="submitDataMoh"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="submitDataMoh">
                    <span class="check-circle"></span>
                    <span class="left-content">Data Submission</span>
                    <span class="right-content">${self_ack006}</span>
                </label>
            </div>
            </c:if>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <input class="form-check-input" style="position: relative;" id="withdrawApplication"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label"
                       for="withdrawApplication">
                    <span class="check-circle"></span>
                    <span class="left-content">Withdraw my application <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                          title='${inbox_ack019}'
                                                                          style="z-index: 10"
                                                                          data-original-title=""
                    >i</a></span>
                    <span class="right-content">${self_ack007}</span>
                </label>
<%--                <div class="visible-lg">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack019}'--%>
<%--                       style="position: absolute;left: 258px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-md">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack019}'--%>
<%--                       style="position: absolute;left: 253px; top: 10px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-sm">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack019}'--%>
<%--                       style="position: absolute;left: 160px; top: 10px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-xs">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack019}'--%>
<%--                       style="position: absolute;left: 225px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
            </div>
            </c:if>
            <c:if test="${hcsaPrivilege == 1}">
            <div class="form-check progress-step-check" style="width: 70%;margin-bottom: 0;">
                <input class="form-check-input" style="position: relative;" id="ceaseLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="ceaseLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Cease my licence <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                   title='${inbox_ack018}'
                                                                   style="z-index: 10"
                                                                   data-original-title=""
                    >i</a></span>
                    <span class="right-content">${self_ack008}</span>
                </label>
<%--                <div class="visible-lg">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack018}'--%>
<%--                       style="position: absolute;left: 190px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-md">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack018}'--%>
<%--                       style="position: absolute;left: 188px; top: 20px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-sm">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack018}'--%>
<%--                       style="position: absolute;left: 188px; top: 20px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
<%--                <div class="visible-xs">--%>
<%--                    <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"--%>
<%--                       title='${inbox_ack018}'--%>
<%--                       style="position: absolute;left: 165px; top: 15px;z-index: 10"--%>
<%--                       data-original-title=""--%>
<%--                    >i</a></div>--%>
            </div>
            </c:if>
        </div>
    </div>
</div>