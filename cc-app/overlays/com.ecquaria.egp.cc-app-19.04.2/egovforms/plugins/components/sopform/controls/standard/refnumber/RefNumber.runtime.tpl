{var id = control.getElementId()}
<div id="${id}" class="control">
	<div class="control-label-span control-set-alignment">
		<label id="${id}**label"
				for="${id}**text"
				class="control-label control-set-font control-font-label">
			${control.properties.caption|escape}
		</label>
		<span class="upload_controls"></span>
	</div>
	<div class="control-input-span control-set-alignment">
		<span class="control-set-font control-font-normal">
			{if Form.instanceMeta && Form.instanceMeta.refNumber}
				${Form.instanceMeta.refNumber}
			{else}
				[auto-generated]
			{/if}
		</span>
	</div>
</div>