package com.fletime.jadesulfuradd;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class JadeSulfurAdd implements ClientModInitializer, IWailaPlugin {
	public static final String MOD_ID = "jadesulfuradd";

	@Override
	public void onInitializeClient() {
	}

	// 在 Jade 里边注册实体组件
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(SulfurCubeArchetypeProvider.INSTANCE, SulfurCube.class);
	}
}
