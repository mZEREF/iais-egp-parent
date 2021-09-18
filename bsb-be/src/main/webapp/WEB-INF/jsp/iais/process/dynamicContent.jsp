<div class="alert alert-info" role="alert"><strong>
    <h4>Dynamic Content Update</h4>
</strong></div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <div id="DynamicContent">
                <iais:row>
                    <iais:field value="Dynamic Content" required="false" width="12"/>
                    <iais:value width="10">
                        <div class="input-group">
                            <div class="ax_default text_area">
                                <textarea id="dynamicContentId" name="dynamicContent" cols="70" rows="7" maxlength="4000"></textarea>
                            </div>
                        </div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
    <div align="right">
        <a style="float:left;padding-top: 1.1%;" class="back" href="#" id="contentBack"><em class="fa fa-angle-left"></em> Back</a>
        <button name="preview" id="preview" type="button" class="btn btn-primary" data-toggle="modal" data-target="#preview">Preview</button>
        <button name="reload" id="reload" type="button" class="btn btn-primary" data-toggle="modal" data-target="#reload">Reload</button>
    </div>
</div>