<div class="row">
        <div class="col-xs-12">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="emailTitle">Title</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <input maxLength="66" type="text" autocomplete="off" name="emailTitle" id="emailTitle" value='<c:out value="${letterDto.emailTitle}"/>'/>
                        <span data-err-ind="emailTitle" class="error-msg"></span>
                    </div>
                    <div class="clear"></div>
                </div>

                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="htmlEditroArea">Content</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <textarea name="emailContent" cols="50" rows="30"
                                  id="htmlEditroArea"
                                  title="content">${letterDto.emailContent}</textarea>
                        <span data-err-ind="emailContent" class="error-msg"></span>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
<div class="row">
    <div class="col-xs-12">
        <a style="float:left;padding-top: 1.1%;" class="back" href="#"><em class="fa fa-angle-left"></em>< Previous</a>
        <div style="text-align: right">
            <button name="previewBtn" id="previewBtn" type="button" class="btn btn-secondary">PREVIEW</button>
        </div>
    </div>
</div>