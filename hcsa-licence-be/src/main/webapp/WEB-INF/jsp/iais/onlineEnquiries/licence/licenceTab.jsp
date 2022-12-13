<div class="col-md-12">
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
