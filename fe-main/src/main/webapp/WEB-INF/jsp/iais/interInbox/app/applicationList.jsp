<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<div class="tab-search">
    <form class="" method="post" id="appForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="app_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="action_no_value" value="">
        <input type="hidden" name="action_grp_value" value="">
        <input type="hidden" name="action_id_value" value="">
        <input type="hidden" name="action_type_value" value="">
        <div id="clearBody">
            <div class="row">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="appNoPath" style="margin-top:1%;">Search by Application No
                            or Part of:</label>
                        <div class="col-xs-7 col-md-7">
                            <input id="appNoPath" name="appNoPath" type="text" maxlength="20"
                                   value="${param.appNoPath}">
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appServiceType" style="margin-top:3%;">Service
                            Type:</label>
                        <div class="col-xs-8 col-md-8">
                            <iais:select name="appServiceType" id="appServiceType" cssClass="appServiceType"
                                         options="appServiceType" value="${param.appServiceType}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="appTypeSelect" style="margin-top:3%;">Application
                            Type:</label>
                        <div class="col-xs-7 col-md-7">
                            <iais:select name="appTypeSelect" id="appTypeSelect" cssClass="appTypeSelect"
                                         options="appTypeSelect" value="${param.appTypeSelect}"/>
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appStatusSelect" style="margin-top:3%;">Application
                            Status:</label>
                        <div class="col-xs-8 col-md-8">
                            <%String appStatusSelect = request.getParameter("appStatusSelect");%>
                            <iais:select options="appStatusSelect" cssClass="appStatusSelect" name="appStatusSelect"
                                         id="appStatusSelect" value="${param.appStatusSelect}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-5 col-md-5" for="esd" style="margin-top:3%;">Date Submitted:</label>
                        <div class="col-xs-7 col-md-7">

                            <iais:datePicker id="esd" name="esd" value="${param.esd}" onchange="LimitDeadline(this.value)"/>
                        </div>
                    </iais:value>
                </div>
                <div class="col-md-6">
                    <iais:value>
                        <label class="col-xs-4 col-md-4" for="appStatusSelect" style="margin-top:3%;">To</label>
                        <div class="col-xs-8 col-md-8">
                            <iais:datePicker id="eed" name="eed" value="${param.eed}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <span class="col-xs-5 col-md-5"></span>
                <div class="col-md-7">
                    <span class="error-msg" style="width: 150%;position: absolute;">${ADEM}</span>
                </div>
            </div>
            <div class="text-right text-center-mobile" style="margin-right:3%">
                <button type="button" class="btn btn-secondary" onclick="doAppClear()">Clear</button>
                <button type="button" class="btn btn-primary" onclick="doSearchApp()">Search</button>
            </div>
        </div>
        <iais:pagination param="appParam" result="appResult"/>
    </form>
</div>
<div class="row" style="padding-bottom: 40px;">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="APPLICATION_NO"
                                         value="Application No."/>
                    <iais:sortableHeader needSort="true" field="APP_TYPE" value="Type"/>
                    <iais:sortableHeader needSort="true" field="SERVICE_ID" value="Service"/>
                    <iais:sortableHeader needSort="true" field="STATUS" value="Status"/>
                    <iais:sortableHeader needSort="true" field="CREATED_DT"
                                         value="Submission Date"/>
                    <th style="width:15%">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty appResult.rows}">
                        <tr>
                            <td colspan="6">
                                <iais:message key="ACK018" escape="true"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${appResult.rows}" var="app" varStatus="status">
                            <tr>
                                <td hidden>
                                    <p class="appId"><iais:mask name="action_id_value" value="${app.id}"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                    <p><a href="#"
                                          <c:if test="${app.status == 'APST008'}">class="appdraftNo"</c:if>
                                          <c:if test="${app.status != 'APST008'}">class="appNo"</c:if>
                                    >${app.applicationNo}</a>
                                    </p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Type</p>
                                    <p class="apptype"><iais:code code="${app.applicationType}"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Service</p>
                                    <p>${app.serviceId}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Status</p>
                                    <p><iais:code code="${app.status}"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                    <p style="width: 153px"><fmt:formatDate value="${app.createdAt}"
                                                                            pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title" for="appAction">Actions</p>
                                    <c:choose>
                                        <c:when test="${app.status == 'APST008'}">
                                            <iais:select name="draftAction" cssClass="draftAction" id="draftAction" options="selectDraftApplication" firstOption="Select"/>
                                        </c:when>
                                        <c:when test="${app.status == 'APST038' || (app.status == 'APST007' && app.applicationType == 'APTY006') || (app.status == 'APST007' && app.applicationType == 'APTY001')}">
                                            <p>N/A</p>
                                        </c:when>
                                        <c:when test="${app.status == 'APST006' || app.status == 'APST005'}">
                                            <iais:select name="appAoRAction" cssClass="appAoRAction" id="appAoRAction" options="selectApproveOrRejectSelectList" firstOption="Select"/>
                                        </c:when>
                                        <c:when test="${app.status == 'APST060'}">
                                            <iais:select name="appRecalledAction" cssClass="appRecalledAction" id="appRecalledAction" options="selectRecalledSelectList" firstOption="Select"/>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:select name="appAction" id="appAction" cssClass="appAction" options="selectApplication" firstOption="Select" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <!-- Modal -->
            <div class="modal fade" id="isAppealModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <div class="row">
                                <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;"> ${ARR} </span></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <iais:confirm msg="${delDraftConfMsg}" needFungDuoJi="false" popupOrder="deleteDraftModal" callBack="delDraftCancelBtn()" title=" " cancelFunc="delDraftYesBtn()" cancelBtnDesc="OK" yesBtnDesc="Cancel" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"  />
            <iais:confirm msg="${delDraftAckMsg}" needFungDuoJi="false" popupOrder="deleteDraftMessage"  title=" " callBack="delDraftMsgYesBtn()"  needCancel="false" />
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#deleteDraftModal').modal('hide');
        $('#deleteDraftMessage').modal('hide');
        if('1' == '${needDelDraftMsg}'){
            $('#deleteDraftMessage').modal('show');
        }
    });

    function delDraftYesBtn() {
        $('#deleteDraftModal').modal('hide');
        submit('appDoDelete');
        showWaiting();
    }

    function delDraftCancelBtn() {
        $('#deleteDraftModal').modal('hide');
    }

    function delDraftMsgYesBtn() {
        $('#deleteDraftMessage').modal('hide');
    }


</script>