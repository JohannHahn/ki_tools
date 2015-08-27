-- INIT --

function init(name)
	name="StateExample"
	--print(entity.team ,"entered State", name)--
end

-- UPDATE --
function update(entity)   	
	
	
	entity:switchTeams();
	
end


function setName(name)
	name = "StateExample"
	return name
end
	
	
