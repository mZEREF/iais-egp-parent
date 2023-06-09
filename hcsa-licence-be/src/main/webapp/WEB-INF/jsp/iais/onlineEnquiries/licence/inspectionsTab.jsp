<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal normal-label">
                    <div class="col-xs-12 col-md-12">
                        <iais:row style="margin-bottom: 0">
                            <iais:field width="4" value="Inspection Search" style="font-weight:bold"/>
                        </iais:row>
                        <hr style="margin-top: 0px">
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Application No.</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="20" id="applicationNo" name="insApplicationNo"
                                       value="<c:out value="${insTabEnquiryFilterDto.applicationNo}"/>">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="100" id="businessName" name="insBusinessName"
                                       value="<c:out value="${insTabEnquiryFilterDto.businessName}"/>">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Vehicle No.</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="10" id="vehicleNo" name="insVehicleNo"
                                       value="<c:out value="${insTabEnquiryFilterDto.vehicleNo}"/>">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Application Status</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="insAppStatus" options="appStatusOption" firstOption="All"
                                             cssClass="clearSel"  value="${insTabEnquiryFilterDto.appStatus}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Inspection Date</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="inspectionDateFrom" name="inspectionDateFrom"
                                                 dateVal="${insTabEnquiryFilterDto.inspectionDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label" style="text-align: center !important;">To&nbsp;</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="inspectionDateTo" name="inspectionDateTo"
                                                 dateVal="${insTabEnquiryFilterDto.inspectionDateTo}"/>
                            </iais:value>

                            <div class="col-md-8 col-xs-8 col-xs-offset-3 col-md-offset-3">
                                <span class="error-msg " name="iaisErrorMsg" id="error_inspectionDate"></span>
                            </div>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Inspection Type</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="inspectionType" options="inspectionTypeOption"
                                             firstOption="All" cssClass="clearSel"
                                             value="${insTabEnquiryFilterDto.inspectionType}"/>
                            </iais:value>
                        </iais:row>

                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doInsClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:searchInspection();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                <div class="components">

                    <iais:pagination param="insTabParam" result="insTabResult"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-responsive">
                            <thead>
                            <tr>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field=""
                                                     value="View Inspection Report"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APPLICATION_NO"
                                                     value="Application No."/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APP_TYPE_STR"
                                                     value="Application Type"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APP_STATUS_STR"
                                                     value="Application Status"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="BUSINESS_NAME"
                                                     value="Business Name"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="VEHICLE_NUM"
                                                     value="Vehicle No."/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="COMPLIANCE"
                                                     value="Compliance Tagging"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="NC_NUM"
                                                     value="No. of NCs"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LAST_RISK_LEVEL_STR"
                                                     value="Risk Tagging"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="INSPECTION_TYPE"
                                                     value="Inspection Type"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="TCU_DATE"
                                                     value="Reason for Inspection"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LAST_INSP_START_DATE"
                                                     value="Inspection Date"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="INSP_NAME"
                                                     value="Inspector(s)"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="AUDIT_TYPE_STR"
                                                     value="Audit Type"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="TCU_DATE"
                                                     value="TCU Date"/>


                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty insTabResult or empty insTabResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="GENERAL_ACK018"
                                                          escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="insTab"
                                               items="${insTabResult.rows}"
                                               varStatus="status">
                                        <tr>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">View Inspection
                                                    Report</p>
                                                <a href="#"
                                                   onclick="fullDetailsView('${MaskUtil.maskValue('appCorrId', insTab.appCorrId)}')">View
                                                    Inspection Report</a>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                <c:out value="${insTab.applicationNo}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application
                                                    Type</p>
                                                <iais:code code="${insTab.appType}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application
                                                    Status</p>
                                                <iais:code code="${insTab.appStatus}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Business Name</p>
                                                <c:out value="${insTab.businessName}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Vehicle No.</p>
                                                <div style="display:table-caption;word-break: keep-all;">${insTab.vehicleNo}</div>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Compliance
                                                    Tagging</p>
                                                <c:out value="${insTab.compliance}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">No. of NCs</p>
                                                <c:out value="${insTab.ncNum}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Risk Tagging</p>
                                                <iais:code code="${insTab.risk}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Inspection Type</p>
                                                <c:out value="${insTab.inspectionType}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Reason for
                                                    Inspection</p>
                                                <c:out value="${insTab.reason}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Inspection Date</p>
                                                <c:out value="${insTab.inspectionDateStr}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Inspector(s)</p>
                                                <div style="display:table-caption;word-break: keep-all;">${insTab.inspectors}</div>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Audit Type</p>
                                                <c:choose>
                                                    <c:when test="${not empty insTab.auditType}">
                                                        <iais:code code="${insTab.auditType}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <iais:code code="-"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">TCU Date</p>
                                                <c:out value="${insTab.tcuDateStr}"/>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <iais:action style="text-align:right;">
                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licence-InsTab-SearchResults-Download">Download</a>
                    </iais:action>
                </div>

            </div>
        </div>
    </div>

</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function doInsClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("All");

    }


    function jumpToPagechangePage() {
        search();
    }

    function searchInspection() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchIns');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchIns');
        $('#mainForm').submit();
    }

    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('reportInfo');
        $('#mainForm').submit();
    }

</script>