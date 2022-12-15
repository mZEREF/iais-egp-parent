<div class="col-md-12">
    <div class="panel panel-default lic-content">

        <div class="panel-heading" id="headingLicence" role="tab">
            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                       href="#collapseLicence" aria-expanded="true"
                                       aria-controls="collapseServiceInfo">
                Licence</a></h4>
        </div>

        <div class=" panel-collapse collapse" id="collapseLicence" role="tabpanel"
             aria-labelledby="headingLicence">
            <div class="panel-body">

                <iais:row>
                    <iais:field width="5" value="Licence No."/>
                    <iais:value width="7" cssClass="col-md-7 licenceNo" display="true">
                        <c:out value="${licenceDto.licenceNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Service Name"/>
                    <iais:value width="7" cssClass="col-md-7 serviceName" display="true">
                        <c:out value="${licenceDto.svcName}" />
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
                        <c:out value="${licenceDto.status}" />
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
    <%@include file="../../application/view/previewLicensee.jsp" %>
    <%@include file="../../application/view/previewPremises.jsp" %>
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

</div>
