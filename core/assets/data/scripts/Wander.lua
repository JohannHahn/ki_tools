-- INIT --

function init(name)
	name="wanderState"
	print(entity.team ,"entered State", name)
end

-- UPDATE --
function update(entity)   	
	
	entity:setState("string");
	
	
	entity.stateMachine.changeState(BoidState.WANDER);
	
end

function setName(name)
	name = "wanderState"
	return name
end
	
