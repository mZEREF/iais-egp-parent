{var id = control.getElementId()}
{var type = control.properties.type}
<div id="${id}" class="control">
	<div class="control-label-span control-set-alignment">
		<label id="${id}**label"
				class="${type} control-label control-set-font">
			[session:${control.properties.key|default:''|escape}]
		</label>
	</div>
</div>
