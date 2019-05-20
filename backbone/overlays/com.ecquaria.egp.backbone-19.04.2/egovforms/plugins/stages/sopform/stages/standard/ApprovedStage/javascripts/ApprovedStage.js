var FQN = "sopform.stages.standard.ApprovedStage";

FormFlow.ApprovedStage = FormFlow.Stage.extend({
	FQN: FQN
},{});

FormFlow.registerStageType(FQN, FormFlow.ApprovedStage);