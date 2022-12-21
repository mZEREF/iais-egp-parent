<div class="col-md-12">
    <div class="panel panel-default lic-content">

        <div class="panel-heading" id="headingLicence" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseLicence" aria-expanded="true"
                                       aria-controls="collapseLicence">
                Licence</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseLicence" role="tabpanel"
             aria-labelledby="headingLicence">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal min-row">
                <iais:row>
                    <iais:field width="5" value="Licence No."/>
                    <iais:value width="7" cssClass="col-md-7 licenceNo" display="true">
                        <c:out value="${licenceDto.licenceNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Service Name"/>
                    <iais:value width="7" cssClass="col-md-7 serviceName" display="true">
                        <c:out value="${licenceDto.svcName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Licence Period"/>
                    <iais:value width="7" cssClass="col-md-7 Period" display="true">
                        <fmt:formatDate
                                value="${licenceDto.startDate}"
                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                            value="${licenceDto.expiryDate}"
                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Licence Status"/>
                    <iais:value width="7" cssClass="col-md-7 status" display="true">
                        <c:out value="${licenceDto.status}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Licence Approved Date"/>
                    <iais:value width="7" cssClass="col-md-7 createdAt" display="true">
                        <fmt:formatDate
                                value="${licenceDto.createdAt}"
                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                    </iais:value>
                </iais:row>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../../application/view/previewLicensee.jsp" %>
    <%@include file="../../application/view/previewPremises.jsp" %>
    <%@include file="../../hcsaLicence/section/viewSpecialised.jsp" %>
    <%@include file="../../hcsaLicence/section/viewKeyRoles.jsp" %>
    <div class="panel panel-default svc-content">

        <div class="panel-heading" id="headingServiceInfo0" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseServiceInfo0" aria-expanded="true"
                                       aria-controls="collapseServiceInfo">
                Service Related Information - ${currentPreviewSvcInfo.serviceName}</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseServiceInfo0" role="tabpanel"
             aria-labelledby="headingServiceInfo0">
            <div class="panel-body">

                <%@include file="../../application/view/previewSvcInfo.jsp" %>
            </div>
        </div>
    </div>
    <div class="panel panel-default lic-History">

        <div class="panel-heading" id="headingLicenceHistory" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseLicenceHistory" aria-expanded="true"
                                       aria-controls="collapseLicenceHistory">
                Licence History</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseLicenceHistory" role="tabpanel"
             aria-labelledby="headingLicenceHistory">
            <div class="panel-body">
                <div class="components">
                    <div class="table-gp">
                        <table aria-describedby="" class="table table-responsive"
                               style="border-collapse:collapse;">
                            <thead>
                            <tr>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LICENCE_NO"
                                                     value="Licence No."/>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="START_DATE"
                                                     value="Licence Period"/>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LicSTATUS"
                                                     value="Licence Status"/>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="LICENSEE_NAME"
                                                     value="Licensee Name (Licensee ID No.)"/>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="BUSINESS_NAME"
                                                     value="Business Name"/>
                                <iais:sortableHeader needSort="false"
                                                     style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                     field="ADDRESS"
                                                     value="MOSD Address"/>

                            </tr>
                            </thead>
                            <tbody class="form-horizontal">
                            <c:forEach var="licence"
                                       items="${licenceHistoryList}"
                                       varStatus="status">
                                <tr>

                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        <c:if var="eqlicenceId" test="${licenceId ==licence.licenceId}">
                                            <c:out value="${licence.licenceNo}"/>
                                        </c:if>
                                        <c:if test="${!eqlicenceId}">
                                            <a href="#" onclick="licTabView('${MaskUtil.maskValue('licenceId', licence.licenceId)}')">${licence.licenceNo}</a>
                                        </c:if>

                                    </td>
                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">Licence Period</p>
                                        <fmt:formatDate
                                                value="${licence.startDate}"
                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                            value="${licence.expiryDate}"
                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                    </td>
                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">Licence Status</p>
                                        <iais:code code="${licence.licenceStatus}"/>
                                    </td>
                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">Licensee Name (Licensee ID No.)</p>
                                        <c:out value="${licence.licenseeIdName} (${licence.licenseeIdNo})"/>
                                    </td>

                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">Business
                                            Name</p>
                                        <c:out value="${licence.businessName}"/>
                                    </td>

                                    <td style="vertical-align:middle;">
                                        <p class="visible-xs visible-sm table-row-title">MOSD
                                            Address</p>
                                        <c:out value="${licence.mosdAddress}"/>
                                    </td>


                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <iais:action style="text-align:right;">
        <a class="btn btn-primary"
           href="#">Print Licence</a>
        <a class="btn btn-primary"
           href="#">Payment Details</a>
    </iais:action>

</div>

<script>


    var licTabView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('preLicInfo');
        $('#mainForm').submit();
    }
</script>