package net.wouterb.blockblock.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.wouterb.blockblock.BlockBlock;
import net.wouterb.blockblock.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if(this.persistentData == null)
            this.persistentData = new NbtCompound();

        return persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound data) {
        this.persistentData = data;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<Boolean> info) {
        if(persistentData != null) {
            nbt.put(BlockBlock.MOD_ID, persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(BlockBlock.MOD_ID)) {
            persistentData = nbt.getCompound(BlockBlock.MOD_ID);
        }
    }
}
