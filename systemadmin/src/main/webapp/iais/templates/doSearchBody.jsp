<div class="row">


    <div class="filter-box form-horizontal">
        <div class="form-group">
            <iais:value>
                <label class="col-xs-12 col-md-4 control-label" for="msgType">Message Type:</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                    <iais:select name="msgType" id="msgType" options="msgType"></iais:select>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-12 col-md-4 control-label" for="templateName">Template Name:</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                    <input id="templateName" name="templateName" type="text" maxlength="500">
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-12 col-md-4 control-label" for="deliveryMode">Delivery Mode:</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                    <iais:select name="deliveryMode" id="deliveryMode" options="deliveryMode"></iais:select>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date:</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                    <iais:datePicker id="esd" name="esd"/>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date:</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                    <iais:datePicker id="eed" name="eed"/>
                </div>
            </iais:value>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-12">
                <div class="text-right"><a class="btn btn-primary" id="ANT_Search">Search</a></div>
            </div>
        </div>
    </div>
</div>