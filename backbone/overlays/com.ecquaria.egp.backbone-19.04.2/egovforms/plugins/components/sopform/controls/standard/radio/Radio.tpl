{var id = control.getElementId(index)}
<div id="${id}" class="form-group form-horizontal control ${control.properties.developerclass}">
    <div class="form-horizontal formgap">
        <div class="col-sm-4 control-label formtext ">
            <label id="${id}--label"
                    class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>
            <span class="upload_controls"></span>

        </div>
        <div class="col-sm-5 radioselect">
            <div class="">
            ${renderer._renderInputTable(control, options, id)}
            <div id="${id}--errorMsg_right" style="display: none;float:left" class="error_placements"></div>
            </div>
        </div>
	</div>
</div>
