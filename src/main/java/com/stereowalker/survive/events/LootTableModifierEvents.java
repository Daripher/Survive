package com.stereowalker.survive.events;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.stereowalker.survive.Survive;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "survive")
public class LootTableModifierEvents {
	private static final String ANIMAL_LOOT = "entities/animal_fat";

	private static final List<Pair<ResourceLocation, List<String>>> LOOT_MODIFIERS = Lists.newArrayList(
			Pair.of(new ResourceLocation("entities/sheep"), Lists.newArrayList(ANIMAL_LOOT)),
			Pair.of(new ResourceLocation("entities/chicken"), Lists.newArrayList(ANIMAL_LOOT)),
			Pair.of(new ResourceLocation("entities/cow"), Lists.newArrayList(ANIMAL_LOOT)),
			Pair.of(new ResourceLocation("entities/pig"), Lists.newArrayList(ANIMAL_LOOT))
			);

	@SubscribeEvent
	public static void lootAppend(LootTableLoadEvent evt) {
		LOOT_MODIFIERS.forEach((pair) -> {
			if(evt.getName().equals(pair.getKey())) {
				pair.getValue().forEach((file) -> {
					Survive.getInstance().debug("Injecting "+file+" in "+pair.getKey());
					evt.getTable().addPool(getInjectPool(file));
				});
			}
		});
	}

	private static LootPool getInjectPool(String entryName) {
		return LootPool.builder().addEntry(getInjectEntry(entryName, 1)).bonusRolls(0.0F, 1.0F).name("survive_inject")
				.build();
	}

	private static LootEntry.Builder<?> getInjectEntry(String name, int weight) {
		ResourceLocation table = Survive.getInstance().location("inject/" + name);
		return TableLootEntry.builder(table).weight(weight);
	}
}
