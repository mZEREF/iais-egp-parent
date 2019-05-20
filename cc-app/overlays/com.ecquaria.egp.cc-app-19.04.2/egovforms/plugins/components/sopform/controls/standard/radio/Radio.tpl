{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
	<div class="control-label-span control-set-alignment">
		<label id="${id}--label"
				class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>
		<span class="upload_controls"></span>
		
	</div>
	<div class="control-input-span control-set-alignment">
		<div class="normal-indicator">
		${renderer._renderInputTable(control, options, id)}
		</div>
		<div id="${id}--errorMsg_right" style="display: none;" class="error_placements"></div>
	</div>
</div>
