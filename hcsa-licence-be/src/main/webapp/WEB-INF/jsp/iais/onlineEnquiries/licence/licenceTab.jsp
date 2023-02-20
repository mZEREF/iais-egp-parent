<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
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
                        <iais:code code="${licenceDto.status}"/>
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
    <%@include file="../../hcsaLicence/section/viewKeyRoles5_7.jsp" %>
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

    <div class="panel panel-default lic-document">

        <div class="panel-heading" id="headingInternalDocuments" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseInternalDocuments" aria-expanded="true"
                                       aria-controls="collapseInternalDocuments">
                MOH Internal Documents</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseInternalDocuments" role="tabpanel"
             aria-labelledby="headingInternalDocuments">
            <div class="panel-body">
                <div class="components">
                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" >Document Name</th>
                                <th scope="col" >File</th>
                                <th scope="col" >File Size</th>
                            </tr>
                            </thead>
                            <tbody id="tbodyFileListId">
                            <c:set var="isEmptyIntranetDocDtoList" value="true"/>
                            <c:if test="${not empty appIntranetDocDtoList}">
                                <c:forEach var="interalFile" items="${appIntranetDocDtoList}" varStatus="status">
                                    <c:set var="isEmptyIntranetDocDtoList" value="false"/>
                                </c:forEach>
                            </c:if>
                            <c:choose>
                                <c:when test="${isEmptyIntranetDocDtoList}">
                                    <tr>
                                        <td colspan="3" align="left">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="interalFile" items="${appIntranetDocDtoList}"
                                               varStatus="status">
                                            <tr>
                                                <td >
                                                    <p>
                                                        <c:choose>
                                                            <c:when test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_CHECK_LIST}">Letter Written to Licensee</c:when>
                                                            <c:when test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_PAST_INS_REPORT}">Inspection Report</c:when>
                                                            <c:otherwise><c:out value="${interalFile.docDesc}"/></c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </td>
                                                <td >
                                                    <p>
                                                        <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${interalFile.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(interalFile.docName, StandardCharsets.UTF_8.toString())}.${interalFile.docType}"
                                                           title="Download" class="downloadFile">
                                                            <span id="${interalFile.fileRepoId}Down">trueDown</span>
                                                        </a>
                                                        <a href="javascript:void(0);" onclick="doVerifyFileGo('${interalFile.fileRepoId}')">
                                                            <c:out value="${interalFile.docName}.${interalFile.docType}"/>
                                                        </a>
                                                    </p>
                                                </td>
                                                <td >
                                                    <p><c:out value="${interalFile.docSize}"/></p>
                                                </td>


                                            </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

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
           href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licence-Print"
        >Print Licence</a>
        <a class="btn btn-primary" href="#"
           onclick="licTabView('${MaskUtil.maskValue('payLicNo', licenceDto.licenceNo)}')">Payment Details</a>
    </iais:action>

</div>
<iais:confirm msg="GENERAL_ACK018"  needCancel="false" callBack="$('#supportReport').modal('hide');" popupOrder="supportReport" ></iais:confirm>

<script>
    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId":verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if(data != null ){
                    if(data.verify == 'N'){
                        $('#supportReport').modal('show');
                    }else {
                        $("#"+verify+"Down").click();
                    }
                    dismissWaiting();
                }
            }
        )
    }

    var licTabView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('preLicInfo');
        $('#mainForm').submit();
    }
</script>