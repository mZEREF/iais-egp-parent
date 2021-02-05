<input type="hidden" name="sysFileSize" id="sysFileSize" value="${sysFileSize}"/>
<c:if test="${requestInformationConfig == null}">
  <c:set var="isClickEdit" value="true"/>
</c:if>
<c:if test="${AppSubmissionDto.needEditController}">
  <c:set var="isClickEdit" value="false"/>
  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
    <c:if test="${'APPSPN06' == clickEditPage}">
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
  <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
    <p><div class="text-right app-font-size-16"><a class="back" id="RfcSkip">Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em></a></div></p>
  </c:if>
  <c:if test="${'true' != isClickEdit}">
    <c:set var="locking" value="true"/>
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    <div id="edit-content">
      <c:choose>
        <c:when test="${'true' == canEdit}">
          <p><div class="text-right app-font-size-16"><a id="edit"><em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit</a></div></p>
        </c:when>
        <c:otherwise>

        </c:otherwise>
      </c:choose>
    </div>
  </c:if>
</c:if>
<c:forEach var="docConfig" items="${serviceDocConfigDto}" varStatus="status">
  <c:set var="fileIndex" value="${status.index}"></c:set>

  <c:set var="svcDoc" value="${ReloadSvcDoc[docConfig.id]}" />
  <c:set var="svcDelFlag" value="${docConfig.id}flag"/>
  <div class="row" style="margin-bottom:2%;">
    <div class="col-xs-12">
      <h3>${docConfig.docTitle}<c:if test="${docConfig.isMandatory}">&nbsp;<span class="mandatory">*</span></c:if></h3>
    </div>
    <%--<div class="col-xs-12">
      <p>${docConfig.docDesc}</p>
    </div>--%>
    <div class="col-xs-12">
      <div class="">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent" style="margin-bottom:1%;">
              <input class="hidden delFlag" type="hidden" name="${svcDelFlag}" value="N"/>
              <input type="hidden" name="docConfig" value=""/>
              <span class="fileNameSpan">
                <c:choose>
                  <c:when test="${!svcDoc.passValidate}">
                    ${svcDoc.docName}
                  </c:when>
                  <c:otherwise>
                    <a class="fileName <c:if test="${!isClickEdit}">disabled</c:if>" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}"  >${svcDoc.docName}</a>
                  </c:otherwise>
                </c:choose>
              </span>&nbsp;&nbsp;
              <c:choose>
                <c:when test="${svcDoc.docName == '' || svcDoc.docName == null }">
                  <span class="hidden delBtn">
                    &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                  </span>
                </c:when>
                <c:otherwise>
                  <span class="existFile delBtn <c:if test="${!isClickEdit}">hidden</c:if>">
                    &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                  </span>
                </c:otherwise>
              </c:choose>
            </div>
            <input class="selectedFile svcDoc"  name = "${docConfig.id}selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a><br/>
            <span name="iaisErrorMsg" class="error-msg" id="error_${docConfig.id}selectedFile"></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</c:forEach>

