<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="RFI Search"/>
                        </iais:row>
                        <hr>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Request Date From</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="requestDateFrom" name="requestDateFrom"
                                                 dateVal="${rfiTabEnquiryFilterDto.requestDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="requestDateTo" name="requestDateTo"
                                                 dateVal="${rfiTabEnquiryFilterDto.requestDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Due Date From</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="dueDateFrom" name="dueDateFrom"
                                                 dateVal="${rfiTabEnquiryFilterDto.dueDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="dueDateTo" name="dueDateTo"
                                                 dateVal="${rfiTabEnquiryFilterDto.dueDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Licence No.</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                       value="${rfiTabEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Requested By</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="requestedBy" id="requestedBy" firstOption="All"
                                             options="rfiUserOption"
                                             cssClass="clearSel" value="${rfiTabEnquiryFilterDto.requestedBy}"/>
                            </iais:value>
                        </iais:row>

                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doRfiClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:searchRfi();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                <div class="components">

                    <iais:pagination param="rfiTabParam" result="rfiTabResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="Action"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="TITLE"
                                                         value="Title"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENCE_NO"
                                                         value="Licence No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="REQUEST_DATE"
                                                         value="Request Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="DUE_DATE_SUBMISSION"
                                                         value="Due Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="REQUEST_USER"
                                                         value="Requested By"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="REPLY_DATE"
                                                         value="Licensee Reply Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="STATUS"
                                                         value="Status"/>


                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty rfiTabResult or empty rfiTabResult.rows}">
                                        <tr>
                                            <td colspan="8">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="rfiTab"
                                                   items="${rfiTabResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Action</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('reqInfoId', rfiTab.rfiId)}')">View</a>

                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Title</p>
                                                    <c:out value="${rfiTab.title}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                    <c:out value="${rfiTab.licenceNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Request Date</p>
                                                    <c:out value="${rfiTab.requestDateStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Due Date</p>
                                                    <c:out value="${rfiTab.dueDateStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Requested By</p>
                                                    <c:out value="${rfiTab.requestBy}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee Reply
                                                        Date</p>
                                                    <c:out value="${rfiTab.replyDateStr}"/>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Status</p>
                                                    <c:out value="${rfiTab.status}"/>
                                                </td>


                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <iais:action style="text-align:right;">
                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licence-RfiTab-SearchResults-Download">Download</a>
                    </iais:action>
                </div>

            </div>
        </div>
    </div>

</div>

<script>
    function doRfiClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("All");

    }


    function jumpToPagechangePage() {
        search();
    }

    function searchRfi() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchRfi');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchRfi');
        $('#mainForm').submit();
    }

    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('rfiInfo');
        $('#mainForm').submit();
    }

</script>