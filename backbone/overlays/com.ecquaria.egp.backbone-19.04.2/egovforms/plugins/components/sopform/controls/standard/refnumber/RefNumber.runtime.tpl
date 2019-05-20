{var id = control.getElementId()}
<div id="${id}" class="control">
    <div class="form-horizontal formgap">
        <div class="col-sm-4 control-label formtext">
            <label id="${id}**label"
                    for="${id}**text"
                    class="control-label control-set-font control-font-label">
                ${control.properties.caption|escape}
            </label>
            <span class="upload_controls"></span>
        </div>
        <div class="col-sm-5" style="margin-top: 5px;">
            <span class="control-set-font control-font-normal">
                {if Form.instanceMeta && Form.instanceMeta.refNumber}
                    ${Form.instanceMeta.refNumber}
                {else}
                    [auto-generated]
                {/if}
            </span>
        </div>
	</div>
</div>