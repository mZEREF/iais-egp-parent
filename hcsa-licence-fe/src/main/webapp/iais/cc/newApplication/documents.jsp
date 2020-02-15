<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>
            
            <div class="tab-content">
              <div class="tab-pane active" id="documentsTab" role="tabpanel">
                <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004'==AppSubmissionDto.appType}">
                  <c:set var="showPreview" value="true"/>
                  <c:forEach var="amendType"  items="${AppSubmissionDto.amendTypes}">
                    <c:if test="${amendType =='RFCATYPE06'}">
                      <c:set var="canEdit" value="1"/>
                    </c:if>
                  </c:forEach>
                </c:if>
              </div>
                <div class="document-content ">
                  <div class="document-info-list">
                    <ul>
                      <li>
                        <p>The maximum file size for each upload is 4MB. </p>
                      </li>
                      <li>
                        <p>Acceptable file formats are PDF, JPG and PNG. </p>
                      </li>
                      <li>
                        <p>All files are mandatory.</p>
                      </li>
                    </ul>
                  </div>
                  <div class="document-upload-gp">
                    <h2>PRIMARY DOCUMENTS</h2>
                    
                    <c:set value="${reloadAppGrpPrimaryDocMap}" var="docMap"/>
                    <!--common -->
                    <c:forEach var="commonDoc" items="${commonHcsaSvcDocConfigDto}" varStatus="v">
                      <c:set var="commKey" value="common${commonDoc.id}" />
                      <c:set var="file" value="${docMap.get(commKey)}"/>
                      <c:set var="commDelFlag" value="common${commonDoc.id}flag"/>
                      <div class="document-upload-list">
                        <h3>${commonDoc.docTitle}</h3>
                        <div class="file-upload-gp">
                          <input class="hidden delFlag" type="hidden" name="${commDelFlag}" value="N"/>
                          <span  >${file.docName}</span>
                          <c:choose>
                            <c:when test="${file.docName == '' || file.docName == null }">
                              <span class="hidden delBtn">
                                &nbsp;&nbsp;<button type="button" class="">Delete</button>
                              </span>
                            </c:when>
                            <c:otherwise>
                              <span class="delBtn <c:if test="${'1' != canEdit}">hidden</c:if>">
                                &nbsp;&nbsp;<button type="button" class="">Delete</button>
                              </span>
                            </c:otherwise>
                          </c:choose>
                          <br/>
                          <input class="selectedFile commDoc" id="commonDoc"  name = "${commKey}" type="file" style="display: none;" aria-label="selectedFile1" >
                          <span name="iaisErrorMsg" class="error-msg" id="error_${commKey}"></span><br>
                          <a class="btn btn-file-upload btn-secondary" >Upload</a><br/>
                        </div>
                      </div>
                    </c:forEach>
                    <!--prem -->
                    <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStatus">
                      <c:forEach var="premDoc" items="${premHcsaSvcDocConfigDto}">
                        <c:choose>
                          <c:when test="${prem.hciName != null && prem.hciName != ''}">
                            <c:set value="${prem.hciName}" var="premIndexNo"/>
                          </c:when>
                          <c:when test="${prem.conveyanceVehicleNo != null && prem.conveyanceVehicleNo != ''}">
                            <c:set value="${prem.conveyanceVehicleNo}" var="premIndexNo"/>
                          </c:when>
                        </c:choose>
                        <c:set var="premKey" value="prem${premDoc.id}${premIndexNo}" />
                        <c:set var="primaryDoc" value="${docMap.get(premKey)}"/>
                        <c:set var="premDelFlag" value="prem${premDoc.id}${premIndexNo}flag"/>
                        <div class="document-upload-list">
                          <h3>premises${premStatus.index+1}: ${premDoc.docTitle}</h3>
                          <div class="file-upload-gp">
                            <input class="hidden delFlag" type="hidden" name="${premDelFlag}" value="N"/>
                            <span  >${primaryDoc.docName}</span>
                            <c:choose>
                              <c:when test="${primaryDoc.docName == '' || primaryDoc.docName == null }">
                                <span class="hidden delBtn">
                                  &nbsp;&nbsp;<button type="button" class="">Delete</button>
                                </span>
                              </c:when>
                              <c:otherwise>
                                <span class=" delBtn">
                                  &nbsp;&nbsp;<button type="button" class="">Delete</button>
                                </span>
                              </c:otherwise>
                            </c:choose>
                            <br/>
                            <input class="selectedFile premDoc"  name = "${premKey}" type="file" style="display: none;" aria-label="selectedFile1">
                            <span name="iaisErrorMsg" class="error-msg" id="error_${premKey}"></span><br>
                            <a class="btn btn-file-upload btn-secondary" >Upload</a><br/>
                          </div>
                        </div>
                      </c:forEach>
                    </c:forEach>
                  </div>
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-12 col-sm-6">
                        <p><a id = "Back" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
                      </div>
                      <div class="col-xs-12 col-sm-6">
                        <div class="button-group"><a class="btn btn-secondary" id = "SaveDraft">Save as Draft</a><a class="btn btn-primary next" id="Next">Next</a></div>
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
  <%@ include file="/include/validation.jsp" %>
    <input type="hidden" name="pageCon" value="valPremiseList" >
</form>


<script type="text/javascript">
    $(document).ready(function() { 
        if('APTY005' == '${AppSubmissionDto.appType}' &&'1' != '${canEdit}'){
            disabledPage();
        }
        
        //Binding method
        $('#Back').click(function(){
            submit('premises',null,null);
        });
        $('#SaveDraft').click(function(){
            submit('documents','saveDraft',null);
        });
        $('#Next').click(function(){
            submit('serviceForms',"next",null);
        });
        /*$('#selectedFile').change(function(){
            var file = $('#selectedFile').val();
            $('#showFile').html(getFileName(file))
        });*/
        // if($('#isPramayEdit').val() == 'true'){
        //     disabledPage();
        // }
        <c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.docEdit}">
        disabledPage();
        </c:if>
    });
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $(this).parent().children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('span:eq(0)').next().removeClass("hidden");
        $(this).parent().children('input delFlag').val('N');
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');

    });





</script>