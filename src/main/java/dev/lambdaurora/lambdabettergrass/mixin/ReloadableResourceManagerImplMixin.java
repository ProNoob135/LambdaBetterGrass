/*
 * Copyright © 2021 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambdaBetterGrass.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdabettergrass.mixin;

import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import dev.lambdaurora.lambdabettergrass.resource.LBGResourcePack;
import net.minecraft.resource.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class ReloadableResourceManagerImplMixin implements ReloadableResourceManager {
	@Shadow
	@Final
	private ResourceType type;

	@Shadow
	public abstract void addPack(ResourcePack resourcePack);

	@Inject(method = "reload", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
	private void onReload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs,
	                      CallbackInfoReturnable<ResourceReload> cir) {
		if (this.type != ResourceType.CLIENT_RESOURCES)
			return;

		var mod = LambdaBetterGrass.get();
		mod.log("Inject generated resource packs.");
		this.addPack(mod.resourcePack = new LBGResourcePack(mod));
	}
}
