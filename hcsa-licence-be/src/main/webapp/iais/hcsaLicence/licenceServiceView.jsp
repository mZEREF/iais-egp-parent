<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <c:set var="appEdit" value="${applicationViewDto.appEditSelectDto}"/>
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
                      <br>
                      <br>
                      <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>
                    </div>
                  </div>
                    <c:if test = "${not empty errorMsg}">
                      <iais:error>
                      <div class="error">
                          ${errorMsg}
                      </div>
                      </iais:error>
                    </c:if>
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
                                <c:if test="${appEdit.premisesEdit || appEdit.premisesListEdit }" >
                                  <input class="form-check-input" id="premisesCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="premises">
                                </c:if>
                                 </p>
                              <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                                <div class="panel-main-content">
                                  <div class="preview-info">
                                    <p><span  ><strong>Premises ${status.index+1}:</strong> ${appGrpPremDto.premisesType}</span>
                                       <wrms:value width="7">
                                      <span class="newVal " attr="${appGrpPremDto.premisesType}" style="display: none"><label><c:out value=""/></label></span>
                                      <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"  style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"/></label></span>
                                    </wrms:value></p>
                                    <p ><span  ><strong >HCL NAME:</strong> ${appGrpPremDto.hciName}</span>
                                       <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.hciName}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/></label></span>
                                      </wrms:value></p>
                                    <p><span  ><strong>Address: </strong> ${appGrpPremDto.address}</span>

                                       <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.address}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/></label></span>
                                      </wrms:value></p>

                                    <p><span ><strong>Telephone:</strong>  ${appGrpPremDto.offTelNo}</span>
                                       <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.offTelNo}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"/></label></span>
                                      </wrms:value></p>

                                    <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                      <p><strong>Vehicle No:</strong> ${appGrpPremDto.conveyanceVehicleNo}  <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></label></span>
                                      </wrms:value></p>
                                    </c:if>
                                    <p><strong>Are you co-locating with anothe licensee? :</strong> ${appGrpPremDto.conveyanceVehicleNo}  <wrms:value width="7">
                                      <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}" style="display: none"><label><c:out value=""/></label></span>
                                      <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></label></span>
                                    </wrms:value></p>

                                    <p><strong>Operating Hours (Start):</strong> ${appGrpPremDto.wrkTimeFrom}  <wrms:value width="7">
                                      <span class="newVal " attr="${appGrpPremDto.wrkTimeFrom}" style="display: none"><label><c:out value=""/></label></span>
                                      <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"/></label></span>
                                    </wrms:value></p>
                                    <p><strong>Operating Hours (End):</strong> ${appGrpPremDto.wrkTimeTo}  <wrms:value width="7">
                                      <span class="newVal " attr="${appGrpPremDto.wrkTimeTo}" style="display: none"><label><c:out value=""/></label></span>
                                      <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"/></label></span>
                                    </wrms:value></p>

                                    <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">

                                      <p><strong>Public Holidays Operating Hours (Start):</strong> ${appPremPhOpenPeriod.startFrom}  <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index][statu.index].startFrom}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index][statu.index].startFrom}"/></label></span>
                                      </wrms:value></p>
                                      <p><strong>Public Holidays Operating Hours (End):</strong> ${appPremPhOpenPeriod.endTo}  <wrms:value width="7">
                                        <span class="newVal " attr="${appPremPhOpenPeriod.endTo}" style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index][statu.index].endTo}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index][statu.index].endTo}"/></label></span>
                                      </wrms:value></p>

                                    </c:forEach>

                                  </div>
                                </div>
                              </c:forEach>
                            </div>
                          </div>
                        </div>
                        <div class="panel panel-default">
                          <div class="panel-heading" id="headingOne" role="tab">
                            <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Key Roles</a></h4>
                          </div>
                          <div class="panel-collapse collapse" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                            <div class="panel-body">
                              <p class="text-right">
                                <!--<input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">-->
                              </p>
                              <div class="elemClass-1561088919456">
                                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                                  <div class="control-set-font control-font-header section-header">
                                  <%--  <p class="summary-header">Licensee (Company)</p>--%>
                                  </div>
                                  <div class="pop-up">
                                    <div class="pop-up-body">
                                      <div class="field col-sm-4 control-label formtext"><p><strong>Licensee (Company)</strong></p>
                                        <p style="display: inline"><strong>Name:</strong></p><p style="display: inline"> ${newLicenceDto.name}</p>
                                        <span>
                                                <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.name}" style="display: none" ><label><c:out value="${newLicenceDto.name}"/></label></span>
                                                  <span class="oldVal compareTdStyle" attr="${oldLicenceDto.name}" style="display: none"><label><c:out value="${oldLicenceDto.name}"/></label></span>
                                                </wrms:value></span>
                                        </span>
                                      <p>
                                        <p><strong>AuthoriseD Person 1(po)</strong>&nbsp;&nbsp;&nbsp;&nbsp;
                                          <c:if test="${appEdit.serviceEdit || appEdit.poEdit}" >
                                          <input class="form-check-input" type="checkbox" name="editCheckbox" aria-invalid="false" value="po">
                                          </c:if>
                                        </p>
                                        <p><strong>Name:</strong></p>
                                        <p><strong>ID:</strong></p>
                                        <br>
                                        <p><strong>AuthoriseD Person 2(dpo)</strong>&nbsp;&nbsp;&nbsp;&nbsp;
                                          <c:if test="${appEdit.serviceEdit ||appEdit.dpoEdit}" >
                                          <input class="form-check-input"  type="checkbox" name="editCheckbox" aria-invalid="false" value="dpo">
                                          </c:if>
                                        </p>
                                          <p><strong>Name:</strong></p>
                                        <p><strong>ID:</strong></p>
                                        <br>
                                        <p><strong>MedAlert Contact Person</strong>&nbsp;&nbsp;&nbsp;&nbsp;
                                          <c:if test="${appEdit.medAlertEdit || appEdit.serviceEdit}">
                                          <input class="form-check-input"  type="checkbox" name="editCheckbox" aria-invalid="false" value="medAlert">
                                          </c:if>
                                        </p>
                                      </div>

                                      <c:forEach var="appGrpPrimaryDocDto" items="${appSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                        <div class="content-body fileUploadContainer">
                                          <div class="control col-sm-5">
                                            <div class="fileList">
                                              <span class="filename server-site col-xs-6 col-md-6" id="130">

                                              <span class="col-xs-6 col-md-6">
                                                <wrms:value width="7">
                                                <span class="newVal compareTdStyle" attr="${appGrpPrimaryDocDto.docSize}${appGrpPrimaryDocDto.docName}"  style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/></label></span>
                                                </wrms:value></span>
                                              </span>
                                            </div>
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

                          <div class="panel panel-default svc-content">

                            <div class="panel-heading"    id="headingServiceInfo0" role="tab" >
                              <h4 class="panel-title"><a class="svc-pannel-collapse"  role="button" data-toggle="collapse" href="#collapseServiceInfo0" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information -- ${hcsaServiceDto.svcName}</a></h4>
                            </div>

                            <div class=" panel-collapse collapse" id="collapseServiceInfo0" role="tabpanel" aria-labelledby="headingServiceInfo0">
                              <div class="panel-body">
                                <p class="text-right">
                                  <input class="form-check-input" id="serviceCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="service">
                                </p>
                                <iframe  class="svc-iframe" title="" src="${pageContext.request.contextPath}/eservice/INTRANET/MOHServiceView" id="elemId-0"  width="100%" height="100%" ></iframe> <!--scrolling="no" scrollbar="no" -->
                              </div>
                            </div>
                          </div>
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
<style>
  .compareTdStyle{display:inline;padding:.2em .6em .3em;line-height:1;color:#000;text-align:center;vertical-align:baseline;border-radius:.5em;font-size:100%;background-color:#FF0}
</style>
<script type="text/javascript">

    $(document).ready(function() {
        //Binding method
        $('#previewNext').click(function(){
            var mainForm = document.getElementById("mainForm");
            mainForm.submit();
        });

        $('.svc-pannel-collapse').click(function () {
            $svcContenEle = $(this).closest('div.svc-content');
            $svcContenEle.find('.svc-iframe').css('height','400px');

        });
    });


    hightLightChangeVal('newVal', 'oldVal');

    function hightLightChangeVal(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if (oldVal.length > 0 && newVal.length > 0) {
                if (oldVal != newVal) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            }else  if($("#view").val()=="") {
                if (oldVal != newVal) {
                    $(this).show();
                    $(this).html("NA");
                } else {
                    $(this).hide();
                }
            }
        });
    }


</script>
