<form class="" method="post" id="dataForm" action=<%=process.runtime.continueURL()%>>
    <%-- validation --%>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_type_action_submission_no" id="crud_type_action_submission_no"/>
    <div class="tab-search">
        <div id="clearBody">
        <div class="row">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-3 col-md-3" for="submissionNoDataSubmission" style="text-align:left;margin-top: 1.5%">Submission ID:</label>
                    <div class="col-xs-9 col-md-9">
                        <input id="submissionNoDataSubmission" name="submissionNoDataSubmission" type="text" maxlength="19"
                               value="${param.submissionNoDataSubmission}">
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row" style="margin-bottom: 1.5%">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-3 col-md-3" for="typeDataSubmission" style="text-align:left;margin-top: 1.5%">Type:</label>
                    <div class="col-xs-9 col-md-9">
                        <iais:select name="typeDataSubmission"  codeCategory="DATA_SUBMISSION_TYPE" value="${param.typeDataSubmission}" firstOption="All" cssClass="dataSubmissionType" needSort="true"/>
                    </div>
                </iais:value>
            </div>
        </div>

        <div class="row" style="margin-bottom: 1.5%">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-3 col-md-3" for="statusDataSubmission" style="text-align:left;margin-top: 1.5%">Status:</label>
                    <div class="col-xs-9 col-md-9">
                        <iais:select name="statusDataSubmission" id="statusDataSubmission" options="dsStatuses" value="${param.statusDataSubmission}" firstOption="All" cssClass="dataSubmissionStatus" needSort="true" />
                    </div>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <iais:value>
                    <label class="col-xs-3 col-md-3 col-lg-3" style="text-align:left;margin-top: 1.5%">Last Updated:</label>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <iais:datePicker id="lastDateStart" name="lastDateStart" value="${param.lastDateStart}"/>
                    </div>
                    <div class="col-xs-3 col-md-1 col-lg-1" style="margin-top: 1.5%">
                        <label>To</label>
                    </div>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <iais:datePicker id="lastDateEnd" name="lastDateEnd" value="${param.lastDateEnd}"/>
                    </div>
                </iais:value>
            </div>
        </div>
            <div class="row" style="margin-bottom: 30px;">
                <div class="col-md-12">
                    <span class="col-xs-3 col-md-3"></span>
                    <div class="col-md-9">
                        <span id="lastDateerror-msg" class="error-msg" style="width: 150%;font-size:1.5rem">${lastUpdateINBOX_ERR011}</span>
                    </div>
                </div>
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
                        <iais:sortableHeader needSort="true" field="typeDesc" value="Type" style="width:12%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="statusDesc" value="Status" style="width:10%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:22%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="UPDATED_DT"
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
                                        <p><fmt:formatDate value="${inboxDataSubmissionQuery.lastUpdated}"
                                                           pattern="dd/MM/yyyy"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Submitted On</p>
                                        <p><fmt:formatDate value="${inboxDataSubmissionQuery.submittedOn}"
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
                            <c:set var="disabledCssNoOnlyOne" value="${(empty needValidatorSize || needValidatorSize == 0) ? 'disabled' : ''}"/>
                            <c:set var="disabledCssOnlyOne" value="${((empty needValidatorSize || needValidatorSize == 0) || (!empty needValidatorSize && needValidatorSize> 1)) ? 'disabled' : ''}"/>
                            <a class="btn btn-primary ${disabledCssOnlyOne}" href="javascript:void(0);" id="ds-deleteDraft">Delete Draft</a>
                            <a class="btn btn-primary ${disabledCssOnlyOne}" href="javascript:void(0);" id="ds-amend">Amend</a>
                            <a class="btn btn-primary ${disabledCssNoOnlyOne}" href="javascript:void(0);" id="ds-withdraw">Withdraw</a>
                            <a class="btn btn-primary ${disabledCssNoOnlyOne}" href="javascript:void(0);" id="ds-unlock">Request to Unlock</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" value="${empty needValidatorSize ? 0 : needValidatorSize}" id="needValidatorSize" name="needValidatorSize">
        <input type="hidden" value="${actionDsButtonShow}" id="actionDsButtonShow" name="actionDsButtonShow">
        <input type="hidden" value="${deleteDraftOk}" id="deleteDraftOkShow" name="deleteDraftOkShow">
        <iais:confirm msg="${empty showPopFailMsg ? 'DS_ERR014' : showPopFailMsg}" needCancel="false" popupOrder="actionDsButton"  yesBtnDesc="Yes"   yesBtnCls="btn btn-secondary"  callBack="cancelBallDsButton()" />
        <iais:confirm msg="INBOX_ACK006" needCancel="false" popupOrder="deleteDraftOkButton"  yesBtnDesc="OK"   yesBtnCls="btn btn-primary"  callBack="deleteDraftOkCallBack()" />
        <iais:confirm msg="NEW_ACK002" needFungDuoJi="false" popupOrder="deleteDraftModal" callBack="delDraftCancelBtn()"  cancelFunc="delDraftYesBtn()" cancelBtnDesc="OK" yesBtnDesc="Cancel" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"  />
        <iais:confirm msg="DS_ERR057" needFungDuoJi="false" needCancel="false" popupOrder="actionNoDraftDataDelete"  yesBtnDesc="OK"   yesBtnCls="btn btn-secondary"  callBack="actionNoDraftDataDeleteCancelBallDsButton()" />
