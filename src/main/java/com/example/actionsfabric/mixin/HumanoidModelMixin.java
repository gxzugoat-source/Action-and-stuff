package com.example.actionsfabric.mixin;

import com.example.actionsfabric.ActionsClient;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
        at = @At("TAIL")
    )
    private void actionsfabric$animate(HumanoidRenderState state, CallbackInfo ci) {
        if (state.isFallFlying) return;

        HumanoidModel<?> m = (HumanoidModel<?>) (Object) this;
        float speed = Math.min(1.0f, state.walkAnimationSpeed);
        float pos = state.walkAnimationPos;
        float age = state.ageInTicks;

        if (state.isVisuallySwimming) {
            if (ActionsClient.SWIM_CRAWL) {
                float s = (float) Math.sin(pos * 0.6f);
                m.rightArm.zRot += 0.25f * s;
                m.leftArm.zRot  -= 0.25f * s;
                m.rightArm.yRot += 0.15f * s;
                m.leftArm.yRot  += 0.15f * s;
                float kick = (float) Math.sin(pos * 1.2f) * 0.25f * Math.max(speed, 0.3f);
                m.rightLeg.xRot += kick;
                m.leftLeg.xRot  -= kick;
                m.body.yRot += s * 0.08f;
            }
            return;
        }

        if (state.isCrouching && ActionsClient.SNEAK_POSE) {
            float b = (float) Math.sin(age * 0.08f);
            m.rightArm.zRot += 0.02f * b + 0.03f;
            m.leftArm.zRot  -= 0.02f * b + 0.03f;
            m.head.xRot += 0.01f * b;
            if (speed > 0.05f) {
                float twist = (float) Math.cos(pos * 0.6662f) * 0.06f * speed;
                m.body.yRot += twist;
                m.head.yRot -= twist * 0.6f;
            }
            return;
        }

        if (ActionsClient.IDLE_BREATHING && speed < 0.05f) {
            float b = (float) Math.sin(age * 0.067f);
            m.rightArm.zRot += 0.03f * b + 0.02f;
            m.leftArm.zRot  -= 0.03f * b + 0.02f;
            m.head.xRot     += 0.012f * b;
        }

        if (ActionsClient.WALK_SWAY && speed > 0.05f) {
            float twist = (float) Math.cos(pos * 0.6662f) * 0.12f * speed;
            m.body.yRot  += twist;
            m.head.yRot  -= twist * 0.6f;
            float roll = (float) Math.sin(pos * 0.6662f) * 0.04f * speed;
            m.body.zRot  += roll;
            m.head.zRot  -= roll;
            m.rightArm.zRot += 0.04f * speed;
            m.leftArm.zRot  -= 0.04f * speed;
        }

        if (ActionsClient.SPRINT_LEAN && speed > 0.85f) {
            float lean = 0.18f * (speed - 0.85f) / 0.15f;
            m.body.xRot  += lean;
            m.head.xRot  -= lean * 0.9f;
            m.rightArm.xRot += lean * 0.5f;
            m.leftArm.xRot  += lean * 0.5f;
            m.rightLeg.xRot -= lean * 0.2f;
            m.leftLeg.xRot  -= lean * 0.2f;
        }
    }
      }
