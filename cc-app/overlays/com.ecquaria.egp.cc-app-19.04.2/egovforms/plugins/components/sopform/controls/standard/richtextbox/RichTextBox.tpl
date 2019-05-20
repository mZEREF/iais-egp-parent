{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass} richtextbox">
	<div class="control-label-span control-set-alignment">
		<label id="${id}--label"
				class="control-label control-set-font control-font-label">
			${control.properties.caption|escape}
		</label>
		<span class="upload_controls"></span>
	</div>
	<div class="control-input-span control-set-alignment">
		<div class="normal-indicator">
			<textarea id="${id}--text" class="control-input"
					{if control.properties.width}style="width: ${control.properties.width}"{/if}
					>${control.properties.text|h}</textarea>
			<div id="${id}--errorMsg_right" style="display: none;" class="error_placements"></div>
		</div>
	</div>
</div>