</form>
<script type="application/javascript">

    $(document).ready(function () {
        if($("#actionDsButtonShow").val() == 1){
            $("#actionDsButton").modal('show');
        }
        if($("#deleteDraftOkShow").val() == 1){
            $("#deleteDraftOkButton").modal('show');
        }
    });

    function doClearSearch(){
        $("#statusDataSubmission option:first").prop("selected", 'selected').val("");
        $("#typeDataSubmission option:first").prop("selected", 'selected').val("");
        $("#submissionNoDataSubmission").val("");
        $("#clearBody .current").text("All");
        $("#submissionNoDataSubmission").val("");
        $("#submissionNoDataSubmission").val("");
        $("[name='lastDateStart']").val("");
        $("[name='lastDateEnd']").val("");
        $("#lastDateerror-msg").html('');
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
        if(size == 1){
            $('#ds-deleteDraft').removeClass("disabled");
            $('#ds-amend').removeClass("disabled");
            $('#ds-withdraw').removeClass("disabled");
            $('#ds-unlock').removeClass("disabled");
        }else if (size <= 0) {
            $('#ds-deleteDraft').addClass("disabled");
            $('#ds-amend').addClass("disabled");
            $('#ds-withdraw').addClass("disabled");
            $('#ds-unlock').addClass("disabled");
        }else if(size>1){
            $('#ds-amend').addClass("disabled");
            $('#ds-deleteDraft').addClass("disabled");
        }
    }

    function doViewData(actionNo){
        $("#crud_type_action_submission_no").val(actionNo);
        doSubmitForDataSubmission("view");
    }

    function doSubmitForDataSubmission(action){
        showWaiting();
        $("[name='crud_action_type']").val(action);
        document.getElementById('dataForm').submit();
    }

    function cancelBallDsButton(){
        $("#actionDsButton").modal('hide');
    }
    function deleteDraftOkCallBack(){
        $("#deleteDraftOkButton").modal('hide');
    }
    function dssToMsgPage(){
        window.location = "${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox")}";
    }
    function dssToAppPage(){
        window.location = "${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox?initPage=initApp")}";
    }
    function dssToLicPage(){
        window.location = "${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox?initPage=initLic")}";
    }


    $('#ds-deleteDraft').click(function (){
        //get first
        if($("[name='submissionNo']:checked").val().indexOf('DS') >= 0){
            $("#deleteDraftModal").modal('show');
        }else {
            $("#actionNoDraftDataDelete").modal('show');
        }

    });
    $('#ds-amend').click(function (){
        doSubmitForDataSubmission('rfc');
    });
    $('#ds-withdraw').click(function (){
        doSubmitForDataSubmission('withdraw');}
    );
    $('#ds-unlock').click(function (){
        doSubmitForDataSubmission('unlock');}
    );
    function actionNoDraftDataDeleteCancelBallDsButton(){
        $("#actionNoDraftDataDelete").modal('hide');
    }

    function delDraftCancelBtn(){
        $("#deleteDraftModal").modal('hide');
    }
    function  delDraftYesBtn(){
        doSubmitForDataSubmission('deleteDraft');
    }
    function jumpToPagechangePage(){
        doSubmitForDataSubmission('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        doSubmitForDataSubmission('sort');
    }
</script>