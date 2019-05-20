<div class="property-container">

		<div id="div_change" style="display:none">
			<label class="page-edit-label">On Value Change</label>
			<div style="padding:3px 0 0 130px;">
				<textarea id=script_input>{if value !== undefined}${value|escape}{/if}</textarea>
				<br/>
				Key in JavaScript here. You will be given the JavaScript Event object "e".
			</div>
		</div>
		
		<div id="div_click" style="display:none">
			<label class="page-edit-label">On Click</label>
			<div style="padding:3px 0 0 130px;">
				<textarea id=script_input_click>{if click_value !== undefined}${click_value|escape}{/if}</textarea>
				<br />
				Key in JavaScript here.
			</div>
		</div>
		
		<div id="div_css" style="display:none">
			<label class="page-edit-label">CSS</label>
			<input id="css_input" type="text" value="{if developer_class !== undefined}${developer_class|escape}{/if}" />
		</div>
</div>