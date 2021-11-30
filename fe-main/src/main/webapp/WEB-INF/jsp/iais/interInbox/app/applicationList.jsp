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
        <input type="hidden" name="action_status_value" value="">
        <input type="hidden" name="action_self_value" value="">
        <input type="hidden" name="action_type_value" value="">
        <input type="hidden" value="" id="isNeedDelete" name="isNeedDelete">
        <div id="clearBody">
            <div class="row">
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-5" for="appNoPath" style="margin-top:1%;">Search by Application No.
                            or Part of:</label>
                        <div class="col-xs-12 col-md-7">
                            <input id="appNoPath" name="appNoPath" type="text" maxlength="20"
                                   value="${param.appNoPath}">
                        </div>
                    </iais:value>
                </div>
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-4" for="appServiceType" style="margin-top:3%;">Service
                            Type:</label>
                        <div class="col-xs-12 col-md-8">
                            <iais:select name="appServiceType" id="appServiceType" cssClass="appServiceType"
                                         options="appServiceType" firstOption="All" value="${param.appServiceType}" needSort="true"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row" style="margin-bottom: 14px">
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-5" for="appTypeSelect" style="margin-top:3%;">Application
                            Type:</label>
                        <div class="col-xs-12 col-md-7">
                            <iais:select name="appTypeSelect" id="appTypeSelect" cssClass="appTypeSelect"
                                         options="appTypeSelect" firstOption="All" value="${param.appTypeSelect}"/>
                        </div>
                    </iais:value>
                </div>
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-4" for="appStatusSelect" style="margin-top:3%;">Application
                            Status:</label>
                        <div class="col-xs-12 col-md-8">
                            <%String appStatusSelect = request.getParameter("appStatusSelect");%>
                            <iais:select options="appStatusSelect" cssClass="appStatusSelect" name="appStatusSelect"
                                         id="appStatusSelect" firstOption="All" value="${param.appStatusSelect}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-5" for="esd" style="margin-top:3%;">Date Submitted:</label>
                        <div class="col-xs-12 col-md-7">

                            <iais:datePicker id="esd" name="esd" value="${param.esd}" onchange="LimitDeadline(this.value)"/>
                        </div>
                    </iais:value>
                </div>
                <div class="col-xs-12 col-md-6">
                    <iais:value>
                        <label class="col-xs-12 col-md-4" for="appStatusSelect" style="margin-top:3%;">To</label>
                        <div class="col-xs-12 col-md-8">
                            <iais:datePicker id="eed" name="eed" value="${param.eed}"/>
                        </div>
                    </iais:value>
                </div>
                <c:if test="${!empty ADEM}">
                    <div class="row" style="margin-bottom: 37px;">
                        <div class="col-xs-12 col-md-6">
                            <span class="col-xs-12 col-md-5"></span>
                            <div class="col-xs-12 col-md-7">
                                <span class="error-msg" style="width: 150%;position: absolute;">${ADEM}</span>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="col-md-12">
            <div class="text-right text-center-mobile" style="margin-right:3%">
                <button type="button" class="btn btn-secondary" onclick="doAppClear()">Clear</button>
                <button type="button" class="btn btn-primary" onclick="doSearchApp()">Search</button>
            </div>
        </div>
        <br> <br>
        <iais:pagination param="appParam" result="appResult"/>
    </form>
