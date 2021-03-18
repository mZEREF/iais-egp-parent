<div class="self-assessment-item completed">
    <div class="amendLicence">
        <div class="form-check-gp">
            <p class="form-check-title">What would you like to do today?</p>
            <div class="form-check progress-step-check" style="width: 930px">
                <input class="form-check-input" id="applyLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="applyLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Apply for a new licence </span>
                    <span class="right-content">${self_ack001}</span>
                </label>
            </div>
            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
               title='${inbox_ack016}'
               style="position: absolute;left: 340px; top: 55px;z-index: 10"
               data-original-title=""
               >i</a>
            <%--
                2
            --%>
            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input" id="renewLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="renewLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Renew my licence</span>
                    <span class="right-content">${self_ack002}</span>
                </label>
            </div>

            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input"
                       id="resumeDraftApplication" type="radio"
                       name="selfAssessmentType" aria-invalid="false">
                <label class="form-check-label"
                       for="resumeDraftApplication">
                    <span class="check-circle"></span>
                    <span class="left-content">Resume my draft application</span>
                    <span class="right-content">${self_ack003}</span>
                </label>
            </div>
            <%--
                3
            --%>
            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input" id="amendLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="amendLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Amend my licence
                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);"
                            data-toggle="tooltip" data-html="true"
                            title='${inbox_ack017}' data-original-title=''
                            style="position: absolute;left: 200px; top: 66px;z-index: 10">i</a></span>
                    <span class="right-content">${self_ack004}</span>
                </label>
            </div>

            <%--
                8
            --%>
            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input"
                       id="updateAdminPersonnel" type="radio"
                       name="selfAssessmentType" aria-invalid="false">
                <label class="form-check-label"
                       for="updateAdminPersonnel">
                    <span class="check-circle"></span>
                    <span class="left-content">Manage admin personnel</span>
                    <span class="right-content">${self_ack005}</span>
                </label>
            </div>

            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input" id="submitDataMoh"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="submitDataMoh">
                    <span class="check-circle"></span>
                    <span class="left-content">Data Submission</span>
                    <span class="right-content">${self_ack006}</span>
                </label>
            </div>

            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input" id="withdrawApplication"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label"
                       for="withdrawApplication">
                    <span class="check-circle"></span>
                    <span class="left-content">Withdraw my application<a href="javascript:void(0);"
                                                                         class="btn-tooltip styleguide-tooltip"
                                                                         data-toggle="tooltip" data-html="true"
                                                                         title='${inbox_ack019}' data-original-title=''
                                                                         style="position: absolute;left: 242px; top: 12px;z-index: 10">i</a></span>
                    <span class="right-content">${self_ack007}</span>
                </label>
            </div>

            <div class="form-check progress-step-check"
                 style="width: 930px">
                <input class="form-check-input" id="ceaseLicence"
                       type="radio" name="selfAssessmentType"
                       aria-invalid="false">
                <label class="form-check-label" for="ceaseLicence">
                    <span class="check-circle"></span>
                    <span class="left-content">Cease my licence<a href="javascript:void(0);"
                                                                  class="btn-tooltip styleguide-tooltip"
                                                                  data-toggle="tooltip" data-html="true"
                                                                  title='${inbox_ack018}' data-original-title=''
                                                                  style="position: absolute;left: 189px; top: 12px;z-index: 10">i</a></span>
                    <span class="right-content">${self_ack008}</span>
                </label>
            </div>
        </div>
    </div>
</div>