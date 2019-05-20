{var id = control.getElementId()}
<div id="${id}" class="control">
	<div class="control-label-span control-set-alignment">
		<label id="${id}--label"
				class="control-label control-set-font control-font-label">
			${control.properties.caption|escape}
		</label>
	</div>
	<div class="control-input-span control-set-alignment">
		<div class="normal-indicator">
			<span class="control-set-font control-font-normal">[auto-generated]</span>
		</div>
	</div>
</div>