package binary404.runic.client.libs;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

public class RenderTypes {

    static RenderState.TextureState mipmapBlockAtlasTexture = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, true);
    static RenderState.AlphaState oneTenthAlpha = new RenderState.AlphaState(0.004F);
    static RenderState.LightmapState enableLightmap = new RenderState.LightmapState(true);

    static RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228515_g_");

    public static RenderType orb;

    public static final VertexFormat POSITION_TEX_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_3F).add(TEX_2F).add(NORMAL_3B).add(PADDING_1B).build());

    static {
        RenderType.State glState = RenderType.State.getBuilder().texture(new RenderState.TextureState(new ResourceLocation("runic", "textures/entity/orbs.png"), false, false)).transparency(TRANSLUCENT_TRANSPARENCY).build(true);
        orb = RenderType.makeType("runic:" + "orb", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256, glState);
    }

    public static RenderType getTEOverlay(ResourceLocation location) {
        return new ShaderRenderType(ShaderHandler.RunicShader.RUNE_MOLDER, null, RenderType.makeType("runic:te_overlay", POSITION_COLOR_TEX, GL11.GL_QUADS, 128, RenderType.State.getBuilder().texture(new RenderState.TextureState(location, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).alpha(oneTenthAlpha).lightmap(enableLightmap).build(true)));
    }
}
