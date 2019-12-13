
<c:forEach var="docConfig" items="${serviceDocConfigDto}">
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
          <div class="fileContent col-xs-12 hidden">
            <input type="hidden" name="docConfig" value="ddfb8b26-f449-44e8-b847-657f341fa1d6;0"/>
            <span class="fileName"></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <%--(<span class="fileSize"></span>MB)--%>
          </div>
          <input class="selectedFile" id="selectedFile" name = "${docConfig.id}selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>
        </div>
      </div>
    </div>
  </div>
</div>
</c:forEach>
<script>
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $(this).parent().children('div:eq(0)').children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('div:eq(0)').removeClass('hidden');
    });

</script>