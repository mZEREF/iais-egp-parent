<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form method="post" enctype="multipart/form-data" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="maxFile" id ="maxFile" value="${maxFile}">
  <div class="row">
    <div class="container">
      <div class="col-xs-12">
        <iais:section title="">
          <iais:row>
            <iais:field value="Licence No."></iais:field>
            <iais:value width="10"><p>${prepareTranfer.licenceNo}</p></iais:value>
          </iais:row>
          <iais:row>
            <iais:field value="Service Name"></iais:field>
            <iais:value width="10"><p>${prepareTranfer.serviceName}</p></iais:value>
          </iais:row>
          <c:if test="${prepareTranfer.groupLic}">
          <iais:row>
            <iais:field value="Select Premises"></iais:field>
            <iais:value width="10">
              <p>
              <c:forEach items="${prepareTranfer.appGrpPremisesDtoList}" var="premises">
                <div class="form-check">
                  <input class="form-check-input" id="premisesInput" type="checkbox" name="premisesInput" aria-invalid="false" value="${premises.premisesIndexNo}" disabled>
                  <label class="form-check-label" for="premisesInput"><span class="check-square"></span>${premises.tranferSelect} </label>
                </div>
              </c:forEach>
              <span  class="error-msg" name="iaisErrorMsg" id="error_premisesError"></span>
              </p>
            </iais:value>
          </iais:row>
          </c:if>
          <iais:row>
          <iais:field value="UEN of Licence to transfer licence to"></iais:field>
          <iais:value width="10">
            <p>
              <input type="text" name="UEN" value="${UEN}" maxlength="10" disabled>
              <span  class="error-msg" name="iaisErrorMsg" id="error_uenError"></span>
            </p>
          </iais:value>
        </iais:row>
         <iais:row>
            <iais:field value="Reason for licence transfer"></iais:field>
            <iais:value width="10">
              <p>
                <textarea  name="reason" maxlength="300" cols="45" >${reason}</textarea>
                <span  class="error-msg" name="iaisErrorMsg" id="error_reasonError"></span>
              </p>
            </iais:value>
         </iais:row>
          <iais:row>
            <iais:field value="Email address of transferee" mandatory="true"></iais:field>
            <iais:value width="10">
              <p>
                <input type="text" name="email" value="${email}" maxlength="66">
                <span  class="error-msg" name="iaisErrorMsg" id="error_emailError"></span>
              </p>
            </iais:value>
          </iais:row>
          <iais:row>
            <iais:field value="Letter of Undertaking" mandatory="true"></iais:field>
            <iais:value width="10">
              <p>
              <div class="file-upload-gp">
              <input class="hidden delFlag" type="hidden" name="commDelFlag" value="N"/>
              <span>${appPremisesSpecialDocDto.docName}</span>
              <c:choose>
                <c:when test="${appPremisesSpecialDocDto.docName == '' || appPremisesSpecialDocDto.docName == null }">
                              <span class="hidden delBtn">
                                &nbsp;&nbsp;<button type="button" class="btn btn-danger btn-sm"><em class="fa fa-times"></em></button>
                              </span>
                </c:when>
                <c:otherwise>
                              <span class="existFile delBtn">
                                &nbsp;&nbsp;<button type="button" class="btn btn-danger btn-sm"><em class="fa fa-times"></em></button>
                              </span>
                </c:otherwise>
              </c:choose>
                <input class="selectedFile" id="selectedFile"  name = "selectedFile" type="file" onclick="fileClicked(event)" onchange="fileChangedTransfer(event)" style="display: none;" aria-label="selectedFile" >
                <a class="btn btn-file-upload btn-secondary" href="javascript:void(0);">Upload</a>
              </div>
              </p>
              <span name="iaisErrorMsg" class="error-msg" id="error_selectedFileError"></span>
            </iais:value>
          </iais:row>
          <iais:row>
           <iais:message key="RFC_ERR0018"></iais:message>
          </iais:row>
          <div class="form-check">
            <input class="form-check-input" id="confirm" type="checkbox" name="confirm" aria-invalid="false" value="1">
            <label class="form-check-label" for="confirm"><span class="check-square"></span>I declare that the information that I have submitted is true</label>
            <p><span name="iaisErrorMsg" class="error-msg" id="error_confirmError"></span></p>
          </div>
        </iais:section>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="container">
      <div class="col-xs-12 col-md-6 text-left">
        <a class="back" href="/hcsa-licence-web/eservice/INTERNET/MohRequestForChange?licenceId=<iais:mask name="licenceId" value="${prepareTranfer.licenceId}"/>"><em class="fa fa-angle-left"></em> Back</a>
      </div>
      <div class="col-xs-12 col-md-6 text-right">
        <a class="btn btn-primary next premiseId" id="Next" href="javascript:void(0);">Next</a>
      </div>
    </div>
  </div>
  <br/>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>




<script>
    $(document).ready(function(){
        $("input[name='premisesInput']").each(function(){
            if('${selectCheakboxs}'.indexOf($(this).val()) != -1){
                $(this).prop("checked",true);
            }
        });
    });
    $("#Next").click(function () {
        showWaiting();
        document.getElementById("mainForm").submit();
    });
    function  showWaiting(){
        $.blockUI({message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}});
    }
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }
    function fileChangedTransfer(event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            fileChanged(event);
        }else{
            fileSelectChange();
        }
    }
    // $('.selectedFile').change(function () {
    //     fileSelectChange();
    // });
    function fileSelectChange(){
        var file = $('#selectedFile');
        file.parent().children('span:eq(0)').html(getFileName(file.val()));
        file.parent().children('span:eq(0)').next().removeClass("hidden");
        file.parent().children('input delFlag').val('N');
        var maxSize = $("#maxFile").val();
        var error  = validateUploadSizeMaxOrEmpty(maxSize,'selectedFile');
        if(error == "N"){
            doDeleteFile();
            $('#error_selectedFileError').html('The file has exceeded the maximum upload size of '+ maxSize + 'M.');
        }
    }
    function doDeleteFile() {
        $('#selectedFile').val("");
    }
    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');

    });

</script>