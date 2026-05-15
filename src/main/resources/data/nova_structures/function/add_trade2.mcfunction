data modify entity @s Offers.Recipes prepend value {buyB:{id:"minecraft:compass",count:1},buy:{id:"minecraft:emerald",count:14},sell:{id:"minecraft:waxed_weathered_cut_copper_stairs",count:1},maxUses:1}

loot replace entity @s weapon.mainhand loot nova_structures:tavern_quest

data modify entity @s Offers.Recipes[0].sell merge from entity @s HandItems[0]

tag @s add trader

