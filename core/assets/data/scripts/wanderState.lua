-- INIT --
function init(name)
	name="wanderState"
end

-- UPDATE --
function update(entity)   	
	print(entity.team)
	entity:setState("string");
	if entity.team:toString()=="GREEN" then
	print("true")
	entity.stateMachine.changeState(BoidState.NO_TARGET);
	end
end


	
