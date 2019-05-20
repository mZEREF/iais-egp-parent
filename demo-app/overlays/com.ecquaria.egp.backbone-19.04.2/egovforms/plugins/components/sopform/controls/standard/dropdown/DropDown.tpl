{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
    <div class="form-horizontal formgap">
        <div class="col-sm-4 control-label formtext">
            <label id="${id}--label" class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>
            <span class="upload_controls"></span>
        </div>
        <div class="col-sm-5">
            <div class="">
                <select id="${id}--select" class="form-control control-input control-set-font control-font-normal">
                {for row in options}
                    <option value="${row.code|escape}"
                            {if control.properties.selected === row.code} selected {/if}>
                        ${row.description|escape}
                    </option>
                {/for}
                </select>

                <div id="${id}--errorMsg_right" style="display: none;" class="error_placements"></div>
            </div>
        </div>
	</div>
</div>