<%--prem svc doc--%>
<c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStatus">
  <c:set var="premIndexNo" value="${prem.premisesIndexNo}" />
  <c:forEach var="premDocConfig" items="${premServiceDocConfigDto}" varStatus="status">
    <c:set var="premKey" value="prem${premDocConfig.id}${premIndexNo}" />
    <c:set var="premSvcDoc" value="${ReloadSvcDoc[premKey]}" />
    <c:set var="premDelFlag" value="prem${premDocConfig.id}${premIndexNo}flag"/>

    <div class="row" style="margin-bottom:2%;">
      <div class="col-xs-12">
        <h3>premises${premStatus.index+1}: ${premDocConfig.docTitle}<c:if test="${premDocConfig.isMandatory}">&nbsp;<span class="mandatory">*</span></c:if></h3>
      </div>
      <%--<div class="col-xs-12">
        <p>${premDocConfig.docDesc}</p>
      </div>--%>
      <div class="col-xs-12">
        <div class="">
          <div class="document-upload-list">
            <div class="file-upload-gp">
              <div class="fileContent" style="margin-bottom:1%;">
                <input class="hidden delFlag" type="hidden" name="${premDelFlag}" value="N"/>
                <input type="hidden" name="premDocConfig" value=""/>
                <span class="fileNameSpan">
                <c:choose>
                  <c:when test="${!premSvcDoc.passValidate}">
                    ${premSvcDoc.docName}
                  </c:when>
                  <c:otherwise>
                    <a class="fileName <c:if test="${!isClickEdit}">disabled</c:if>" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${premSvcDoc.fileRepoId}"/>&fileRepoName=${premSvcDoc.docName}"  >${premSvcDoc.docName}</a>
                  </c:otherwise>
                </c:choose>
              </span>&nbsp;&nbsp;
                <c:choose>
                  <c:when test="${premSvcDoc.docName == '' || premSvcDoc.docName == null }">
                    <span class="hidden delBtn">
                      &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                    </span>
                  </c:when>
                  <c:otherwise>
                      <span class="existFile delBtn <c:if test="${!isClickEdit}">hidden</c:if>">
                        &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                    </span>
                  </c:otherwise>
                </c:choose>
              </div>
              <input class="selectedFile svcDoc"  name = "${premKey}" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a><br/>
              <span name="iaisErrorMsg" class="error-msg" id="error_${premKey}"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:forEach>
</c:forEach>


<script>
    $(document).ready(function() {
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            disabledPage();
            $('.btn-file-upload').addClass('hidden');
        }

        doEdit();
    });

    <!-- 108635 start-->
    // FileChanged()
    function fileChangedLocal(obj,event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            fileChanged(event);
        }else{
            var file = obj.value;
            if(file != null && file != '' && file != undefined){
                var documentDiv = $(obj).closest('.document-upload-list');
                documentDiv.find('.fileNameSpan').html(getFileName(file));
                documentDiv.find('.delBtn').html('&nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>');
                documentDiv.find('.delBtn').removeClass('hidden');
                var $fileUploadContentEle = $(obj).closest('div.file-upload-gp');
                $fileUploadContentEle.find('.delBtn').removeClass('hidden');
            }
        }
    }
    <!-- 108635 end-->

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    /*$('.selectedFile').change(function () {
        var file = $(this).val();
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileNameSpan').html(getFileName(file));
        documentDiv.find('.delBtn').html('&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-sm"><em class="fa fa-times"></em></button>');
        documentDiv.find('.delBtn').removeClass('hidden');
        var $fileUploadContentEle = $(this).closest('div.file-upload-gp');
        $fileUploadContentEle.find('.delBtn').removeClass('hidden');
    });*/

    $('.delBtn').click(function () {
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileNameSpan').html('');
        documentDiv.find('.delBtn').html('');
        documentDiv.find('.delBtn').addClass('hidden');
        documentDiv.find('input.svcDoc').val('');
        $(this).closest('.fileContent').find('input.selectedFile').val('');
        $(this).closest('.fileContent').find('input.delFlag').val('Y');
    });

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('input[type="file"]').prop('disabled',false);
            $('.existFile').removeClass('hidden');
            $('.existFile').removeClass('existFile');
            $('.btn-file-upload').removeClass('hidden');
        });
    }

    $('.svcDoc').change(function () {
        var maxFileSize = $('#sysFileSize').val();
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, $(this));
        if (error == "N"){
            $(this).closest('.document-upload-list').find('.error-msg').html('The file has exceeded the maximum upload size of '+ maxFileSize + 'M.');
            $(this).closest('.document-upload-list').find('span.delBtn').trigger('click');
            dismissWaiting();
        }else{
            $(this).closest('.document-upload-list').find('.error-msg').html('');
            dismissWaiting();
        }

    });

    function validateUploadSizeMaxOrEmpty(maxSize,$fileEle) {
        var fileV = $fileEle.val();
        var file = $fileEle.get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            return "N";
        }
        return "Y";
    }
</script>