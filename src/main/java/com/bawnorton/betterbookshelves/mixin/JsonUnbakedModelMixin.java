package com.bawnorton.betterbookshelves.mixin;

import com.bawnorton.betterbookshelves.render.ChiseledBookshelfModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(JsonUnbakedModel.class)
public class JsonUnbakedModelMixin {
    @Shadow @Final private static Logger LOGGER;

    @Shadow @Nullable protected Identifier parentId;

    @Inject(method ="method_45785", at=@At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"), cancellable = true)
    private void method_45785(Function<Identifier, UnbakedModel> function, CallbackInfo ci) {
        UnbakedModel model = function.apply(parentId);
        if(model != null) {
            LOGGER.info("Model: " + model);
        }
    }
}
