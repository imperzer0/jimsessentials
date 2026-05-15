data modify entity @s Offers.Recipes prepend value {buyB:{id:"minecraft:compass",count:1},buy:{id:"minecraft:emerald",count:14},sell:{id:"minecraft:paper",count:1},maxUses:1}

loot replace entity @s weapon.mainhand loot nova_structures:tavern_quest

data modify entity @s Offers.Recipes[0].sell merge from entity @s HandItems[0]

effect give @s regeneration 30 1

execute as @s run function nova_structures:add_trade2