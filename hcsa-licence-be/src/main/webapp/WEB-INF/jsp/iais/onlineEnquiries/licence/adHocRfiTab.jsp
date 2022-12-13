<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="RFI Search" />
                        </iais:row>
                        <hr>
                        <iais:row>
                            <iais:field width="4" value="Request Date From"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${rfiTabEnquiryFilterDto.submissionDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="3" cssClass="col-md-3">
                                <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${rfiTabEnquiryFilterDto.submissionDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Due Date From"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${rfiTabEnquiryFilterDto.submissionDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="3" cssClass="col-md-3">
                                <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${rfiTabEnquiryFilterDto.submissionDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Licence No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                       value="${rfiTabEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Requested By"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="mosdType" id="mosdType" firstOption="Please Select" options="mosdTypeOption"
                                             cssClass="clearSel"   value="${rfiTabEnquiryFilterDto.mosdType}" />
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

                    <iais:pagination param="transactionParam" result="transactionResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr >
                                    <iais:sortableHeader needSort="false" style="white-space: nowrap;padding: 15px 25px 15px 0px;" field=""
                                                         value="Action"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMISSION_NO"
                                                         value="Title"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMISSION_NO"
                                                         value="Licence No."/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMIT_DT"
                                                         value="Request Date"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="CYCLE_STAGE_DESC"
                                                         value="Due Date"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="cycle_no"
                                                         value="Requested By"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="FROZEN_OOCY_NUM"
                                                         value="Licensee Reply Date"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_OOCY_NUM"
                                                         value="Status"/>



                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty transactionResult or empty transactionResult.rows}">
                                        <tr>
                                            <td colspan="8">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="transaction"
                                                   items="${transactionResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">AR Centre</p>
                                                    <c:out value="${transaction.arCentre}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Submission ID</p>
                                                    <a href="#" onclick="doStageSearch('${transaction.cycleId}','${transaction.submissionIdNo}')">${transaction.submissionIdNo}
                                                    </a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Date of Submission</p>
                                                    <fmt:formatDate
                                                            value="${transaction.submissionDate}"
                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Stage</p>
                                                    <iais:code code="${transaction.stage}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Cycle</p>
                                                    <c:out value="${transaction.cycle}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeFrozenOocytes }">-</c:when>
                                                        <c:when test="${transaction.changeFrozenOocytes == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeFrozenOocytes}"/></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeThawedOocytes }">-</c:when>
                                                        <c:when test="${transaction.changeThawedOocytes == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeThawedOocytes}"/></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeFreshOocytes }">-</c:when>
                                                        <c:when test="${transaction.changeFreshOocytes == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeFreshOocytes}"/></c:otherwise>
                                                    </c:choose>
                                                </td>


                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>

</div>