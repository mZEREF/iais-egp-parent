<div class="col-md-12">
    <input type="hidden" name="stgCycleId" id="stgCycleId"/>
    <input type="hidden" name="stgSubmitNum" id="stgSubmitNum"/>
    <div class="table-gp col-md-10" style="left: 8%;">
        <table aria-describedby="" class="table view-print-width">
            <thead>
            <tr >
                <th scope="col">AR Centre</th>
                <th scope="col">Fresh Oocyte</th>
                <th scope="col">Thawed Oocyte</th>
                <th scope="col">Frozen Oocyte</th>
                <th scope="col">Fresh Embryos</th>
                <th scope="col">Thawed Embryos</th>
                <th scope="col">Frozen Embryos</th>
                <th scope="col">Frozen Sperms</th>
                <th scope="col">Fresh Sperms</th>
            </tr>
            </thead>
            <tbody class="form-horizontal">
                <c:choose>
                    <c:when test="${empty patientInventoryDtos}">
                        <tr>
                            <td colspan="15">
                                <iais:message key="GENERAL_ACK018"
                                              escape="true"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="patientInventory"
                                   items="${patientInventoryDtos}"
                                   varStatus="status">
                            <tr>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">AR Centre</p>
                                    <c:out value="${patientInventory.key}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Fresh Oocyte</p>
                                    <c:out value="${patientInventory.value.freshOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Thawed Oocyte</p>
                                    <c:out value="${patientInventory.value.thawedOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Frozen Oocyte</p>
                                    <c:out value="${patientInventory.value.frozenOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
                                    <c:out value="${patientInventory.value.freshEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
                                    <c:out value="${patientInventory.value.thawedEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
                                    <c:out value="${patientInventory.value.frozenEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
                                    <c:out value="${patientInventory.value.frozenSpermNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <p class="visible-xs visible-sm table-row-title">Fresh Sperms</p>
                                    <c:out value="${patientInventory.value.freshSpermNum}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    <div class="col-lg-12 col-xs-12">
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <iais:field width="4" value="SEARCH BY" />
                        </iais:row>

                        <hr>

                        <iais:row>
                            <iais:field width="4" value="AR Centre" />
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:select name="arCentre" id="arCentre" firstOption="Please Select" options="arCentreSelectOption" cssClass="clearSel"
                                             value="${arTransactionHistoryFilterDto.arCentre}"  />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Date Of Submission"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${arTransactionHistoryFilterDto.submissionDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="3" cssClass="col-md-3">
                                <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${arTransactionHistoryFilterDto.submissionDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Cycle No."/>
                            <iais:value width="4" cssClass="col-md-4">
                                <input type="number" onkeypress="var keyCode = event.keyCode; event.returnValue = keyCode >= 48 && keyCode <= 57;" style="margin-bottom: 0px;" id="cycleNumber"  name="cycleNumber" value="${arTransactionHistoryFilterDto.cycleNumber}" >
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Include Transfers?" />
                            <div class="col-md-8 row">
                                <iais:value width="8" cssClass="col-md-12 row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="checkbox"
                                               name="includeTransfers" id="includeTransfers"
                                               <c:if test="${ arTransactionHistoryFilterDto.includeTransfers =='on'  }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="includeTransfers">
                                            <span class="check-square"></span>
                                        </label>
                                    </div>
                                </iais:value>
                            </div>
                        </iais:row>




                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doInvClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:searchInventory();">Search
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
                                <table aria-describedby="" class="table view-print-width">
                                    <thead>
                                    <tr >
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="BUSINESS_NAME"
                                                             value="AR Centre"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="SUBMISSION_NO"
                                                             value="Submission ID"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="SUBMIT_DT"
                                                             value="Date of Submission"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="CYCLE_STAGE_DESC"
                                                             value="Stage"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="cycle_no"
                                                             value="Cycle"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="FRESH_OOCY_NUM"
                                                             value="Fresh Oocyte"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="THAWED_OOCY_NUM"
                                                             value="Thawed Oocyte"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="FROZEN_OOCY_NUM"
                                                             value="Frozen Oocyte"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="FRESH_EMBR_NUM"
                                                             value="Fresh Embryos"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="THAWED_EMBR_NUM"
                                                             value="Thawed Embryos"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="FROZEN_EMBR_NUM"
                                                             value="Frozen Embryos"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="FROZEN_SPERM_NUM"
                                                             value="Frozen Sperms"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                             field="FRESH_SPERM_NUM"
                                                             value="Fresh Sperms"/>
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
                                                        <a href="#" onclick="doStageSearch('<iais:mask name="stgCycleId" value="${transaction.cycleId}"/>','<iais:mask name="stgSubmitNum" value="${transaction.submissionIdNo}"/>')">${transaction.submissionIdNo}
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
                                                        <p class="visible-xs visible-sm table-row-title">Fresh Oocyte</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeFreshOocytes }">-</c:when>
                                                            <c:when test="${transaction.changeFreshOocytes == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeFreshOocytes}"/></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Thawed Oocyte</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeThawedOocytes }">-</c:when>
                                                            <c:when test="${transaction.changeThawedOocytes == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeThawedOocytes}"/></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Frozen Oocyte</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeFrozenOocytes }">-</c:when>
                                                            <c:when test="${transaction.changeFrozenOocytes == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeFrozenOocytes}"/></c:otherwise>
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
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeThawedEmbryos }">-</c:when>
                                                            <c:when test="${transaction.changeThawedEmbryos == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeThawedEmbryos}"/></c:otherwise>
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
                                                        <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeFrozenSperms }">-</c:when>
                                                            <c:when test="${transaction.changeFrozenSperms == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeFrozenSperms}"/></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Fresh Sperms</p>
                                                        <c:choose>
                                                            <c:when test="${empty transaction.changeFreshSperms }">-</c:when>
                                                            <c:when test="${transaction.changeFreshSperms == 0}">-</c:when>
                                                            <c:otherwise><c:out value="${transaction.changeFreshSperms}"/></c:otherwise>
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
