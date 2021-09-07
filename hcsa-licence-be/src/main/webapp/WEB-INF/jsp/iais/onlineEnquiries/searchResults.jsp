<style>
    .form-check {
        display: revert;
    }
</style>
<div class="row">
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>

                        <iais:pagination param="SearchParam" result="SearchResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr >
                                        <c:if test="${cease==1}">
                                            <th scope="col" class="form-check">
                                                <c:if test="${!empty SearchResult.rows}">
                                                    <input class="form-check-input licenceCheck"
                                                           type="checkbox" name="userUids"
                                                           id="checkboxAll"
                                                           onchange="javascirpt:checkAll('${isASO}');"/>
                                                    <label class="form-check-label"
                                                           for="checkboxAll">
                                                        <span class="check-square"></span>
                                                    </label>
                                                </c:if>
                                            </th>
                                        </c:if>
                                        <iais:sortableHeader needSort="false" field=""
                                                             value="S/N"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="APPLICATION_NO"
                                                             value="Application No."/>
                                        <iais:sortableHeader needSort="false"
                                                             field="APP_TYPE"
                                                             value="Application Type"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="LICENCE_NO"
                                                             value="Licence No."/>
                                        <iais:sortableHeader needSort="false"
                                                             field="HCI_CODE"
                                                             value="HCI Code"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="HCI_NAME"
                                                             value="HCI Name "/>
                                        <iais:sortableHeader needSort="false"
                                                             field="HCI_ADDRESS"
                                                             value="HCI Address"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="UEN_NO"
                                                             value="UEN"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="LICENSEE_NAME"
                                                             value="Licensee Name"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="SERVICE_NAME"
                                                             value="Service Name"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Licence_Period"
                                                             value="Licence Period"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Licence_Status"
                                                             value="Licence Status"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="2nd_last_compliance_history"
                                                             value="2nd Last Compliance History"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="last_compliance_history"
                                                             value="Last Compliance History"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="current_risk_tagging"
                                                             value="Current Risk Tagging"/>
                                    </tr>
                                    </thead>
                                    <tbody class="form-horizontal">
                                    <c:choose>
                                        <c:when test="${empty SearchResult.rows}">
                                            <tr>
                                                <td colspan="15">
                                                    <iais:message key="GENERAL_ACK018"
                                                                  escape="true"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <style>
                                                .nice-select {
                                                    /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
                                                    background: #efefef;
                                                    /* Old browsers */
                                                    background: -moz-linear-gradient(left, #efefef 0%, white 100%);
                                                    /* FF3.6-15 */
                                                    background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
                                                    /* Chrome10-25,Safari5.1-6 */
                                                    background: linear-gradient(to right, #efefef 0%, white 100%);
                                                    border: 1px solid #6c6c6c;
                                                    border-radius: 14px;
                                                    font-size: 1.6rem;
                                                    padding: 14px 40px 14px 15px;
                                                    height: auto;
                                                    line-height: 22px;
                                                    width: 100%;
                                                    -webkit-transition: all 0.1s ease;
                                                    -moz-transition: all 0.1s ease;
                                                    -ms-transition: all 0.1s ease;
                                                    -o-transition: all 0.1s ease;
                                                    transition: all 0.1s ease;
                                                    white-space: normal;
                                                    text-overflow: inherit;
                                                }
                                            </style>
                                            <c:forEach var="pool"
                                                       items="${SearchResult.rows}"
                                                       varStatus="status">
                                                <tr>
                                                    <c:if test="${cease==1}">
                                                        <td class="form-check"
                                                            onclick="javascript:controlCease('${isASO}')">
                                                            <input class="form-check-input licenceCheck"
                                                                   id="licence${status.index + 1}"
                                                                   type="checkbox"
                                                                   name="appIds"
                                                                   value="${pool.appId}|${pool.isCessation}|${pool.licenceId}|${pool.licenceStatus}">
                                                            <label class="form-check-label"
                                                                   for="licence${status.index + 1}"><span
                                                                    class="check-square"></span>
                                                            </label>
                                                        </td>
                                                    </c:if>
                                                    <td class="row_no">
                                                        <c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/>
                                                    </td>
                                                    <td>
                                                        <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                        <c:if test="${pool.appCorrId!=null}"><a href="#"
                                                                onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                                    </td>
                                                    <td><c:out
                                                            value="${pool.applicationType}"/></td>
                                                    <td>
                                                        <c:if test="${pool.licenceId!=null&&pool.licenceStatus!='Inactive'}"><a href="#"
                                                                onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></c:if>
                                                        <c:if test="${pool.licenceId==null|| pool.licenceStatus=='Inactive'}">${pool.licenceNo}</c:if>
                                                    </td>
                                                    <td><c:out
                                                            value="${pool.hciCode}"/><c:if
                                                            test="${empty pool.hciCode}">-</c:if></td>
                                                    <td><c:out
                                                            value="${pool.hciName}"/></td>
                                                    <td>
                                                        <c:if test="${pool.licenceNo==null}">
                                                            <c:choose>
                                                                <c:when test="${pool.address.size() == 0}">
                                                                    <c:out value="-"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:out value="${pool.address[0]}"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${pool.licenceNo!=null}">
                                                            <c:choose>
                                                                <c:when test="${pool.address.size() == 1}">
                                                                    <c:out value="${pool.address[0]}"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <select>
                                                                        <option value="">
                                                                            Multiple
                                                                        </option>
                                                                        <c:forEach
                                                                                items="${pool.address}"
                                                                                var="address"
                                                                                varStatus="index">
                                                                            <option value="${address}">${address}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </td>
                                                    <td><c:out
                                                            value="${pool.uen}"/></td>
                                                    <td><c:out
                                                            value="${pool.licenseeName}"/></td>
                                                    <td><c:if test="${pool.appCorrId==null}">${pool.serviceName}</c:if>
                                                        <c:if test="${pool.appCorrId!=null}"><iais:service value="${pool.serviceName}"/></c:if>
                                                    </td>
                                                    <td><fmt:formatDate
                                                            value="${pool.startDate}"
                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                                            value="${pool.expiryDate}"
                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/></td>
                                                    <td><c:out
                                                            value="${pool.licenceStatus}"/></td>
                                                    <td><c:out
                                                            value="${pool.getTwoLastComplianceHistory()}"/></td>
                                                    <td><c:out
                                                            value="${pool.getLastComplianceHistory()}"/></td>
                                                    <td><c:out
                                                            value="${pool.currentRiskTagging}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                            <c:if test="${blank}">
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                                <div class="row">&nbsp;</div>
                            </c:if>
                        </div>
                        <div class="row">&nbsp;</div>
                        <iais:row id="selectDecisionMsg" style="display: none">
                            <div class="row" height="1"
                                 style="font-size: 1.6rem; color: #D22727; padding-left: 20px">
                                <iais:message key="CESS_ERR006"
                                              escape="flase"></iais:message>
                            </div>
                        </iais:row>
                        <iais:row id="selectDecisionMsgActive" style="display: none">
                            <div class="row" height="1"
                                 style="font-size: 1.6rem; color: #D22727; padding-left: 20px">
                                <iais:message key="CESS_ERR005"
                                              escape="flase"></iais:message>
                            </div>
                        </iais:row>
                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
                            <c:if test="${cease==1}">
                                <button type="button" class="btn btn-primary ReqForInfoBtn"
                                        disabled
                                        onclick="javascript:doReqForInfo();">ReqForInfo
                                </button>
                                <button type="button" class="btn btn-primary CeaseBtn"
                                        disabled
                                        onclick="javascript:doCessation();">Cease
                                </button>
                            </c:if>
                        </iais:action>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>