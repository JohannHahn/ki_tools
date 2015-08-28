
name = "Wander"

function init()
end

function enter(entity)
	entity:addComponent(name)
	print(entity.team ,"entered State", name)
end


-- UPDATE --
function update(entity) 
	
	if not entity:gotComponent(name) then
		entity:addComponent(name)
		print(entity.team ,"entered State in update", name)
	end	
	
	local action = "Evade"
	if entity.team:toString() == "RED" then
		action = "Pursuit"
	end
	
	if entity:setTarget(entity:searchTarget(), action) then		
		entity:changeStateByName(action)
	end
	
	if entity:checkFuel() then
		print("hi script here" , entity.team, "getankt")
		entity:removeComponent("Seek")
	end
end


function exit(entity)
	entity:removeComponent(name)
end

function setName(inputName)
	return name
end
	
