<div class="row">
        <div class="col-xs-12">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="letterTitle">Title</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <input maxLength="66" type="text" autocomplete="off" name="letterTitle" id="letterTitle" value='<c:out value="${letterDto.letterTitle}"/>'/>
                        <span data-err-ind="letterTitle" class="error-msg"></span>
                    </div>
                    <div class="clear"></div>
                </div>

                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="htmlEditroArea">Content</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <textarea name="letterContent" cols="50" rows="30"
                                  id="htmlEditroArea"
                                  title="content">${letterDto.letterContent}</textarea>
                        <span data-err-ind="letterContent" class="error-msg"></span>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
<div class="row">
    <div class="col-xs-12">
        <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
        <div style="text-align: right">
            <button name="previewBtn" id="previewBtn" type="button" class="btn btn-secondary">PREVIEW</button>
        </div>
    </div>
</div>