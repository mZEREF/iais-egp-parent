<c:if test="${'APTY005' ==AppSubmissionDto.appType}">
  <c:set var="showPreview" value="true"/>
  <c:forEach var="amendType"  items="${AppSubmissionDto.amendTypes}">
    <c:if test="${amendType =='RFCATYPE06'}">
      <c:set var="canEdit" value="1"/>
    </c:if>
  </c:forEach>
</c:if>
<c:forEach var="docConfig" items="${serviceDocConfigDto}" varStatus="status">
  <c:set var="svcDoc" value="${ReloadSvcDoc[docConfig.id]}" />
  <c:set var="svcDelFlag" value="${docConfig.id}flag"/>
  <div class="row">
    <div class="col-xs-12">
      <h2>${docConfig.docTitle}</h2>
    </div>
    <div class="col-xs-12">
      <p>${docConfig.docDesc}</p>
    </div>
    <div class="col-xs-12">
      <div class="text-center col-xs-12">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent col-xs-12">
              <input class="hidden delFlag" type="hidden" name="${svcDelFlag}" value="N"/>
              <input type="hidden" name="docConfig" value=""/>
              <span class="fileName"><a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}"  >${svcDoc.docName}</a></span>&nbsp;&nbsp;
              <c:choose>
                <c:when test="${svcDoc.docName == '' || svcDoc.docName == null }">
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
                <%--(<span class="fileSize"></span>MB)--%>
            </div>
            <input class="selectedFile" id="selectedFile" name = "${docConfig.id}selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>
            <span id="error_file"${status.index}></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</c:forEach>
<script>
    $(document).ready(function() {
        if('APTY005' == '${AppSubmissionDto.appType}' &&'1' != '${canEdit}'){
            disabledPage();
        }
    });

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $(this).parent().children('div:eq(0)').children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('div:eq(0)').removeClass('hidden');
        $fileUploadContentEle = $(this).closest('div.file-upload-gp');
        $fileUploadContentEle.find('.delBtn').removeClass('hidden');
    });

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');
    });



</script>