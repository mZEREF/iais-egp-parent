<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<div class="col-xs-12">
    <div class="input-group">
        <div class="ax_default text_area">
            <span  style="font-size: 18px"><strong>Inspection Date</strong></span>
            <iais:datePicker id="inspectionDate" name = "inspectionDate"  value="${serListDto.inspectionDate}"></iais:datePicker>
            <span class="error-msg" id="error_inspectionDate" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="input-group">
        <div class="ax_default text_area">
            <span  style="font-size: 18px"><strong>Inspection Start Time (HH MM)</strong></span>
        </div>
        <div style="float: left"><input type="number" oninput="if(value.length>2)value=value.slice(0,2)" maxlength="2" name="startHour" value="<c:out value="${serListDto.startHour}"/>"></div><div style="float: left;padding-left: 10px;"><input type="number" oninput="if(value.length>2)value=value.slice(0,2)" maxlength="2" name="startHourMin" value="<c:out value="${serListDto.startMin}"/>"></div>
        <span class="error-msg" id="error_sTime" name="iaisErrorMsg"></span>
    </div>

    <div class="input-group">
        <div class="ax_default text_area">
            <span  style="font-size: 18px"><strong>Inspection End Time(HH MM)</strong></span>
        </div>
        <div style="float: left"><input type="number" oninput="if(value.length>2)value=value.slice(0,2)" maxlength="2" name="endHour" value="<c:out value="${serListDto.endHour}"/>"></div><div style="float: left;padding-left: 10px;"><input type="number" oninput="if(value.length>2)value=value.slice(0,2)" maxlength="2" name="endHourMin" value="<c:out value="${serListDto.endMin}"/>"></div>
        <span class="error-msg" id="error_eTime" name="iaisErrorMsg"></span>
        <span class="error-msg" id="error_timevad" name="iaisErrorMsg"></span>
    </div>

    <div class="input-group">
        <div class="ax_default text_area">
            <span style="font-size: 18px"><strong>Inspector Lead</strong></span> <c:out value="${serListDto.inspectionLeader}"/>
        </div>
    </div>

    <div class="input-group">
        <div class="ax_default text_area">
            <span style="font-size: 18px"><strong>Inspection Officers</strong></span>
            <c:forEach var = "officer" items = "${serListDto.inspectionofficer}" varStatus="status">
                <c:out value="${officer}"/>
            </c:forEach>
        </div>
    </div>

    <div class="input-group">
        <div class="ax_default text_area">
            <span style="font-size: 18px"><strong>Other Inspection Officers</strong></span><br>
            <textarea cols="70" rows="7" name="otherinspector" id="otherinspector" maxlength="300"><c:out value="${serListDto.otherinspectionofficer}"></c:out></textarea>
            <span class="error-msg" id="error_otherofficer" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="input-group">
        <div class="ax_default text_area">
            <span style="font-size: 18px"><strong>No. of Non-Compliance</strong></span>
            <c:out value="${serListDto.totalNcNum}"></c:out>
        </div>
    </div>

    <div class="input-group">
        <div class="ax_default text_area">
            <h4><strong>Remarks</strong></h4> <textarea cols="70" rows="7" name="tcuRemark" id="tcuRemark" maxlength="300"><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
            <span class="error-msg" id="error_tcuRemark" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="input-group">
        <div class="ax_default text_area">
            <h4><strong>Best Practices</strong></h4>
            <textarea cols="70" rows="7" name="bestpractice" id="bestpractice" maxlength="500"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
            <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
        </div>
    </div>
    <div class="input-group">
        <div class="ax_default text_area">
            <h4><strong>Letter Written to Licensee</strong></h4>
            <div class="file-upload-gp">
                <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
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
    <div class="input-group">
        <div class="ax_default text_area">
            <h4><strong>TCU</strong></h4> <input type="checkbox" id="tcuType"  value="tcuType"  <c:if test="${serListDto.tcuFlag == true}">checked</c:if>  name="tcuType" onclick="javascript: showTcuLabel(this);">
        </div>
    </div>

    <div class="input-group" id="tcuLabel">
        <div class="ax_default text_area">
            <span  style="font-size: 18px"><strong>TCU Date</strong></span>
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

    $('#selectedFile').change(function () {
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