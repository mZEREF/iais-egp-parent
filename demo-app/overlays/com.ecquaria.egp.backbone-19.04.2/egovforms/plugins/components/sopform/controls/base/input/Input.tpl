{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
<div class="form-group form-horizontal control-set-alignment formgap">
	<div class="col-sm-4 control-label formtext">
		<label id="${id}--label">${control.properties.caption|escape}</label>
		<span class="upload_controls"></span>
	</div>
	<div class="col-sm-5" style="">
		<div class="normal-indicator">
			<label id="${id}--text" class="control-input control-set-font control-font-normal">
			{if control.properties.text}
				${control.properties.text|escape}
			{/if}
			</label>
			<div class="control-item-aux-container" style="display:none;">
			</div>
		</div>
	</div>
</div>
</div>