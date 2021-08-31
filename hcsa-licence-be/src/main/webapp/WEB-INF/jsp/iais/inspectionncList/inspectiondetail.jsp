<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<div class="row" id="checklistEditArea">
    <input type="hidden" id="fileMaxSizeForIns" name="fileMaxSizeForIns" value="${applicationViewDto.systemMaxFileSize}">
    <input type="hidden" id="fileUploadTypeForIns" name="fileUploadTypeForIns" value="${applicationViewDto.systemFileType}">
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label"  for="inspectionDate" >Inspection Date</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p> <span>${serListDto.inspectionDate}</span></p>
            <div hidden>
            <iais:datePicker id="inspectionDate" name = "inspectionDate"  value="${serListDto.inspectionDate}"></iais:datePicker>
            </div>
            <span class="error-msg" id="error_inspectionDate" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Inspection Start Time (HH MM)</label>
            <c:choose>
                <c:when test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
               <div class="col-xs-8 col-sm-6 col-md-5">
                <div style="margin-left: -15px" class="col-md-6">
                    <iais:select name="startHour" options="hhSelections" value="${serListDto.startHour}" firstOption="--"></iais:select>
                </div>
                <div class="col-md-6">
                    <iais:select name="startHourMin" options="ddSelections" value="${serListDto.startMin}" firstOption="--"></iais:select>
                </div>
                   <span class="error-msg" id="error_sTime" name="iaisErrorMsg"></span>
               </div>
            </c:when>
                <c:otherwise>
               <div class="col-xs-8 col-sm-6 col-md-5">
                    <p> <span>
                        <c:if test="${(empty serListDto.startHour) && (empty serListDto.startMin)}">
                        </c:if>
                  <c:if test="${(not empty serListDto.startHour) && (not empty serListDto.startMin)}">
                      ${serListDto.startHour} : ${serListDto.startMin}
                  </c:if>
                </div>
                </c:otherwise>
            </c:choose>
        </div>
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspection End Time (HH MM)</label>
        <c:choose> <c:when test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
            <div class="col-xs-8 col-sm-6 col-md-5">
                <div style="margin-left: -15px" class="col-md-6" >
                    <iais:select name="endHour" options="hhSelections" value="${serListDto.endHour}" firstOption="--" ></iais:select>
                </div>
                <div class="col-md-6">
                    <iais:select name="endHourMin" options="ddSelections" value="${serListDto.endMin}" firstOption="--"></iais:select>
                </div>
                <span class="error-msg" id="error_eTime" name="iaisErrorMsg"></span>
                <span class="error-msg" id="error_timevad" name="iaisErrorMsg"></span>
            </div>
        </c:when>
        <c:otherwise>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p> <span>
                  <c:if test="${(empty serListDto.endHour) && (empty serListDto.endMin)}">
                  </c:if>
                  <c:if test="${(not empty serListDto.endHour) && (not empty serListDto.endMin)}">
                    ${serListDto.endHour} : ${serListDto.endMin}
                 </c:if>
                </span>
            </p>
        </div>
        </c:otherwise>
       </c:choose>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspector Lead</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p> <c:out value="${serListDto.inspectionLeader}"/> </p>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspection Officer(s)</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p><c:forEach var = "officer" items = "${serListDto.inspectionofficer}" varStatus="status"><c:if test="${status.index >0}">, </c:if><c:out value="${officer}"/></c:forEach></p>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Other Inspection Officer(s)</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="otherinspector" id="otherinspector" maxlength="300"><c:out value="${serListDto.otherinspectionofficer}"></c:out></textarea>
            <span class="error-msg" id="error_otherofficer" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >No. of Non-Compliance</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p> <c:out value="${serListDto.totalNcNum}"></c:out></p>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Internal Remarks</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="tcuRemark" id="tcuRemark" maxlength="300"><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
            <span class="error-msg" id="error_tcuRemark" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Recommendation</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="bestpractice" id="bestpractice" maxlength="500"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
            <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Observation</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="observation " id="observation" maxlength="500"><c:out value="${serListDto.observation}"></c:out></textarea>
            <span class="error-msg" id="error_observation" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label">Letter Written to Licensee</label>
            <div class="col-xs-8 col-sm-6 col-md-5" >
                  <c:if test="${applicationViewDto.applicationDto.status == ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
                      <p>
                          <c:if test="${ not empty  serListDto.appPremisesSpecialDocDto.fileRepoId}">
                      <span ><a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo888&fileRo888=<iais:mask name="fileRo888" value="${serListDto.appPremisesSpecialDocDto.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(serListDto.appPremisesSpecialDocDto.docName,StandardCharsets.UTF_8.toString())}" title="Download" class="downloadFile">&nbsp; <c:out value="${serListDto.appPremisesSpecialDocDto.docName}"/></a></span>
                          </c:if>
                      </p>
                  </c:if>
                <div class="file-upload-gp" <c:if test="${applicationViewDto.applicationDto.status == ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}"> hidden</c:if>>
                    <input id="selectedFileView" name="selectedFileView" type="file" style="display: none;" aria-label="selectedFile" onclick="javascript:fileClicked(event)" onchange="javascript:fileChangedIns(event)"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                    <span id="licFileName"> <a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo888&fileRo888=<iais:mask name="fileRo888" value="${serListDto.appPremisesSpecialDocDto.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(serListDto.appPremisesSpecialDocDto.docName,StandardCharsets.UTF_8.toString())}" title="Download" class="downloadFile">&nbsp; <c:out value="${serListDto.appPremisesSpecialDocDto.docName}"/></a></span>
                    <span id="licFileNameDe" <c:if test="${empty serListDto.appPremisesSpecialDocDto}">hidden</c:if> >
                                &nbsp;&nbsp;<button type="button" class="btn btn-secondary-del btn-sm" onclick="javascript:doDeleteFile()">
                                                 Delete</button>
                </span>
                    <input id="litterFile" name="litterFile" type="hidden" value="<c:out value="${serListDto.appPremisesSpecialDocDto.docName}"></c:out>" />
                    <input id="litterFileId" name="litterFileId" type="hidden" value="<iais:mask name="litterFileId" value="${serListDto.appPremisesSpecialDocDto.id}"/>"/>
                    <br/> <span class="error-msg" id="error_litterFile" name="iaisErrorMsg"></span>
                    <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
                </div>
            </div>
        </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">TCU</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
           <p><input type="checkbox" id="tcuType"  value="tcuType"  <c:if test="${serListDto.tcuFlag}">checked</c:if>  name="tcuType" onclick="javascript: showTcuLabel(this);">
               <label class="form-check-label" for="tcuType" ><span class="check-square"></span></label>
           </p>
        </div>
    </div>

    <div class="form-group" id="tcuLabel" >
        <label class="col-xs-12 col-md-4 control-label">TCU Date <c:if test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}"><span style="color: red"> *</span></c:if></label>
     <c:choose>
    <c:when test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
        <div class="col-xs-8 col-sm-6 col-md-5">
            <iais:datePicker id = "tuc" name = "tuc" value="${serListDto.tuc}"></iais:datePicker>
            <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
        </div>
    </c:when>
    <c:otherwise>
        <div class="col-xs-8 col-sm-6 col-md-5">
        <p> <span>${serListDto.tuc}</span></p>
        </div>
    </c:otherwise>
     </c:choose>
    </div>

    <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK == applicationViewDto.applicationDto.applicationType && ApplicationConsts.AUDIT_TYPE_THEMATIC != applicationViewDto.licPremisesAuditDto.auditType}">
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">To include findings in risk score framework?</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p><input type="checkbox" id="framework" name="framework" onchange="javascirpt:changeframework();"
               value="0"    <c:if test="${applicationViewDto.licPremisesAuditDto.inRiskSocre == 0}">checked</c:if>  >
                <label class="form-check-label" for="framework" ><span class="check-square"></span></label>
            </p>
        </div>
    </div>
    <div class="form-group" id="frameworkOp" style="display: none;">
        <label class="col-xs-12 col-md-4 control-label">Which form of risk should it be recorded in risk score framework?<c:if test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}"><span style="color: red"> *</span></c:if></label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <c:choose>
                <c:when test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}">
                    <iais:select name="periods" options="frameworknOption" firstOption="Please Select" onchange="javascirpt:changeFramewordOption(this.value);"
                                 value="${applicationViewDto.licPremisesAuditDto.includeRiskType}"/>
                    <span id="error_periods" name="iaisErrorMsg" class="error-msg"></span>
                </c:when>
                <c:otherwise>
                    <iais:select name="periods" options="frameworknOption" firstOption="Please Select" onchange="javascirpt:changeFramewordOption(this.value);"
                                 value="${applicationViewDto.licPremisesAuditDto.includeRiskType}" disabled="true"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="form-group" id="frameworkRe" style="display: none;">
        <label class="col-xs-12 col-md-4 control-label">Enforcement Remarks<c:if test="${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW}"><span style="color: red"> *</span></c:if></label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea name="frameworkRemarks" id="frameworkRemarks" cols="43" rows="5" maxlength="2000"><c:out value="${applicationViewDto.licPremisesAuditDto.lgrRemarks}"></c:out></textarea>
            <br/>
            <span id="error_frameworkRemarks" name="iaisErrorMsg" class="error-msg"></span>
        </div>
    </div>
    </c:if>

