package binary404.runic.client.libs;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

public class RenderTypes {

    public static final VertexFormat POSITION_TEX_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_3F).add(TEX_2F).add(NORMAL_3B).add(PADDING_1B).build());

    public static final RenderType OBJ = RenderType.makeType("runic:obj", POSITION_TEX_NORMAL, GL11.GL_QUADS, 256, false, true, RenderType.State.getBuilder().texture(new RenderState.TextureState()).build(false));

}
