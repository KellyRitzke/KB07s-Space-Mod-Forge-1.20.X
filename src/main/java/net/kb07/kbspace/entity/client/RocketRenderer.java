package net.kb07.kbspace.entity.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.entity.custom.RocketEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RocketRenderer extends EntityRenderer<RocketEntity> {
    private final RocketModel<RocketEntity> model;

    public RocketRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new RocketModel<>(pContext.bakeLayer(ModModelLayers.ROCKET_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(RocketEntity rocketEntity) {
        return new ResourceLocation(KBSpace.MOD_ID, "textures/entity/rocket.png");
    }

    @Override
    public void render(RocketEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pPoseStack.pushPose();

        // Rotate and scale the entity
        pPoseStack.translate(0.0D, 1.5D, 0.0D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntityYaw));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(pEntity)));
        model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

    }


}
