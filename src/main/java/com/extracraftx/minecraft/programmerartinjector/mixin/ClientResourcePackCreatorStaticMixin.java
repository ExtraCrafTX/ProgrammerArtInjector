package com.extracraftx.minecraft.programmerartinjector.mixin;

import java.io.File;

import com.extracraftx.minecraft.programmerartinjector.ProgrammerArtResourcePack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import net.minecraft.resource.ResourcePack;

@Mixin(ClientBuiltinResourcePackProvider.class)
public class ClientResourcePackCreatorStaticMixin{

    @Inject(method = "method_16048", at=@At("HEAD"), cancellable = true)
    private static void onCreateProgrammerResourcePack(File file, CallbackInfoReturnable<ResourcePack> info){
        info.setReturnValue(new ProgrammerArtResourcePack(file));
    }

}