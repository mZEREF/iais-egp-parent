<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="Application Search"/>
                        </iais:row>
                        <hr>
                        <iais:row>
                            <iais:field width="4" value="Application No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                       value="${applicationTabEnquiryFilterDto.applicationNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Business Name"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="100" id="businessName" name="businessName"
                                       value="${applicationTabEnquiryFilterDto.businessName}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Vehicle No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="10" id="vehicleNo" name="vehicleNo"
                                       value="${applicationTabEnquiryFilterDto.vehicleNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Application Status"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="appStatus" options="appStatusOption" firstOption="Please Select"
                                             value="${applicationTabEnquiryFilterDto.appStatus}"></iais:select>
                            </iais:value>
                        </iais:row>

                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doAppClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:searchApp();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                <div class="components">

                    <iais:pagination param="appTabParam" result="appTabResult"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-responsive">
                            <thead>
                            <tr>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field=""
                                                     value="S/N"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APPLICATION_NO"
                                                     value="Application No."/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APP_TYPE"
                                                     value="Application Type"/>
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
                                                     field="SUBMIT_DT"
                                                     value="Application Date"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="SUBMIT_BY"
                                                     value="Submitted By"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="AUTO_APPROVE"
                                                     value="Auto Approved"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="APP_STATUS"
                                                     value="Application Status"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="DISPLAY_NAME"
                                                     value="Assigned Officer"/>
                                <iais:sortableHeader needSort="true"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="PMT_STATUS"
                                                     value="Payment Status"/>
                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:choose>
                                <c:when test="${empty appTabResult or empty appTabResult.rows}">
                                    <tr>
                                        <td colspan="11">
                                            <iais:message key="GENERAL_ACK018"
                                                          escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="appTab"
                                               items="${appTabResult.rows}"
                                               varStatus="status">
                                        <tr>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                <c:out value="${status.index + 1+ (appTabParam.pageNo - 1) * appTabParam.pageSize}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                <a href="#">${appTab.applicationNo}</a>

                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application
                                                    Type</p>
                                                <iais:code code="${appTab.appType}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Business Name</p>
                                                <c:out value="${appTab.businessName}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Vehicle No.</p>
                                                <c:out value="${appTab.vehicleNo}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application
                                                    Date</p>
                                                <c:out value="${appTab.submitDtStr}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Submitted By</p>
                                                <c:out value="${appTab.submitDy}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Auto Approved</p>
                                                <c:out value="${appTab.autoApprove}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Application
                                                    Status</p>
                                                <iais:code code="${appTab.appStatus}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Assigned
                                                    Officer</p>
                                                <c:out value="${appTab.assignedOfficer}"/>
                                            </td>
                                            <td style="vertical-align:middle;">
                                                <p class="visible-xs visible-sm table-row-title">Payment Status</p>
                                                <iais:code code="${appTab.pmtStatus}"/>
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
                           href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licence-AppTab-SearchResults-Download">Download</a>
                    </iais:action>
                </div>

            </div>
        </div>
    </div>

</div>

<script>
    function doAppClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("Please Select");

    }


    function jumpToPagechangePage() {
        search();
    }

    function searchApp() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchApp');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchApp');
        $('#mainForm').submit();
    }

</script>