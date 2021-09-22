<form class="" method="post" id="licForm" action=<%=process.runtime.continueURL()%>>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <div class="tab-search">
        <input type="hidden" name="lic_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="action_id_value" value="">
        <div id="clearBody">
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="licNoPath" style="text-align:left;margin-top: 1.5%">Search by Licence No.</label>
                        <div class="col-xs-9 col-md-9">
                            <input id="licNoPath" name="licNoPath" type="text" maxlength="24"
                                   value="${param.licNoPath}">
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="licType" style="text-align:left;margin-top: 1.5%">Service
                            Type:</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="licType" id="licType" options="licType" value="${param.licType}" firstOption="All" cssClass="serviceType"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 1.5%">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3" for="licStatus" style="text-align:left;margin-top: 1.5%">Licence
                            Status:</label>
                        <div class="col-xs-9 col-md-9">
                            <iais:select name="licStatus" id="licStatus" options="licStatus" value="${param.licStatus}" firstOption="All" cssClass="licStatus"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3 col-lg-3" for="fStartDate" style="text-align:left;margin-top: 1.5%">Licence Start
                            Date:</label>
                        <div class="col-xs-9 col-md-4 col-lg-4">
                            <iais:datePicker id="fStartDate" name="fStartDate" value="${param.fStartDate}"/>
                        </div>
                        <div class="col-xs-3 col-md-1 col-lg-1" style="margin-top: 1.5%">
                            <label>To</label>
                        </div>
                        <div class="col-xs-9 col-md-4 col-lg-4">
                            <iais:datePicker id="eStartDate" name="eStartDate" value="${param.eStartDate}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <span class="col-xs-3 col-md-3"></span>
                    <div class="col-md-9">
                        <span class="error-msg" style="padding: 0;font-size:1.5rem">${LDEM}</span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-xs-3 col-md-3 col-lg-3" style="text-align:left;margin-top: 1.5%">Licence Expiry
                            Date:</label>
                        <div class="col-xs-9 col-md-4 col-lg-4">
                            <iais:datePicker id="fExpiryDate" name="fExpiryDate" value="${param.fExpiryDate}"/>
                        </div>
                        <div class="col-xs-3 col-md-1 col-lg-1" style="margin-top: 1.5%">
                        <label>To</label>
                    </div>
                        <div class="col-xs-9 col-md-4 col-lg-4">
                            <iais:datePicker id="eExpiryDate" name="eExpiryDate" value="${param.eExpiryDate}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 30px;">
                <div class="col-md-12">
                    <span class="col-xs-3 col-md-3"></span>
                    <div class="col-md-9">
                        <span class="error-msg" style="width: 150%;font-size:1.5rem">${LEEM}</span>
                    </div>
                </div>
            </div>
            <div class="col-md-12">
                <div class="text-right">
                    <button type="button" class="btn btn-secondary" onclick="doClearLic()">Clear</button>
                    <button type="button" class="btn btn-primary" onclick="doSearchLic()">Search</button>
                </div>
            </div>
        </div>
        <iais:pagination param="licParam" result="licResult"/>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="display: none;"></th>
                        <iais:sortableHeader needSort="false" field="" value=" " style="width:1%;"/>
                        <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                             value="Licence No." style="width:15%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="SVC_NAME" value="Type" style="width:12%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="LIC_STATUS_DESC" value="Status" style="width:10%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="address" value="Mode of Service Delivery" style="width:22%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="START_DATE"
                                             value="Start Date" style="width:13%;" isFE="true"/>
                        <iais:sortableHeader needSort="true" field="EXPIRY_DATE"
                                             value="Expiry Date" style="width:13%;" isFE="true"/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty licResult.rows}">
                            <tr>
                                <td colspan="8">
                                    <iais:message key="GENERAL_ACK018" escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="licenceQuery" items="${licResult.rows}" varStatus="status">
                                <tr>
                                    <td style="display: none;">
                                        <p class="licId"><iais:mask name="action_id_value" value="${licenceQuery.id}"/></p>
                                    </td>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        <div class="form-check">
                                            <input class="form-check-input licenceCheck" id="licence1" type="checkbox"
                                                   name="licenceNo" value="licenId${status.index}" aria-invalid="false" <c:if test="${fn:contains(licence_err_list, licenceQuery.id)}">checked</c:if> onclick="licClick()">
                                            <label class="form-check-label" for="licence1"><span
                                                    class="check-square"></span>
                                            </label>
                                        </div>
                                    </td>
                                    <td>
                                        <a href="#" class="licToView" style="font-size: 16px">${licenceQuery.licenceNo}</a>
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
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-renew">Renew</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-cease">Cease</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-amend">Amend</a>
                            <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-appeal">Appeal</a>
                            <c:if test="${InterInboxDelegator_lic_print_flag == '1'}">
                                <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-print">Print</a>
                            </c:if>
                            <c:if test="${InterInboxDelegator_lic_print_flag == '0'}">
                                <a class="btn btn-primary disabled" href="javascript:void(0);" id="lic-print-only-show">Print</a>
                            </c:if>
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
    $(document).ready(function () {
        $('#draftByLicAppId').modal('hide');
        if('1' == '${isShow}'){
            $('#draftByLicAppId').modal('show');
        }
        $('#draftRenewByLicAppId').modal('hide');
        if('1' == '${isRenewShow}'){
            $('#draftRenewByLicAppId').modal('show');
        }
        $('#draftAppealByLicAppId').modal('hide');
        if('1' == '${isAppealShow}'){
            $('#draftAppealByLicAppId').modal('show');
        }
        $('#draftCeaseByLicAppId').modal('hide');
        if('1' == '${isCeaseShow}'){
            $('#draftCeaseByLicAppId').modal('show');
        }
        if('1'=='${isBundleShow}'){
            $('#bundleShow').modal('show');
        }
    });
    function cancel() {
        $('#draftByLicAppId').modal('hide');
        $('#draftRenewByLicAppId').modal('hide');
        $('#draftAppealByLicAppId').modal('hide');
        $('#draftCeaseByLicAppId').modal('hide');
    }
    $('#lic-amend').click(function () {
        doLicAmend();
    });
    function deleteAppealDraft() {
        $('#isNeedDelete').val('delete');
        doLicAppeal();
    }
    function deleteCeaseDraft() {
        $('#isNeedDelete').val('delete');
        doLicCease();
    }
    function deleteRfcDraft(){
        $('#isNeedDelete').val('delete');
        doLicAmend();
    }
    function deleteRenewDraft(){
        $('#isNeedDelete').val('delete');
        doLicRenew();
    }
    function bundleYes(){
        $('#bundle').val('yes');
        doLicRenew();
    }
    function bundleNo(){
        $('#bundle').val('no');
        doLicRenew();
    }
    $('#lic-renew').click(function () {
        doLicRenew();
    });
    $('#lic-cease').click(function () {
        doLicCease();
    });
    $('#lic-appeal').click(function () {
        doLicAppeal();
    });
    $('#lic-print').click(function () {
        doLicPrint();
    });

</script>