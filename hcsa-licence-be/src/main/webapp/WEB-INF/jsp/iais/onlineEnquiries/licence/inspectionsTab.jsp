<div class="col-md-12">
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="Inspection Search" />
                        </iais:row>
                        <hr>
                        <iais:row>
                            <iais:field width="4" value="Application No." />
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                       value="${insTabEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Business Name"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="100" id="licenceNo" name="licenceNo"
                                       value="${insTabEnquiryFilterDto.licenceNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Vehicle No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="text" maxlength="10" id="vehicleNo" name="vehicleNo"
                                       value="${insTabEnquiryFilterDto.vehicleNo}">
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Application Status" />
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="applicationStatus"  codeCategory="CATE_ID_APP_STATUS" firstOption="Please Select" value="${insTabEnquiryFilterDto.licenceStatus}" ></iais:select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Inspection Date From"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${insTabEnquiryFilterDto.submissionDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="3" cssClass="col-md-3">
                                <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${insTabEnquiryFilterDto.submissionDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Inspection Type" />
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="applicationStatus"  codeCategory="CATE_ID_APP_STATUS" firstOption="Please Select" value="${insTabEnquiryFilterDto.licenceStatus}" ></iais:select>
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

                    <iais:pagination param="transactionParam" result="transactionResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr >
                                    <iais:sortableHeader needSort="false" style="white-space: nowrap;padding: 15px 25px 15px 0px;" field=""
                                                         value="View Inspection Report"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMISSION_NO"
                                                         value="Application No."/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMISSION_NO"
                                                         value="Application Type"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMISSION_NO"
                                                         value="Application Status"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="SUBMIT_DT"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="CYCLE_STAGE_DESC"
                                                         value="Vehicle No."/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="cycle_no"
                                                         value="Compliance Tagging"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="FROZEN_OOCY_NUM"
                                                         value="No. of NCs"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_OOCY_NUM"
                                                         value="Risk Tagging"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="FRESH_OOCY_NUM"
                                                         value="Inspection Type"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="FROZEN_EMBR_NUM"
                                                         value="Reason for Inspection"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_EMBR_NUM"
                                                         value="Inspection Date"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_EMBR_NUM"
                                                         value="Inspector(s)"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_EMBR_NUM"
                                                         value="Audit Type"/>
                                    <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                         field="THAWED_EMBR_NUM"
                                                         value="TCU Date"/>


                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty transactionResult or empty transactionResult.rows}">
                                        <tr>
                                            <td colspan="15">
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
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeFrozenEmbryos }">-</c:when>
                                                        <c:when test="${transaction.changeFrozenEmbryos == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeFrozenEmbryos}"/></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeThawedEmbryos }">-</c:when>
                                                        <c:when test="${transaction.changeThawedEmbryos == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeThawedEmbryos}"/></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
                                                    <c:choose>
                                                        <c:when test="${empty transaction.changeFreshEmbryos }">-</c:when>
                                                        <c:when test="${transaction.changeFreshEmbryos == 0}">-</c:when>
                                                        <c:otherwise><c:out value="${transaction.changeFreshEmbryos}"/></c:otherwise>
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