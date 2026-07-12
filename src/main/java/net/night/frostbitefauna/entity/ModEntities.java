package net.night.frostbitefauna.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.DecoyDummyEntity;
import net.night.frostbitefauna.entity.custom.FrozenZombieEntity;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class ModEntities {

    public static final EntityType<GreatEntity> THEGREATWANDERER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(FrostbiteFauna.MOD_ID, "thegreatwanderer"),
            EntityType.Builder.create(GreatEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1.9f,1.75f).build());

    public static final EntityType<DecoyDummyEntity> DECOYDUMMY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(FrostbiteFauna.MOD_ID, "decoydummy"),
            EntityType.Builder.create(DecoyDummyEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.80f,1.5f).build());

    public static final EntityType<FrozenZombieEntity> FROZEN_ZOMBIE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(FrostbiteFauna.MOD_ID, "frozen_zombie"),
            EntityType.Builder.create(FrozenZombieEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f).build());

    public static void registerModEntities() {
        FrostbiteFauna.LOGGER.info("Registering Mod Entities for " + FrostbiteFauna.MOD_ID);
    }
}
