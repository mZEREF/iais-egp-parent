<div class="col-xs-12">
    <div class="tab-gp dashboard-tab">
        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
            <li id="InspectionReport" role="presentation" class="active">
                <a href="#tabInspectionReport" id="doInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
            </li>
            <li id="CertificationReport" role="presentation">
                <a href="#tabCertificationReport" id="doCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
            </li>
        </ul>
        <div class="tab-nav-mobile visible-xs visible-sm">
            <div class="swiper-wrapper" role="tablist">
                <div class="swiper-slide active">
                    <a href="#tabInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
                </div>
                <div class="swiper-slide">
                    <a href="#tabCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
                </div>
            </div>
        </div>
        <div class="tab-content">
            <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                <%@include file="inspectionReport.jsp"%>
            </div>
            <div class="tab-pane" id="tabCertificationReport" role="tabpanel">
                <%@include file="certificationDocumentsPage.jsp"%>
            </div>
        </div>
    </div>
</div>