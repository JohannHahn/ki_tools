-- INIT --
<<<<<<< HEAD
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
=======
function init(character)
	print("Character init")
end

-- UPDATE --
function update(entity)   	
	print(entity.team)
>>>>>>> branch 'master' of https://github.com/wanische/ki_tools.git
end


	
