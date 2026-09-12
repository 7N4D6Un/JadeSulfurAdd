package com.fletime.jadesulfuradd;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SulfurCubeArchetypeProvider implements IEntityComponentProvider {
	public static final SulfurCubeArchetypeProvider INSTANCE = new SulfurCubeArchetypeProvider();
	private static final String ARCHE_TYPE_KEY_PREFIX = "jadesulfuradd.archetype.";

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		SulfurCube sulfurCube = (SulfurCube) accessor.getEntity();
		ItemStack body = sulfurCube.getItemBySlot(EquipmentSlot.BODY);
		if (body.isEmpty()) {
			return;
		}
		List<Identifier> locations = resolveArchetypeLocations(sulfurCube, body);

		// 没有匹配到任何原型
		if (locations.isEmpty()) {
			tooltip.add(Component.translatable("jadesulfuradd.tooltip.archetype",
					Component.translatableWithFallback(ARCHE_TYPE_KEY_PREFIX + "default", "default")));
			return;
		}
		tooltip.add(Component.translatable("jadesulfuradd.tooltip.archetype", joinArchetypeNames(locations)));
	}

	// 如果原型不止一个
	private static Component joinArchetypeNames(List<Identifier> locations) {
		MutableComponent joined = Component.empty();
		for (int i = 0; i < locations.size(); i++) {
			if (i > 0) {
				joined.append(" | ");
			}
			String path = locations.get(i).getPath();
			joined.append(Component.translatableWithFallback(ARCHE_TYPE_KEY_PREFIX + path, path));
		}
		return joined;
	}

	// 在当前注册表中查找 items 包含 body 物品的所有原型
	private static List<Identifier> resolveArchetypeLocations(SulfurCube sulfurCube, ItemStack body) {
		Optional<Registry<SulfurCubeArchetype>> lookup = sulfurCube.level().registryAccess().lookup(Registries.SULFUR_CUBE_ARCHETYPE);
		return lookup.map(registry -> registry.listElements()
				.filter(reference -> body.is(reference.value().items()))
				.map(reference -> reference.key().identifier())
				.toList()).orElse(List.of());
	}

	@Override
	public Identifier getUid() {
		return Identifier.fromNamespaceAndPath(JadeSulfurAdd.MOD_ID, "sulfur_cube_archetype");
	}
}
