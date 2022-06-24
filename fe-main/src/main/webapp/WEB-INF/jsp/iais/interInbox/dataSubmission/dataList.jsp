<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts" %>
<style>
    .table {
        width: 140%;
        max-width: 140%;
        margin-bottom: 1rem;
        color: #212529;
        border-collapse: collapse;
        border-spacing: 0;
        background-color: transparent;
    }
</style>
<form class="" method="post" id="dataForm" action=<%=process.runtime.continueURL()%>>
    <%-- validation --%>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_type_action_submission_no" id="crud_type_action_submission_no"/>
    <div class="tab-search">
        <div id="clearBody">
        <div class="row">
                <iais:value>
                    <label class="col-xs-3 col-md-2 col-lg-2" style="text-align:left;margin-top: 1.5%">Submission ID:</label>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <input id="submissionNoDataSubmission" name="submissionNoDataSubmission" type="text" maxlength="19"
                               value="${param.submissionNoDataSubmission}">
                    </div>
                    <div class="col-xs-3 col-md-2 col-lg-2" style="margin-top: 1.5%">
                        <label>Type:</label>
                    </div>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <iais:select multiSelect="true" name="typeDataSubmission" id ="typeDataSubmission" options="submissionTypes" multiValues="${typeSelectValues}"  cssClass="dataSubmissionType" needSort="true"/>
                    </div>
                </iais:value>
        </div>
        <div class="row" style="margin-bottom: 1.5%">
            <iais:value>
                <label class="col-xs-3 col-md-2 col-lg-2" style="text-align:left;margin-top: 1.5%">Patient Name:</label>
                <div class="col-xs-9 col-md-4 col-lg-4">
                    <input id="patientNameDataSubmission" name="patientNameDataSubmission" type="text" maxlength="255"
                           value="${param.patientNameDataSubmission}">
                </div>
                <div class="col-xs-3 col-md-2 col-lg-2" style="margin-top: 1.5%">
                    <label>Status:</label>
                </div>
                <div class="col-xs-9 col-md-4 col-lg-4">
                    <iais:select name="statusDataSubmission" id="statusDataSubmission" options="dsStatuses" value="${param.statusDataSubmission}" firstOption="All" cssClass="dataSubmissionStatus" needSort="true" />
                </div>
            </iais:value>
        </div>


        <div class="row" style="margin-bottom: 1.5%">
            <iais:value>
                <label class="col-xs-3 col-md-2 col-lg-2" style="text-align:left;margin-top: 1.5%">Patient ID Number:</label>
                <div class="col-xs-9 col-md-4 col-lg-4">
                    <input id="patientIdNumberDataSubmission" name="patientIdNumberDataSubmission" type="text" maxlength="255"
                           value="${param.patientIdNumberDataSubmission}">
                </div>
                <div class="col-xs-3 col-md-2 col-lg-2" style="margin-top: 1.5%">
                    <label>Submitted By:</label>
                </div>
                <div class="col-xs-9 col-md-4 col-lg-4">
                    <input id="submittedByDataSubmission" name="submittedByDataSubmission" type="text" maxlength="255"
                           value="${param.submittedByDataSubmission}">
                </div>
            </iais:value>
        </div>

        <div class="row">
                <iais:value>
                    <label class="col-xs-3 col-md-2 col-lg-2" style="text-align:left;margin-top: 1.5%">Last Updated:</label>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <iais:datePicker id="lastDateStart" name="lastDateStart" value="${param.lastDateStart}"/>
                    </div>
                    <div class="col-xs-3 col-md-2 col-lg-2" style="margin-top: 1.5%">
                        <label>To:</label>
                    </div>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <iais:datePicker id="lastDateEnd" name="lastDateEnd" value="${param.lastDateEnd}"/>
                    </div>
                </iais:value>
        </div>

            <div class="row" style="margin-bottom: 30px;">
                    <span class="col-xs-3 col-md-2"></span>
                    <div class="col-md-9">
                        <span id="lastDateerror-msg" class="error-msg" style="width: 150%;font-size:1.5rem">${lastUpdateINBOX_ERR011}</span>
                    </div>
            </div>

            <div class="row" style="margin-bottom: 1.5%">
                <iais:value>
                    <label class="col-xs-3 col-md-2 col-lg-2" style="text-align:left;margin-top: 1.5%">Business Name:</label>
                    <div class="col-xs-9 col-md-4 col-lg-4">
                        <input id="businessNameDataSubmission" name="businessNameDataSubmission" type="text" maxlength="255"
                               value="${param.businessNameDataSubmission}">
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
        <c:if test="${submissionTypes.size() > 13}"><br><br></c:if>

        <iais:pagination param="dataSubmissionParam" result="dataSubmissionResult"/>
    </div>


            <div class="table-responsive table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="display: none;"></th>
                        <iais:sortableHeader needSort="false" field="" value=" " style="width:1%;"/>
                        <iais:sortableHeader needSort="true" field="SUBMISSION_NO"
                                             value="Submission ID"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="Patient_Id_Number"
                                             value="Patient ID Number"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="typeDesc" value="Type"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="statusDesc" value="Status"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="UPDATED_DT"
                                             value="Last Updated"  isFE="true"/>
                        <iais:sortableHeader needSort="true" field="SUBMIT_BY"
                                             value="Submitted By"  isFE="true"/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty dataSubmissionResult.rows}">
                            <tr>
                                <td colspan="10">
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
                                            <c:set  var="typeForWithdraw" value="${StringUtil.stringsContainKey(inboxDataSubmissionQuery.type,DataSubmissionConsts.DS_CYCLE_AR,DataSubmissionConsts.DS_CYCLE_IUI,DataSubmissionConsts.DS_CYCLE_EFO) ? 'sameType' : inboxDataSubmissionQuery.type}"/>
                                            <input class="form-check-input licenceCheck" id="dataSubmission${submissionNo}" type="checkbox"
                                                   name="submissionNo" value="${submissionNo}" aria-invalid="false" <c:if test="${inboxDataSubmissionQuery.submissionSelect}">checked</c:if> onclick="doCheckBoxSelect('${submissionNo}','${typeForWithdraw}')">
                                            <label class="form-check-label" for="dataSubmission${submissionNo}"><span
                                                    class="check-square"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Submission ID</p>
                                        <a href="#" class="licToView word-wrap" style="font-size: 16px" onclick="doViewData('${submissionNo}')">${submissionNo}</a>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Patient ID Number</p>
                                        <iais:code code="${inboxDataSubmissionQuery.patientIdNumber}"/>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                        <input type="hidden" value="${typeForWithdraw}" id="typeValue${submissionNo}" name="typeValue${submissionNo}">
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
                                        <p class="visible-xs visible-sm table-row-title">Submitted By</p>
                                        <p style="margin-right: 26px;"><c:out value="${inboxDataSubmissionQuery.submittedBy}"/></p>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <br>
                <div  style="padding-bottom: 3%;width: 140%" class="text-left">
                        <c:if test="${canCreateDs}">
                            <a class="btn btn-primary" href="/hcsa-licence-web/eservice/INTERNET/MohDataSubmission">Create</a>
                        </c:if>
                        <c:if test="${dataSubARTPrivilege == 1}">
                            <a class="btn btn-primary" href="/hcsa-licence-web/eservice/INTERNET/MohOnlineEnquiryAssistedReproduction">AR Online Enquiry</a>
                        </c:if>
                        <c:if test="${dataSubLDTPrivilege == 1}">
                            <a  class="btn btn-primary" href="/hcsa-licence-web/eservice/INTERNET/MohLabDevelopedTestsEnquiry">LDT Online Enquiry</a>
                        </c:if>
                        <c:set var="disabledCssNoOnlyOne" value="${(empty needValidatorSize || needValidatorSize == 0) ? 'disabled' : ''}"/>
                        <c:set var="disabledCssOnlyOne" value="${((empty needValidatorSize || needValidatorSize == 0) || (!empty needValidatorSize && needValidatorSize> 1)) ? 'disabled' : ''}"/>
                       <%-- <c:set var="disabledCssForWithDraw" value="${disabledCssNoOnlyOne == 'disabled' ? disabledCssNoOnlyOne : (StringUtil.stringContain( selectAllTypeSub,',') ? 'disabled' : '')}"/>--%>
                        <c:set var="disabledCssForRFC" value="${disabledCssOnlyOne == 'disabled' ? disabledCssOnlyOne : (StringUtil.stringContain( selectAllTypeSub,'VSS') ? 'disabled' : '')}"/>
                        <a class="btn btn-primary ${disabledCssNoOnlyOne}" href="javascript:void(0);" id="ds-deleteDraft">Delete Draft</a>

                        <c:if test="${canAmendDs}">
                            <a class="btn btn-primary ${disabledCssForRFC}" href="javascript:void(0);" id="ds-amend">Amend</a>
                        </c:if>

                        <%--<a class="btn btn-primary ${disabledCssForWithDraw}" href="javascript:void(0);" id="ds-withdraw">Withdraw</a>--%>
                       <c:if test="${dataSubARTPrivilege == 1}">
                        <a class="btn btn-primary ${disabledCssNoOnlyOne}" href="javascript:void(0);" id="ds-unlock">Request to Unlock</a>
                       </c:if>
                </div>
            </div>



    <input type="hidden" value="${empty needValidatorSize ? 0 : needValidatorSize}" id="needValidatorSize" name="needValidatorSize">
        <input type="hidden" value="${empty selectAllTypeSub ? '' : selectAllTypeSub}" id="selectAllTypeSub" name="selectAllTypeSub">
        <input type="hidden" value="${actionDsButtonShow}" id="actionDsButtonShow" name="actionDsButtonShow">
        <input type="hidden" value="${deleteDraftOk}" id="deleteDraftOkShow" name="deleteDraftOkShow">
        <input type="hidden" value="${rfcType}" id="rfcType" name="rfcType">
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
        $("#submissionNoDataSubmission").val("");
        $("#clearBody .current").text("All");
        $("#clearBody input[type='checkbox']").prop('checked', false);
        $('#typeDataSubmission').val('');
        $("#clearBody .multi-select-button").html("-- Select --");
        $("#patientNameDataSubmission").val("");
        $("#patientIdNumberDataSubmission").val("");
        $("#submittedByDataSubmission").val("");
        $("#businessNameDataSubmission").val("");
        $("[name='lastDateStart']").val("");
        $("[name='lastDateEnd']").val("");
        $("#lastDateerror-msg").html('');
    }

    function doSearch(){
        doSubmitForDataSubmission("search");
    }
    function doCheckBoxSelect(actionNo,type){
        let size = $("#needValidatorSize").val()
        var selectAllTypeSub = $("#selectAllTypeSub").val();
        if($("#dataSubmission"+actionNo).is(':checked')){
            if(selectAllTypeSub == ''){
                selectAllTypeSub = type;
            }else if (selectAllTypeSub.indexOf(type) < 0) {
                selectAllTypeSub += ','+type;
            }
            size++;
        }else {
            if(selectAllTypeSub == type){
                selectAllTypeSub = '';
            }else if(selectAllTypeSub.indexOf(type) >= 0){
                var arr = selectAllTypeSub.split(',');
                selectAllTypeSub = '';
                for(let i=0; i< arr.length;i++){
                    if(arr[i] != type && arr[i] != '' && arr[i] != null){
                        selectAllTypeSub += arr[i] + ',';
                    }
                }
                selectAllTypeSub = selectAllTypeSub.substr(0,selectAllTypeSub.length-1);
            }

            let containType = false;
            $.each($("[name='submissionNo']:checked"),function(){
                if(!containType){
                    if($('#typeValue'+ $(this).val()).val() == type){
                        containType = true;
                    }
                }
            });
            if(containType){
                if(selectAllTypeSub == ''){
                    selectAllTypeSub = type;
                }else {
                    selectAllTypeSub += ',' + type;
                }
            }
            size--;
        }
        $("#selectAllTypeSub").val(selectAllTypeSub);
        $("#needValidatorSize").val(size);
        if(size == 1){
            $('#ds-deleteDraft').removeClass("disabled");
            var rfcType = $("#rfcType").val();
            console.log(rfcType);
            console.log(selectAllTypeSub);
            /*if(selectAllTypeSub.indexOf('DSCL_012') < 0 ){*/
            if(rfcType.indexOf(selectAllTypeSub)>0){
                $('#ds-amend').removeClass("disabled");
            }else {
                $('#ds-amend').addClass("disabled");
            }
            //$('#ds-withdraw').removeClass("disabled");
            $('#ds-unlock').removeClass("disabled");
        }else if (size <= 0) {
            $('#ds-deleteDraft').addClass("disabled");
            $('#ds-amend').addClass("disabled");
            //$('#ds-withdraw').addClass("disabled");
            $('#ds-unlock').addClass("disabled");
        }else if(size>1){
            $('#ds-amend').addClass("disabled");
            if(selectAllTypeSub.indexOf(',') > 0){
               // $('#ds-withdraw').addClass("disabled");
            }else {
               // $('#ds-withdraw').removeClass("disabled");
            }
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
        goToProcessForInbox("${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox")}");
    }
    function dssToAppPage(){
        goToProcessForInbox("${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox?initPage=initApp")}");
    }
    function dssToLicPage(){
        goToProcessForInbox("${pageContext.request.contextPath.concat("/eservice/INTERNET/MohInternetInbox?initPage=initLic")}");
    }


    $('#ds-deleteDraft').click(function (){
        //get first
       if($("[name='submissionNo']:checked").val() != null){
           let canDraft = true;
           $.each($("[name='submissionNo']:checked"),function(){
             if(canDraft && $(this).val().indexOf('DS') <0){
                 canDraft = false;
             }
           })
           if(canDraft){
               $("#deleteDraftModal").modal('show');
           }else {
               $("#actionNoDraftDataDelete").modal('show');
           }
       } else {
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