<form class="" method="post" id="dataForm" action=<%=process.runtime.continueURL()%>>
    <%-- validation --%>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_type_action_submission_no" id="crud_type_action_submission_no"/>
    <div class="tab-search">
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="submissionNoDataSubmission" style="text-align:left;margin-top: 1.5%">Search by submission ID</label>
                        <div class="col-xs-9 col-md-9">
                            <input id="submissionNoDataSubmission" name="submissionNoDataSubmission" type="text" maxlength="24"
                                   value="${param.submissionNo}">
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="typeDataSubmission" style="text-align:left;margin-top: 1.5%">Type</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="typeDataSubmission"  codeCategory="DATA_SUBMISSION_TYPE" value="${param.type}" firstOption="All" cssClass="dataSubmissionType"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="statusDataSubmission" style="text-align:left;margin-top: 1.5%">Status</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="statusDataSubmission" id="statusDataSubmission" codeCategory="DATA_SUBMISSION_STATUS" value="${param.status}" firstOption="All" cssClass="dataSubmissionStatus"/>
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
        <iais:pagination param="dataSubmissionParam" result="dataSubmissionResult"/>
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
                                                   name="submissionNo" value="${inboxDataSubmissionQuery.submissionNo}" aria-invalid="false" <c:if test="${inboxDataSubmissionQuery.submissionSelect}">checked</c:if> onclick="doCheckBoxSelect('${submissionNo}')">
                                            <label class="form-check-label" for="dataSubmission${submissionNo}"><span
                                                    class="check-square"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <a href="#" class="licToView word-wrap" style="font-size: 16px" onclick="doViewData('${submissionNo}')">${submissionNo}</a>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                       <iais:code code="${inboxDataSubmissionQuery.type}"/>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                        <p style="margin-right: 26px;"><iais:code code="${inboxDataSubmissionQuery.status}"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Business Name</p>
                                        <p style="margin-right: 26px;"><c:out value="${inboxDataSubmissionQuery.businessName}"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Last Updated</p>
                                        <p><fmt:formatDate value="${licenceQuery.lastUpdated}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Submitted On</p>
                                        <p><fmt:formatDate value="${licenceQuery.submittedOn}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
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
    <input type="hidden" value="${empty needValidatorSize ? 0 : needValidatorSize}" id="needValidatorSize" name="needValidatorSize">
    <iais:confirm msg="DS_ERR014" needCancel="false" popupOrder="actionDsButton"  yesBtnDesc="Yes"   yesBtnCls="btn btn-secondary"  callBack="cancelBallDsButton()" />
</form>
<script>

    $(document).ready(function () {
        if(${actionDsButtonShow == 1}){
            $("#actionDsButton").show();
        }
    });

    function doClearSearch(){
        $("#licType option:first").prop("selected", 'selected').val("");
        $("#licStatus option:first").prop("selected", 'selected').val("");
    }

    function doSearch(){
        doSubmitForDataSubmission("search");
    }
    function doCheckBoxSelect(actionNo){
        let size = $("#needValidatorSize").val()
        if($("#dataSubmission"+actionNo).is(':checked')){
            size++;
        }else {
            size--;
        }
        $("#needValidatorSize").val(size);
        if(size > 0){
            $('#ds-deleteDraft').removeClass("disabled");
            $('#ds-amend').removeClass("disabled");
            $('#ds-withdraw').removeClass("disabled");
            $('#ds-unlock').removeClass("disabled");
        }else {
            $('#ds-deleteDraft').addClass("disabled");
            $('#ds-amend').addClass("disabled");
            $('#ds-withdraw').addClass("disabled");
            $('#ds-unlock').addClass("disabled");
        }
    }

    function doViewData(actionNo){
        $("#crud_type_action_submission_no").val(actionNo);
        doSubmitForDataSubmission("view");
    }

    function doSubmitForDataSubmission(action){
        showWaiting();
        submit(action);
    }

    function cancelBallDsButton(){
        $("#actionDsButton").hide();
    }


    $('#ds-deleteDraft').click(doSubmitForDataSubmission('deleteDraft'));
    $('#ds-amend').click(doSubmitForDataSubmission('rfc'));
    $('#ds-withdraw').click(doSubmitForDataSubmission('withdraw'));
    $('#ds-unlock').click(doSubmitForDataSubmission('unlock'));
</script>