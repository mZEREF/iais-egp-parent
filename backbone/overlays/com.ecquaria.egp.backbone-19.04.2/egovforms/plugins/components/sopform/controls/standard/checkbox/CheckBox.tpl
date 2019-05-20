{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
    <div class="form-horizontal formgap">
        <div class="col-sm-4 control-label">
            <label id="${id}--label"
                    class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>
            <span class="upload_controls"></span>

        </div>
        <div class="col-sm-5">
            <div class="">
                ${renderer._renderInputTable(control, options, id)}
            </div>
            <div id="${id}--errorMsg_right" style="display: none;" class="error_placements"></div>
        </div>
	</div>
</div>
