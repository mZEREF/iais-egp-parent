<style>
    thead > tr > th > p {
        line-height: 33px;
    }
</style>
<div class="col-md-12">

    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col">AR Centre</th>
                <th scope="col">Frozen Oocytes</th>
                <th scope="col">Thawed Oocytes</th>
                <th scope="col">Fresh Oocytes</th>
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
                                    <c:out value="${patientInventory.value.currentFrozenOocytes}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentThawedOocytes}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentFreshOocytes}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentFrozenEmbryos}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentThawedEmbryos}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentFreshEmbryos}"/>
                                </td>
                                <td style="vertical-align:middle;">
                                    <c:out value="${patientInventory.value.currentFrozenSperms}"/>
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
                                <select name="arCentre" id="arCentre">
                                    <option value="" <c:if test="${empty arTransactionHistoryFilterDto.arCentre}">selected="selected"</c:if>>Please Select</option>
                                    <c:forEach items="${arCentreSelectOption}" var="selectOption">
                                        <option value="${selectOption.value}" <c:if test="${arTransactionHistoryFilterDto.arCentre ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                                    </c:forEach>
                                </select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Date Of Submission"/>
                            <iais:value width="4" cssClass="col-md-4">
                                <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${arTransactionHistoryFilterDto.submissionDateFrom}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4">
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
                            <div class="col-md-8">
                                <iais:value width="8" cssClass="col-md-12">
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

                <div class="col-xs-12">
                    <div class="components">

                        <iais:pagination param="transactionParam" result="transactionResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr >
                                        <iais:sortableHeader needSort="true"
                                                             field="BUSINESS_NAME"
                                                             value="AR Centre"/>
                                        <iais:sortableHeader needSort="true"
                                                             field="SUBMISSION_NO"
                                                             value="Submission ID"/>
                                        <iais:sortableHeader needSort="true"
                                                             field="SUBMIT_DT"
                                                             value="Submission Date"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="CYCLE_STAGE"
                                                             value="Stage"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="cycle_no"
                                                             value="Cycle"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Frozen Oocytes"
                                                             value="Frozen Oocytes"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Thawed Oocytes"
                                                             value="Thawed Oocytes"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Fresh Oocytes"
                                                             value="Fresh Oocytes"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Frozen Embryos"
                                                             value="Frozen Embryos"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Thawed Embryos"
                                                             value="Thawed Embryos"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Fresh Embryos"
                                                             value="Fresh Embryos"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Frozen Sperms"
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
                                                        <c:out value="${transaction.changeFrozenOocytes}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeThawedOocytes}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeFreshOocytes}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeFrozenEmbryos}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeThawedEmbryos}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeFreshEmbryos}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${transaction.changeFrozenSperms}"/>
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
</div>
