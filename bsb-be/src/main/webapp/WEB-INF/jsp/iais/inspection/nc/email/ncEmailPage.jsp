<div class="row">
        <div class="col-xs-12">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="title">Title</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <input maxLength="66" type="text" autocomplete="off" name="title" id="title" value='<c:out value="${ncEmailDto.title}"/>'/>
                        <span data-err-ind="title" class="error-msg"></span>
                    </div>
                    <div class="clear"></div>
                </div>

                <div class="form-group">
                    <label class="col-xs-12 col-md-2 control-label" for="htmlEditroArea">Content</label>
                    <div class="col-sm-7 col-md-10 col-xs-10">
                        <textarea name="content" cols="50" rows="30"
                                  id="htmlEditroArea"
                                  title="content">${ncEmailDto.content}</textarea>
                        <span data-err-ind="content" class="error-msg"></span>
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