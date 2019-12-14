<div class="row">
    <div class="col-xs-2 col-md-2">
        <div class="components">
            <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter">Filter</a>
        </div>
    </div>
    <div class="col-xs-3 col-md-3">
        <div class="components">
            <a class="btn btn-primary" onclick="doCreate()">Create</a>
        </div>
    </div>
    <div id="advfilter" class="collapse">
        <div class="filter-box">
            <h3>Master Code Search</h3>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="codeCategory">Code Category.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="codeCategory" name="codeCategory" type="text">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="description">Code Description.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="description" name="codeDescription" type="text">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="esd" name="esd"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="eed" name="eed"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="status">Status.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="status" type="text" name="codeStatus">
                    </div>
                </iais:value>
            </div>

            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="text-right"><a class="btn btn-primary" id="MC_Search">Search</a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
