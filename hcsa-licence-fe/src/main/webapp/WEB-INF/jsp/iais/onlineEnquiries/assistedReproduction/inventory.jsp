<div class="col-md-12">

    <div class="table-gp col-md-10" style="left: 8%;">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col">AR Centre</th>
                <th scope="col">Frozen Oocyte</th>
                <th scope="col">Thawed Oocyte</th>
                <th scope="col">Fresh Oocyte</th>
                <th scope="col">Frozen Embryos</th>
                <th scope="col">Thawed Embryos</th>
                <th scope="col">Fresh Embryos</th>
                <th scope="col">Frozen Sperms</th>
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
                                    <c:out value="${patientInventory.key}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.frozenOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.thawedOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.freshOocyteNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.frozenEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.thawedEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.freshEmbryoNum}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.frozenSpermNum}"/>
                                </td>

                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
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
                <iais:value width="8" cssClass="col-md-8">
                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="cycleNumber"  name="cycleNumber" value="${arTransactionHistoryFilterDto.cycleNumber}" >
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

    <div class="col-xs-12 row">
        <div class="components">

            <iais:pagination param="transactionParam" result="transactionResult"/>
            <div class="table-responsive">
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr >
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="BUSINESS_NAME"
                                                 value="AR Centre"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="SUBMISSION_NO"
                                                 value="Submission ID"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="SUBMIT_DT"
                                                 value="Date of Submission"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="CYCLE_STAGE_DESC"
                                                 value="Stage"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="cycle_no"
                                                 value="Cycle"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="FROZEN_OOCY_NUM"
                                                 value="Frozen Oocyte"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="THAWED_OOCY_NUM"
                                                 value="Thawed Oocyte"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="FRESH_OOCY_NUM"
                                                 value="Fresh Oocyte"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="FROZEN_EMBR_NUM"
                                                 value="Frozen Embryos"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="THAWED_EMBR_NUM"
                                                 value="Thawed Embryos"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="FRESH_EMBR_NUM"
                                                 value="Fresh Embryos"/>
                            <iais:sortableHeader needSort="true" style="white-space: nowrap;"
                                                 field="FROZEN_SPERM_NUM"
                                                 value="Frozen Sperms"/>
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
                                            <c:out value="${transaction.arCentre}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <a href="#" onclick="doStageSearch('${transaction.cycleId}','${transaction.submissionIdNo}')">${transaction.submissionIdNo}
                                            </a>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <fmt:formatDate
                                                    value="${transaction.submissionDate}"
                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <iais:code code="${transaction.stage}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:out value="${transaction.cycle}"/>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeFrozenOocytes }">-</c:when>
                                                <c:when test="${transaction.changeFrozenOocytes == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeFrozenOocytes}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeThawedOocytes }">-</c:when>
                                                <c:when test="${transaction.changeThawedOocytes == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeThawedOocytes}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeFreshOocytes }">-</c:when>
                                                <c:when test="${transaction.changeFreshOocytes == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeFreshOocytes}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeFrozenEmbryos }">-</c:when>
                                                <c:when test="${transaction.changeFrozenEmbryos == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeFrozenEmbryos}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeThawedEmbryos }">-</c:when>
                                                <c:when test="${transaction.changeThawedEmbryos == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeThawedEmbryos}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeFreshEmbryos }">-</c:when>
                                                <c:when test="${transaction.changeFreshEmbryos == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeFreshEmbryos}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="vertical-align:middle;">
                                            <c:choose>
                                                <c:when test="${empty transaction.changeFrozenSperms }">-</c:when>
                                                <c:when test="${transaction.changeFrozenSperms == 0}">-</c:when>
                                                <c:otherwise><c:out value="${transaction.changeFrozenSperms}"/></c:otherwise>
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