</div>
<script type="text/javascript">
    function changeframework() {
        if ($('#framework').is(':checked')) {
            $("#frameworkOp").show();
        } else {
            $("#frameworkOp").hide();
            $("#frameworkRe").hide();
        }
    }


    function changeFramewordOption(obj) {
        if (obj == "INRT001") {
            $("#frameworkRe").show();
        } else {
            $("#frameworkRe").hide();
            $("#frameworkRemarks").val("");
        }
    }

    $(document).ready(function (){
        if($("#tcuType").is(":checked")){
            $("#tcuLabel").show()
        }else{
            $("#tcuLabel").hide();
            $("#tuc").val("");
        }
        if(${serListDto.tuc!= null && serListDto.tuc != ""}){
            $("#tcuType").attr('checked',true)
            $("#tcuLabel").show();
        }
        changeframework();
        changeFramewordOption('${applicationViewDto.licPremisesAuditDto.includeRiskType}');
        readOnlyArea('${applicationViewDto.applicationDto.status}');
    });

    function readOnlyArea(status) {
        if(status == 'APST032'){
            $("#checklistEditArea textarea").attr('readonly','readonly');
            $("#checklistEditArea textarea").attr('Enabled',false);
            $("#tcuType").attr("disabled",true);
            $("#framework").attr("disabled",true);
        }
    }
    function showTcuLabel(checkbox){
        if(checkbox.checked == true){
            $("#tcuLabel").show()
        }else{
            $("#tcuLabel").hide();
            $("#tuc").val("");
        }
    }

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('#selectedFileView').change(fileChangeIns());
    function fileChangedIns(event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            clone[fileElement.id].insertBefore(fileElement); //'Restoring Clone'
            $(fileElement).remove(); //'Removing Original'
            if (evenMoreListeners) { addEventListenersTo(clone[fileElement.id]) }//If Needed Re-attach additional Event Listeners
        }else {
            $('#selectedFileView').change(fileChangeIns());
        }
    }
    function fileChangeIns() {
        var maxSize = $("#fileMaxSizeForIns").val();
        var error  = validateUploadSizeMaxOrEmpty(maxSize,'selectedFileView');
        if( error =="Y"){
            var file = $("#selectedFileView").val();
            var  fileName = getFileName(file);
            if( fileName != null && fileName.trim() != ""){
                if(fileName.length > 100){
                    doDeleteFile();
                    $('#error_litterFile_Show').html($("#fileMaxLengthMessage").val());
                }else {
                    $("#licFileNameDe").attr("hidden",false);
                    $('#litterFile').val(fileName);
                    $('#litterFileId').val("");
                    $('#error_litterFile_Show').html("");
                    doSubmitUploadFile();
                    $("#licFileName").html( fileName);
                }
            }else {
                $('#error_litterFile_Show').html("");
            }
        }else {
            if(error == "N"){
                bigFileClear();
                doDeleteFile();
                $('#error_litterFile_Show').html($("#fileMaxMBMessage").val());
            }
        }
    }
    function doDeleteFile() {
        $("#licFileNameDe").attr("hidden",true);
        $("#licFileName").html("");
        $('#litterFile').val("");
        $('#litterFileId').val("");
        $("#error_litterFile").html("");
        $('#error_litterFile_Show').html("");
        $("#selectedFileView").val('');
    }

    function bigFileClear() {
        var error = validateUploadSizeMaxOrEmpty('100','selectedFileView');
        if(error == "N") {
            var file = $("#selectedFileView");
            var fileClone = file.clone();
            file.after(fileClone.val(""));
            file.remove();
        }
    }

    function doSubmitUploadFile(){
        $("#viewchk").val("uploadFileLetter");
        showWaiting();
        if(${applicationViewDto.applicationDto.status != ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER}){
            SOP.Crud.cfxSubmit("mainForm", "next");
        }else {
            $("#saveflag").val("save");
            SOP.Crud.cfxSubmit("mainForm", "save");
        }
    }

    //only for doc change
    $('#documentsli').click(function(){
        $("#mainForm").removeClass("form-horizontal");});

    $('#infoli').click(function(){
        $("#mainForm").addClass("form-horizontal");});

    $('#checkListli').click(function(){
        $("#mainForm").addClass("form-horizontal");});

    $('#processingli').click(function(){
        $("#mainForm").removeClass("form-horizontal");});
    $('#emailViewli').click(function(){
        $("#mainForm").addClass("form-horizontal");});

</script>