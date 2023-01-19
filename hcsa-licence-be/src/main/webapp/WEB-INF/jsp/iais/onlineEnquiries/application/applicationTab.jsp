<div class="col-md-12">
    <div class="panel panel-default lic-content">

        <div class="panel-heading" id="headingApplication" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseApplication" aria-expanded="true"
                                       aria-controls="collapseApplication">
                Application Details</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseApplication" role="tabpanel"
             aria-labelledby="headingApplication">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal min-row">
                    <iais:row>
                        <iais:field width="5" value="Application Type"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.applicationType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Application No. (Overall)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.applicationNoOverAll}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Application No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.applicationDto.applicationNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Service Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.serviceType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Application Date"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.submissionDate}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Working Group"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${currTask.wkGrpId}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Application Status"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.currentStatus}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Auto Approved"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if var="rfc" test="${applicationViewDto.applicationDto.applicationType == 'APTY005'}">
                                <c:out value="${applicationViewDto.applicationGroupDto.autoApprove ? 'Yes':'No'}"/>
                            </c:if>
                            <c:if test="${!rfc}">-</c:if>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Assigned Officer"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${currTask.userId}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Submitted By"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${applicationViewDto.applicationGroupDto.submitBy}"/>
                        </iais:value>
                    </iais:row>


                    <iais:row>
                        <iais:field width="5" value="Licence No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${licenceDto.licenceNo}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Licence Period"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <fmt:formatDate
                                    value="${licenceDto.startDate}"
                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                value="${licenceDto.expiryDate}"
                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Licence Status"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:code code="${licenceDto.status}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Application Approved Date"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
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
    <%@include file="../../hcsaLicence/section/viewDeclaration.jsp" %>
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
                            <c:if test="${not empty applicationViewDto.appIntranetDocDtoList}">
                                <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}" varStatus="status">
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
                                    <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}"
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


    var jumpPayPage = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $('#mainForm').submit();
    }
</script>