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
                        <label class="col-xs-3 col-md-3" for="licNoPath" style="text-align:left;margin-top: 1.5%">Search
                            by Licence No or Part of:</label>
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
                            <iais:select name="licType" id="licType" options="licType" value="${param.licType}" cssClass="serviceType"/>
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
                            <iais:select name="licStatus" id="licStatus" options="licStatus" value="${param.licStatus}" cssClass="licStatus"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iais:value>
                        <label class="col-md-3" for="fStartDate" style="text-align:left;margin-top: 1.5%">Licence Start
                            Date:</label>
                        <div class="col-md-4">
                            <iais:datePicker id="fStartDate" name="fStartDate" value="${param.fStartDate}"/>
                        </div>
                        <div class="col-xs-1 col-md-1" style="margin-top: 1.5%">
                            <label>To</label>
                        </div>
                        <div class="col-md-4">
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
                        <label class="col-xs-3 col-md-3" style="text-align:left;margin-top: 1.5%">Licence Expiry
                            Date:</label>
                        <div class="col-xs-4 col-md-4">
                            <iais:datePicker id="fExpiryDate" name="fExpiryDate" value="${param.fExpiryDate}"/>
                        </div>
                    </iais:value>
                    <div class="col-xs-1 col-md-1" style="margin-top: 1.5%">
                        <label>To</label>
                    </div>
                    <iais:value>
                        <div class="col-xs-4 col-md-4">
                            <iais:datePicker id="eExpiryDate" name="eExpiryDate" value="${param.eExpiryDate}"/>
                        </div>
                    </iais:value>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <span class="col-xs-3 col-md-3"></span>
                    <div class="col-md-5">
                        <span class="error-msg" style="width: 150%;position: absolute;font-size:1.5rem">${LEEM}</span>
                    </div>
                    <div class="col-md-4">
                        <div class="text-right">
                            <button type="button" class="btn btn-secondary" onclick="doClearLic()">Clear</button>
                            <button type="button" class="btn btn-primary" onclick="doSearchLic()">Search</button>
                        </div>
                    </div >
                </div>
            </div>
        </div>
        <iais:pagination param="licParam" result="licResult"/>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader needSort="false" field="" value=" " />
                        <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                             value="Licence No."/>
                        <iais:sortableHeader needSort="true" field="SVC_NAME" value="Type"/>
                        <iais:sortableHeader needSort="true" field="LIC_STATUS_DESC" value="Status" style="width:9%;"/>
                        <iais:sortableHeader needSort="false" field="PREMISE" value="Premises" style="width:30%;"/>
                        <iais:sortableHeader needSort="true" field="START_DATE"
                                             value="Start Date"/>
                        <iais:sortableHeader needSort="true" field="EXPIRY_DATE"
                                             value="Expiry Date"/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty licResult.rows}">
                            <tr>
                                <td colspan="6">
                                    <iais:message key="ACK018" escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="licenceQuery" items="${licResult.rows}" varStatus="status">
                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        <div class="form-check">
                                            <c:if test="${licenceQuery.status == 'LICEST001'}">
                                                <input class="form-check-input licenceCheck" id="licence1" type="checkbox"
                                                       name="licenceNo" value="licenId${status.index}" aria-invalid="false" <c:if test="${fn:contains(licence_err_list, licenceQuery.id)}">checked</c:if> onclick="licClick()">
                                                <label class="form-check-label" for="licence1"><span
                                                        class="check-square"></span>
                                                </label>
                                            </c:if>
                                        </div>
                                    </td>
                                    <td hidden>
                                        <p class="licId"><iais:mask name="action_id_value" value="${licenceQuery.id}"/></p>
                                    </td>
                                    <td>
                                        <c:if test="${licenceQuery.status == 'LICEST001'}">
                                            <a href="#" class="licToView">${licenceQuery.licenceNo}</a>
                                            <input type="hidden" name="licenId${status.index}"
                                                   value="<iais:mask name= "licenId${status.index}" value="${licenceQuery.id}"/>"/>
                                        </c:if>
                                        <c:if test="${licenceQuery.status != 'LICEST001'}">
                                            <p href="#">${licenceQuery.licenceNo}</p>
                                            <input type="hidden" name="licenId${status.index}" value="<iais:mask name= "licenId${status.index}" value="${licenceQuery.id}"/>"/>
                                        </c:if>
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
                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
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
                <div class="modal fade" id="isRenewedModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:60%; overflow: visible;bottom: inherit;right: inherit;">
                    <div class="modal-dialog" role="document" style="width: 760px;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            </div>
                            <div class="modal-body" style="text-align: center">
                                <div class="row">
                                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;">${LAEM}</span></div>
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
                <div class="modal fade" id="ceasedModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:60%; overflow: visible;bottom: inherit;right: inherit;">
                    <div class="modal-dialog" role="document" style="width: 760px;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            </div>
                            <div class="modal-body" style="text-align: center">
                                <div class="row">
                                    <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;">
                                        There is a pending application for this licence, please withdraw the application before proceeding with cessation
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
                            <a class="btn btn-primary disabled" href="#" id="lic-renew">Renew</a>
                            <a class="btn btn-primary disabled" href="#" id="lic-cease">Cease</a>
                            <a class="btn btn-primary disabled" href="#" id="lic-amend">Amend</a>
                            <a class="btn btn-primary disabled" href="#" id="lic-appeal">Appeal</a>
                            <a class="btn btn-primary disabled" href="#" id="lic-print">Print</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    $('#lic-amend').click(function () {
        doLicAmend();
    });

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