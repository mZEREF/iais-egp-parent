<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-10">
                                            <p>Please note that you will not be able to pay for this application if you have not provided the mandatory information and documents.</p>
                                        </div>
                                        <div class="col-xs-12 col-md-2 text-right">
                                            <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <div class="panel panel-default">
                                                    <div class="panel-heading <c:if test="${Msg.premiss==null}">completed </c:if> <c:if test="${Msg.premiss!=null}">incompleted </c:if> " id="headingPremise" role="tab">
                                                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
                                                    </div>
                                                    <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                                                        <div class="panel-body">
                                                            <p class="text-right"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                                                            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                                                            <div class="panel-main-content">
                                                                <div class="preview-info">
                                                                    <p><strong>Premises ${status.index+1}</strong></p>
                                                                    <p>${appGrpPremDto.premisesType}: ${appGrpPremDto.address}</p>
                                                                    <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                                                        <p>Vehicle No: ${appGrpPremDto.conveyanceVehicleNo}</p>
                                                                    </c:if>
                                                                </div>
                                                            </div>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-heading <c:if test="${Msg.document==null}">completed </c:if>  <c:if test="${Msg.document!=null}">incompleted </c:if>" id="headingOne" role="tab">
                                                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
                                                    </div>
                                                    <div class="panel-collapse collapse " id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                                                        <div class="panel-body">
                                                            <p class="text-right mb-0"><a href="#" id="docEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                                                            <div class="elemClass-1561088919456">
                                                                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                                                                    <div class="control-set-font control-font-header section-header">
                                                                        <h2 class="summary-header">Uploaded Documents
                                                                        </h2>
                                                                    </div>
                                                                    <div class="pop-up">
                                                                        <div class="pop-up-body">
                                                                            <c:forEach var="appGrpPrimaryDocDto" items="${AppSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                                                                <div class="content-body fileUploadContainer">
                                                                                    <div class="field col-sm-4 control-label formtext"><label>Docment1 for Premise1:</label></div>
                                                                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                                                                    <div class="control col-sm-5">
                                                                                      <div class="fileList "><span class="filename server-site" id="130"><a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">${appGrpPrimaryDocDto.docName}</a> (${appGrpPrimaryDocDto.docSize} KB)</span></div>
                                                                                    </div>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status" >
                                                    <div class="panel panel-default svc-content">

                                                        <div class="panel-heading <c:if test="${fn:contains(Msg.serviceId,hcsaServiceDto.id)}">incompleted</c:if> <c:if test="${fn:contains(Msg.serviceId,hcsaServiceDto.id)==false}">completed</c:if>  "  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title"><a class="svc-pannel-collapse"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information -- ${hcsaServiceDto.svcName}</a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo${status.index}" role="tabpanel" aria-labelledby="headingServiceInfo${status.index}">
                                                            <div class="panel-body">
                                                                <p class="text-right mb-0"><a href="application-service-related-clinical-lab-lab-discipline.html"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                                                                <iframe  class="svc-iframe" title="" src="${pageContext.request.contextPath}/eservice/INTERNET/MohServiceRelatedInformation?crud_action_type_form_value=prepareView&svcId=${hcsaServiceDto.id}" id="elemId-${status.index}"  width="100%" height="100%" ></iframe> <!--scrolling="no" scrollbar="no" -->
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" aria-invalid="false">
                                                <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span>Herby I certify that the information I provided is all correct and accurate</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" id="declarationCheckbox" type="checkbox" name="declarationCheckbox" aria-invalid="false">
                                                <label class="form-check-label" for="declarationCheckbox"><span class="check-square"></span>&lt;Sample Declaration&gt;</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <p><a id = "docBack" class="back" ><em class="fa fa-angle-left"></em> Back</a></p>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" id = "previewSaveDraft">Save as Draft</a><a class="next btn btn-primary" id = "previewNext">SUBMIT & PAY </a></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">

    $(document).ready(function() {
        //Binding method
        $('#premisesEdit').click(function(){
            submit('premises',null,null);
        });
        $('#docEdit').click(function(){
            submit('documents',null,null);
        });
        $('#docBack').click(function(){
            submit('serviceForms',null,null);
        });
        $('#previewSaveDraft').click(function(){
            submit('preview','saveDraft',null);
        });
        $('#previewNext').click(function(){
            submit('payment','doSubmit',null);
        });

    });

    $('.svc-pannel-collapse').click(function () {
        $svcContenEle = $(this).closest('div.svc-content');
        $svcContenEle.find('.svc-iframe').css('height','400px');

    });




</script>
