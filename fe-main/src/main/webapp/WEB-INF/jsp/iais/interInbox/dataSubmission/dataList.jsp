<form class="" method="post" id="dataForm" action=<%=process.runtime.continueURL()%>>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%-- validation --%>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_type_action_submission_no" id="crud_type_action_submission_no"/>
    <div class="tab-search">
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="licNoPath" style="text-align:left;margin-top: 1.5%">Search by submission ID</label>
                        <div class="col-xs-9 col-md-9">
                            <input id="licNoPath" name="licNoPath" type="text" maxlength="24"
                                   value="${param.submissionNo}">
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="dataSubmissionType" style="text-align:left;margin-top: 1.5%">Type</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="dataSubmissionType"  options="dataSubmissionType" value="${param.type}" firstOption="All" cssClass="dataSubmissionType"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="dataSubmissionStatus" style="text-align:left;margin-top: 1.5%">Status</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="dataSubmissionStatus" id="dataSubmissionStatus" options="dataSubmissionStatus" value="${param.status}" firstOption="All" cssClass="dataSubmissionStatus"/>
                        </div>
                    </iais:value>
                </div>
            <div class="col-md-12">
                <div class="text-right">
                    <button type="button" class="btn btn-secondary" onclick="doClearSearch()">Clear</button>
                    <button type="button" class="btn btn-primary" onclick="doSearch()">Search</button>
                </div>
            </div>
        </div>
        <br> <br>
        <iais:pagination param="dataSubmission" result="dataSubmissionResult"/>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="display: none;"></th>
                        <iais:sortableHeader needSort="false" field="" value=" " style="width:1%;"/>
                        <iais:sortableHeader needSort="true" field="SUBMISSION_NO"
                                             value="Submission ID" style="width:15%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="type" value="Type" style="width:12%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="status" value="Status" style="width:10%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:22%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="CREATED_DT"
                                             value="Last Updated" style="width:13%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="SUBMIT_DT"
                                             value="Submitted On" style="width:13%;" isFE="true"/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty dataSubmissionResult.rows}">
                            <tr>
                                <td colspan="8">
                                    <iais:message key="GENERAL_ACK018" escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="inboxDataSubmissionQuery" items="${dataSubmissionResult.rows}" varStatus="status">
                                <tr>
                                    <td style="display: none;">
                                        <p class="submissionid"><iais:mask name="action_id_value" value="${inboxDataSubmissionQuery.id}"/></p>
                                    </td>
                                    <td>
                                        <c:set var="submissionNo" value="${inboxDataSubmissionQuery.submissionNo}"/>
                                        <p class="visible-xs visible-sm table-row-title"></p>
                                        <div class="form-check">
                                            <input class="form-check-input licenceCheck" id="dataSubmission${submissionNo}" type="checkbox"
                                                   name="submissionNo" value="${inboxDataSubmissionQuery.submissionNo}" aria-invalid="false" <c:if test="${inboxDataSubmissionQuery.submissionSelect}">checked</c:if> onclick="doViewData('${submissionNo}')">
                                            <label class="form-check-label" for="dataSubmission${submissionNo}"><span
                                                    class="check-square"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <a href="#" class="licToView word-wrap" style="font-size: 16px">${licenceQuery.licenceNo}</a>
                                        <input type="hidden" name="licenId${status.index}"
                                               value="<iais:mask name= "licenId${status.index}" value="${licenceQuery.id}"/>"/>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <p>${licenceQuery.svcName}</p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p style="margin-right: 26px;"><iais:code code="${licenceQuery.status}"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Mode of Service Delivery</p>
                                        <c:choose>
                                            <c:when test="${licenceQuery.premisesDtoList.size() == 1}">
                                                <P>${licenceQuery.premisesDtoList[0]}</P>
                                            </c:when>
                                            <c:otherwise>
                                                <select>
                                                    <option value ="">Multiple</option>
                                                    <c:forEach items="${licenceQuery.premisesDtoList}" var="address" varStatus="index">
                                                        <option value ="${address}">${address}</option>
                                                    </c:forEach>
                                                </select>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.startDate}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                        <p><fmt:formatDate value="${licenceQuery.expiryDate}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- Modal -->
                <div class="modal fade" id="isRenewedModal" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-lg modal-dialog-centered" role="document" >
                        <div class="modal-content">
<%--                            <div class="modal-header">--%>
<%--                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                            </div>--%>
                            <div class="modal-body" style="text-align: center">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem;">${LAEM}</span></div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Modal End-->
                <!-- Modal -->
                <div class="modal fade" id="ceasedModal" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                        <div class="modal-content">
<%--                            <div class="modal-header">--%>
<%--                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                            </div>--%>
                            <div class="modal-body" style="text-align: center">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem;">
                                        ${cessationError}
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Modal End-->
                <div class="row" style="padding-bottom: 9%">
                    <div class="col-md-12">
                        <div class="col-md-12 text-right">
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="ds-deleteDraft">Delete Draft</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="ds-amend">Amend</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="ds-withdraw">Withdraw</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="ds-unlock">Request to Unlock</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" value="" id="isNeedDelete" name="isNeedDelete">
    <input type="hidden" value="" id="bundle" name="bundle">
    <iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteRfcDraft()"></iais:confirm>
    <iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftRenewByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteRenewDraft()"></iais:confirm>
    <iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftAppealByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteAppealDraft()"></iais:confirm>
    <iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftCeaseByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteCeaseDraft()"></iais:confirm>
    <iais:confirm msg="${draftByLicAppId}" callBack="bundleNo()" popupOrder="bundleShow" yesBtnDesc="no" cancelBtnDesc="yes" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="bundleYes()"></iais:confirm>
</form>
<script>
    function doClearSearch(){
        $("#licType option:first").prop("selected", 'selected').val("");
        $("#licStatus option:first").prop("selected", 'selected').val("");
    }

    function doSearch(){
        doSubmitForDataSubmission("search");
    }

    function doViewData(actionNo){
        $("#crud_type_action_submission_no").val(actionNo);
        doSubmitForDataSubmission("view");
    }

    function doSubmitForDataSubmission(action){
        showWaiting();
        submit(action);
    }
</script>