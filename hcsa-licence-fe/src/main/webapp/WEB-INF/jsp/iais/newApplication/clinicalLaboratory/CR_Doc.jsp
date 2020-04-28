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
    <p class="text-right"><a class="back" id="RfcSkip">Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em></a></p>
  </c:if>
  <c:if test="${'true' != isClickEdit}">
    <c:set var="locking" value="true"/>
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    <div id="edit-content">
      <c:choose>
        <c:when test="${'true' == canEdit}">
          <p class="text-right"><a id="edit"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a></p>
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
      <h3>${docConfig.docTitle}</h3>
    </div>
    <div class="col-xs-12">
      <p>${docConfig.docDesc}</p>
    </div>
    <div class="col-xs-12">
      <div class="">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent" style="margin-bottom:1%;">
              <input class="hidden delFlag" type="hidden" name="${svcDelFlag}" value="N"/>
              <input type="hidden" name="docConfig" value=""/>
              <span class="fileName"><a class="fileName <c:if test="${!isClickEdit}">disabled</c:if>" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}"  >${svcDoc.docName}</a></span>&nbsp;&nbsp;
              <c:choose>
                <c:when test="${svcDoc.docName == '' || svcDoc.docName == null }">
                                                    <span class="hidden delBtn">
                                                      &nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>
                                                    </span>
                </c:when>
                <c:otherwise>
                                                      <span class="existFile delBtn <c:if test="${!isClickEdit}">hidden</c:if>">
                                                        &nbsp;&nbsp;<button type="button" class="btn btn-danger btn-sm"><i class="fa fa-times"></i></button>
                                                    </span>
                </c:otherwise>
              </c:choose>
            </div>
            <input class="selectedFile" id="selectedFile" name = "${docConfig.id}selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>

            <c:if test="${svcDoc.docName!=null}">
              <span name="iaisErrorMsg" class="error-msg" id="error_${docConfig.id}selectedFile"></span>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</c:forEach>
<script>
    $(document).ready(function() {
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            disabledPage();
        }

        doEdit();
    });

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    $('.selectedFile').change(function () {
        var file = $(this).val();
        var documentDiv = $(this).closest('.document-upload-list');
        documentDiv.find('.fileName').html(getFileName(file));
        documentDiv.find('.fileName span:eq(1)').html('&nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>');
        documentDiv.find('.fileName span:eq(1)').removeClass('hidden');
        var $fileUploadContentEle = $(this).closest('div.file-upload-gp');
        $fileUploadContentEle.find('.delBtn').removeClass('hidden');
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(1)').html('');
        $(this).parent().children('span:eq(1)').next().addClass("hidden");
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
        });
    }

</script>