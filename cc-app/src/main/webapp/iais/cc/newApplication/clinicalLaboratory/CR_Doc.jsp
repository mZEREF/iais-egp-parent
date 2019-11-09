<div class="row">
  <div class="col-xs-12">
    <h2>[Sample Document]</h2>
  </div>
  <div class="col-xs-12">
    <p>[description of the document/instruction for retrieving the document]</p>
  </div>
  <div class="col-xs-12">
    <%--<div class="updateContent col-xs-12 hidden">
      &lt;%&ndash;<input type="text" value="sample.pdf"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="text" value="(3.2MB)"/>&ndash;%&gt;
      <label>sample.pdf</label>&nbsp;&nbsp;&nbsp;&nbsp;<label>(3.2MB)</label>
    </div>--%>
    <div class="text-center col-xs-12">
      <div class="document-upload-list">
        <div class="file-upload-gp">
          <div class="fileContent col-xs-12 hidden">
            <span class="fileName">11</span>&nbsp;&nbsp;&nbsp;&nbsp;
            (<span class="fileSize">3.2</span>MB)
          </div>
          <input class="selectedFile" id="selectedFile" name = "selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>
        </div>
      </div>
    </div>
  </div>

</div>

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