</div>
<div class="row" style="padding-bottom: 115px;">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" style="display: none"></th>
                    <iais:sortableHeader needSort="true" field="APPLICATION_NO"
                                         value="Application No." isFE="true"  style="width:20%"/>
                    <iais:sortableHeader needSort="true" field="APP_TYPE_DESC" value="Type" isFE="true"  style="width:13%"/>
                    <iais:sortableHeader needSort="true" field="code" value="Service" isFE="true" style="width:19%"/>
                    <iais:sortableHeader needSort="true" field="STATUS_DESC" value="Status" isFE="true"  style="width:13%"/>
                    <iais:sortableHeader needSort="true" field="CREATED_DT"
                                         value="Submission Date" style="width:18%" isFE="true"/>
                    <iais:sortableHeader needSort="false" field=""
                                         value="Actions" style="width:17%" />
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty appResult.rows}">
                        <tr>
                            <td colspan="6">
                                <iais:message key="GENERAL_ACK018" escape="true"/>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${appResult.rows}" var="app" varStatus="status">
                            <tr>
                                <td style="display: none">
                                    <p class="appId"><iais:mask name="action_id_value" value="${app.id}"/></p>
                                    <p class="appSelfFlag">${app.selfAssmtFlag}</p>
                                     <p class="appGroupId">${app.appGrpId}</p>
                                     <p class="appPmtStatus">${app.pmtStatus}</p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                    <p>
                                        <c:choose>
                                        <c:when test="${app.status == 'APST093'}">
                                                <p>${app.applicationNo}</p>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#"
                                               <c:if test="${app.status == 'APST008' || app.status =='APST060'}">class="appdraftNo"</c:if>
                                               <c:if test="${app.status != 'APST008'}">class="appNo"</c:if>>${app.applicationNo}</a>
                                        </c:otherwise>
                                        </c:choose>
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
                                    <p class="appStatus"><iais:code code="${app.status}"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Submission Date</p>
                                    <p style="width: 153px"><fmt:formatDate value="${app.createdAt}"
                                                                            pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                </td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Actions</p>
                                    <c:choose>
                                        <c:when test="${app.applicationType == 'APTY002' || app.applicationType == 'APTY004' || app.applicationType == 'APTY005'}">
                                            <c:if test="${app.status == 'APST005' || app.status == 'APST006'
                                                       || app.status =='APST075' ||  app.status == 'APST050'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                        <c:if test="${empty app.miscAppId}"><option value="Appeal">Appeal</option></c:if>
                                                        <c:if test="${app.selfAssmtFlag == 0 || app.selfAssmtFlag == 2}">
                                                            <option value="Assessment">Assessment</option>
                                                        </c:if>
                                                        <c:if test="${app.pmtStatus == 'PMT06'}">
                                                            <option value="Make Payment">Make Payment</option>
                                                        </c:if>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST084'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Make Payment">Make Payment</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST008'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Continue">Continue</option>
                                                    <option value="Delete">Delete</option>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST007'}">
                                                <select id="appDoSelectActive" <c:if test="${app.autoApprove}">disabled</c:if> class="appDoSelectActive" name="appDoSelectActive">
                                                    <c:if test="${app.autoApprove}">
                                                        <option>N/A</option>
                                                    </c:if>
                                                        <%--                                                     <c:if test="${app.autoApprove}">--%>
                                                        <%--                                                    <c:if test="${empty app.miscAppId}"><option value="Appeal">Appeal</option></c:if>--%>
                                                        <%--                                                    </c:if>--%>
                                                    <c:if test="${empty app.autoApprove || !app.autoApprove}">
                                                        <option value="" selected>Select</option>
                                                        <c:if test="${app.canInspection}">
                                                            <c:if test="${app.selfAssmtFlag == 0 || app.selfAssmtFlag == 2}">
                                                                <option value="Assessment">Assessment</option>
                                                            </c:if>
                                                            <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                        </c:if>
                                                        <c:if test="${app.pmtStatus == 'PMT06'}">
                                                            <option value="Make Payment">Make Payment</option>
                                                        </c:if>
                                                        <%--<c:if test="${app.canRecall}">--%>
                                                        <%--<option value="Recall">Recall</option>--%>
                                                        <%--</c:if>--%>
                                                        <option value="Withdraw">Withdraw</option>
                                                    </c:if>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST028' || app.status == 'APST003' || app.status == 'APST001'
                                                       || app.status == 'APST002' || app.status == 'APST037' || app.status == 'APST029'
                                                       || app.status == 'APST023' || app.status == 'APST024' || app.status == 'APST013'
                                                       || app.status == 'APST014' || app.status == 'APST062' || app.status == 'APST012'
                                                       || app.status == 'APST010' || app.status == 'APST027' || app.status == 'APST031'
                                                       || app.status == 'APST067' || app.status == 'APST053' || app.status == 'APST054'
                                                       || app.status == 'APST068' || app.status == 'APST063' || app.status == 'APST064'
                                                       || app.status == 'APST065' || app.status == 'APST066' || app.status == 'APST077'
                                                       || app.status == 'APST090' || app.status == 'APST091' || app.status == 'APST069'
                                                       || app.status == 'APST071' || app.status == 'APST034' || app.status == 'APST019'
                                                       || app.status == 'APST020' || app.status == 'APST022' || app.status == 'APST032'
                                                       || app.status == 'APST048' || app.status == 'APST049' || app.status == 'APST039'
                                                       || app.status == 'APST040' || app.status == 'APST011' || app.status == 'APST004'
                                                       || app.status =='APST033'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <c:if test="${app.canInspection}">
                                                    <c:if test="${app.selfAssmtFlag == 0 || app.selfAssmtFlag == 2}">
                                                        <option value="Assessment">Assessment</option>
                                                    </c:if>
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST060'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Continue">Continue</option>
                                                    <c:if test="${app.canInspection}">
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST045' || app.status == 'APST093'}">
                                                <select disabled>
                                                    <option>N/A</option>
                                                </select>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${app.applicationType == 'APTY009'}">
                                            <c:if test="${app.status != 'APST005' && app.status != 'APST006' && app.status != 'APST050' && app.canInspection}">
                                            <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                <option value="" selected>Select</option>
                                                <c:if test="${app.selfAssmtFlag == 0 || app.selfAssmtFlag == 2}">
                                                    <option value="Assessment">Assessment</option>
                                                </c:if>
                                                    <option value="Inspection">Indicate Preferred Inspection Date</option>
                                            </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST005' || app.status == 'APST006' || app.status == 'APST050' || !app.canInspection }">
                                            <select disabled>
                                                <option>N/A</option>
                                            </select>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${app.applicationType == 'APTY001'}">
                                            <c:if test="${app.pmtStatus == 'PMT06' || app.status == 'APST084'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Make Payment">Make Payment</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST008'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Continue">Continue</option>
                                                    <option value="Delete">Delete</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST032' || (app.pmtStatus == 'PMT06' || app.status == 'APST084')}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <c:if test="${app.canInspection}">
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <%--<c:if test="${app.canRecall}">--%>
                                                        <%--<option value="Recall">Recall</option>--%>
                                                    <%--</c:if>--%>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST060'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Continue">Continue</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST002'
                                                       || app.status == 'APST012' || app.status == 'APST038' || app.status == 'APST010'
                                                       || app.status == 'APST011' || app.status == 'APST023' || app.status == 'APST027'
                                                       || app.status == 'APST024' || app.status == 'APST021' || app.status == 'APST033'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST007'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <%--<c:if test="${app.canRecall}">--%>
                                                        <%--<option value="Recall">Recall</option>--%>
                                                    <%--</c:if>--%>
                                                    <c:if test="${app.canInspection}">
                                                        <option value="Assessment">Assessment</option>
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>

                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST005'
                                            || app.status == 'APST006' || app.status == 'APST045' || app.status == 'APST093'
                                            || app.status == 'APST050'}">
                                                <select disabled>
                                                    <option>N/A</option>
                                                </select>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${app.applicationType == 'APTY006'}">
                                            <c:if test="${app.status == 'APST005' || app.status == 'APST006'
                                            || app.status == 'APST050' || app.status == 'APST002'
                                            || app.status == 'APST011' || app.status == 'APST012'
                                            || app.status == 'APST090' || app.status == 'APST092'
                                            || app.status == 'APST092' || app.status == 'APST024'
                                            || app.status == 'APST023' }">
                                                <select disabled>
                                                    <option>N/A</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST007'}">
                                                    <c:if test="${app.canInspection}">
                                                        <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                            <option value="" selected>Select</option>
                                                            <option value="Assessment">Assessment</option>
                                                            <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                        </select>
                                                    </c:if>
                                                    <c:if test="${ !app.canInspection}">
                                                     <select disabled>
                                                    <option>N/A</option>
                                                     </select>
                                                     </c:if>
                                            </c:if>

                                            <c:if test="${app.status == 'APST084'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <option value="Make Payment">Make Payment</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST032'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <c:if test="${app.canInspection}">
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <%--<c:if test="${app.canRecall}">--%>
                                                        <%--<option value="Recall">Recall</option>--%>
                                                    <%--</c:if>--%>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>
                                                </select>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${app.applicationType == 'APTY008'}">
                                            <c:if test="${app.status == 'APST005'||app.status == 'APST075'
                                                      || app.status == 'APST076' || app.status == 'APST050'
                                                      || app.status == 'APST011'
                                                      || (app.pmtStatus == 'PMT06' || app.status == 'APST084')}">
                                                <select disabled>
                                                    <option>N/A</option>
                                                </select>
                                            </c:if>
                                            <c:if test="${app.status == 'APST007'}">
                                                <c:if test="${app.canInspection}">
                                                    <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                        <option value="" selected>Select</option>
                                                        <option value="Assessment">Assessment</option>
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </select>
                                                </c:if>
                                                <c:if test="${ !app.canInspection}">
                                                    <select disabled>
                                                        <option>N/A</option>
                                                    </select>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${app.status == 'APST032'}">
                                                <select id="appDoSelectActive" class="appDoSelectActive" name="appDoSelectActive">
                                                    <option value="" selected>Select</option>
                                                    <c:if test="${app.canInspection}">
                                                        <option value="Inspection">Indicate Preferred Inspection Date</option>
                                                    </c:if>
                                                    <%--<c:if test="${app.canRecall}">--%>
                                                        <%--<option value="Recall">Recall</option>--%>
                                                    <%--</c:if>--%>
                                                    <c:if test="${app.pmtStatus == 'PMT06'}">
                                                        <option value="Make Payment">Make Payment</option>
                                                    </c:if>
                                                    <option value="Withdraw">Withdraw</option>
                                                </select>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <select disabled>
                                                <option>N/A</option>
                                            </select>
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
            <div class="modal fade" id="isAppealModal" role="dialog" aria-labelledby="myModalLabel" >
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
<%--                        <div class="modal-header">--%>
<%--                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                        </div>--%>
                        <div class="modal-body" style="text-align: center;">
                            <div class="row">
                                <div class="col-md-12"><span style="font-size: 2rem;"> ${ARR} </span></div>
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
            <iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftAppealAppealByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteAppealAppealDraft()"/>
           <input type="hidden" name="appealApplication" id="appealApplication" value="${appealApplication}">
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
        $('#draftAppealAppealByLicAppId').modal('hide');
        if('1' == '${isAppealApplicationShow}'){
            $('#draftAppealAppealByLicAppId').modal('show');
        }

        $('#deleteDraftMessage').find('div.modal-body').find('div.row div').css('width','70%');
    });

    function delDraftYesBtn() {
        $('#deleteDraftModal').modal('hide');
        submit('appDoDelete');
        showWaiting();
    }
    function deleteAppealAppealDraft() {
        $('#isNeedDelete').val('delete');
        showWaiting();
        $("[name='action_id_value']").val($('#appealApplication').val());
        submit("appDoAppeal");
    }

    function delDraftCancelBtn() {
        $('#deleteDraftModal').modal('hide');
    }

    function delDraftMsgYesBtn() {
        $('#deleteDraftMessage').modal('hide');
    }


</script>