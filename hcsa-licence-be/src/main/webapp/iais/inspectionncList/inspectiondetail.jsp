<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<div class="row">
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label"  for="inspectionDate" >Inspection Date</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <iais:datePicker id="inspectionDate" name = "inspectionDate"  value="${serListDto.inspectionDate}"></iais:datePicker>
            <span class="error-msg" id="error_inspectionDate" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Inspection Start Time (HH MM)</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <div style="margin-left: -15px" class="col-md-6">
                    <iais:select name="startHour" options="hhSelections" value="${serListDto.startHour}" firstOption="--"></iais:select>
            </div>
            <div class="col-md-6">
                    <iais:select name="startHourMin" options="ddSelections" value="${serListDto.startMin}" firstOption="--"></iais:select>
            </div>
            <span class="error-msg" id="error_sTime" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspection End Time (HH MM)</label>
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
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspector Lead</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p> <c:out value="${serListDto.inspectionLeader}"/> </p>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Inspection Officers</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <p>
            <c:forEach var = "officer" items = "${serListDto.inspectionofficer}" varStatus="status">
                <c:out value="${officer}"/>
            </c:forEach>
            </p>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label" >Other Inspection Officers</label>
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
        <label class="col-xs-12 col-md-4 control-label">Remarks</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="tcuRemark" id="tcuRemark" maxlength="300"><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
            <span class="error-msg" id="error_tcuRemark" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Best Practices</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <textarea cols="43" rows="5" name="bestpractice" id="bestpractice" maxlength="500"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
            <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
        </div>
    </div>

    <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label">Letter Written to Licensee</label>
            <div class="col-xs-8 col-sm-6 col-md-5">
                <div class="file-upload-gp">
                    <input id="selectedFileView" name="selectedFileView" type="file" style="display: none;" aria-label="selectedFile"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                    <span id="licFileName"> &nbsp; &nbsp; &nbsp; &nbsp;${serListDto.appPremisesSpecialDocDto.docName}</span>
                    <span id="licFileNameDe" <c:if test="${empty serListDto.appPremisesSpecialDocDto}">hidden</c:if> >
                                &nbsp;&nbsp;<button type="button" onclick="javascript:doDeleteFile()">Delete</button>
                </span>
                    <input id="litterFile" name="litterFile" type="hidden" value="<c:out value="${serListDto.appPremisesSpecialDocDto.docName}"></c:out>" />
                    <input id="litterFileId" name="litterFileId" type="hidden" value="<c:out value="${serListDto.appPremisesSpecialDocDto.id}"></c:out>"/>
                    <span class="error-msg" id="error_litterFile" name="iaisErrorMsg"></span>
                </div>
            </div>
        </div>


    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">TCU</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
           <p><input type="checkbox" id="tcuType"  value="tcuType"  <c:if test="${serListDto.tcuFlag}">checked</c:if>  name="tcuType" onclick="javascript: showTcuLabel(this);"></p>
        </div>
    </div>

    <div class="form-group" id="tcuLabel" >
        <label class="col-xs-12 col-md-4 control-label">TCU Date</label>
        <div class="col-xs-8 col-sm-6 col-md-5">
            <iais:datePicker id = "tuc" name = "tuc" value="${serListDto.tuc}"></iais:datePicker>
            <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
        </div>
    </div>

</div>
<script type="text/javascript">
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
    });
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

    $('#selectedFileView').change(function () {
        var file = $(this).val();
        var  fileName = getFileName(file);
        if( fileName != null && fileName.trim() != ""){
            $("#licFileNameDe").attr("hidden",false);
            $("#licFileName").html( fileName);
            $('#litterFile').val(fileName);
            $('#litterFileId').val("");
        }
    });

    function doDeleteFile() {
        $("#licFileNameDe").attr("hidden",true);
        $("#licFileName").html("");
        $('#litterFile').val("");
        $('#litterFileId').val("");
        $("#error_litterFile").html("");
    }
</script>