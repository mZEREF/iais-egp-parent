<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="previewTab" role="tabpanel">
                <div class="preview-gp">
                  <div class="row">

                    <div class="col-xs-12 col-md-2 text-right">
                      <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-xs-12">
                      <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                        <div class="panel panel-default">
                          <div class="panel-heading" id="headingPremise" role="tab">
                            <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
                          </div>
                          <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                            <div class="panel-body">
                              <p class="text-right">
                                <input class="form-check-input" id="premisesCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="premises">
                              </p>
                              <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
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
                          <div class="panel-heading" id="headingOne" role="tab">
                            <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
                          </div>
                          <div class="panel-collapse collapse" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                            <div class="panel-body">
                              <p class="text-right">
                                <input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">
                              </p>
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
                                            <div class="fileList"><span class="filename server-site" id="130">
                                              <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">${appGrpPrimaryDocDto.docName}</a> (${appGrpPrimaryDocDto.docSize} KB)</span></div>
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
                        <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status">
                          <div class="panel panel-default">
                            <div class="panel-heading " id="headingServiceInfo" role="tab">
                              <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information -- ${hcsaServiceDto.svcName}</a></h4>
                            </div>
                            <div class="panel-collapse collapse in" id="collapseServiceInfo${status.index}" role="tabpanel" aria-labelledby="headingServiceInfo${status.index}">
                              <div class="panel-body">
                                <p class="text-right">
                                  <input class="form-check-input" id="serviceCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="service">
                                </p>
                                <iframe  class="elemClass-1561088919456" title="" src="${pageContext.request.contextPath}/eservice/INTERNET/MohServiceRelatedInformation?crud_action_type_form_value=prepareView&svcId=${hcsaServiceDto.id}" id="elemId-${status.index}"  width="100%" height="100%" ></iframe> <!--scrolling="no" scrollbar="no" -->
                              </div>
                            </div>
                          </div>
                        </c:forEach>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6">

                    </div>
                    <div class="col-xs-12 col-sm-6">
                      <div class="button-group"><a class="next btn btn-primary" id = "previewNext">SUBMIT </a></div>
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

        donwload();
    });




</script>
