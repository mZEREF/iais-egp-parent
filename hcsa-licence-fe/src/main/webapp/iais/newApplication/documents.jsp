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
                    <c:if test="${AppSubmissionDto.needEditController}">
                      <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                        <c:if test="${'APPPN02' == clickEditPage}">
                          <c:set var="isClickEdit" value="true"/>
                        </c:if>
                      </c:forEach>
                      <c:choose>
                        <c:when test="${'true' != isClickEdit}">
                          <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                        </c:when>
                        <c:otherwise>
                          <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                        </c:otherwise>
                      </c:choose>
                      <c:if test="${'true' != isClickEdit}">
                        <c:set var="locking" value="true"/>
                        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.docEdit}"/>
                        <div id="edit-content">
                          <c:choose>
                            <c:when test="${canEdit}">
                              <p class="text-right"><a id="edit"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                          </c:choose>
                        </div>
                      </c:if>
                    </c:if>





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
                              <span class="existFile delBtn <c:if test="${!isClickEdit}">hidden</c:if>">
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
                    <c:choose>
                    <c:when test="${'APTY005' ==AppSubmissionDto.appType && requestInformationConfig == null}">
                      <%@include file="../common/rfcFooter.jsp"%>
                    </c:when>
                    <c:otherwise>
                      <div class="row">
                        <div class="col-xs-12 col-sm-6">
                          <p><a id = "Back" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                          <div class="button-group"><a class="btn btn-secondary" id = "SaveDraft">Save as Draft</a><a class="btn btn-primary next" id="Next">Next</a></div>
                        </div>
                      </div>
                    </c:otherwise>
                    </c:choose>
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
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
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



        doEdit();
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

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('input[type="file"]').prop('disabled',false);
            $('.existFile').removeClass('hidden');
            $('.existFile').removeClass('existFile');
        });
    }



</script>