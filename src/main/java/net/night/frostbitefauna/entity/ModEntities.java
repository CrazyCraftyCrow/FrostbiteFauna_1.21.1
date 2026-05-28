package net.night.frostbitefauna.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class ModEntities {

    public static final EntityType<GreatEntity> THEGREATWANDERER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(FrostbiteFauna.MOD_ID, "thegreatwanderer"),
            EntityType.Builder.create(GreatEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1.9f,1.75f).build());

    public static void registerModEntities() {
        FrostbiteFauna.LOGGER.info("Registering Mod Entities for " + FrostbiteFauna.MOD_ID);
    }
}
