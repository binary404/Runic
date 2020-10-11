package binary404.mystica.client.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

public class TexturedQuadMystica {

    ModelRenderer.PositionTextureVertex[] vertexPositions;
    public int nVertices;

    public TexturedQuadMystica(ModelRenderer.PositionTextureVertex[] vertices) {
        this.flipped = false;


        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }

    private boolean invertNormal;
    private boolean flipped;

    public TexturedQuadMystica(ModelRenderer.PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
        this(vertices);
        float f2 = 0.0F / textureWidth;
        float f3 = 0.0F / textureHeight;
        vertices[0] = vertices[0].setTextureUV(texcoordU2 / textureWidth - f2, texcoordV1 / textureHeight + f3);
        vertices[1] = vertices[1].setTextureUV(texcoordU1 / textureWidth + f2, texcoordV1 / textureHeight + f3);
        vertices[2] = vertices[2].setTextureUV(texcoordU1 / textureWidth + f2, texcoordV2 / textureHeight - f3);
        vertices[3] = vertices[3].setTextureUV(texcoordU2 / textureWidth - f2, texcoordV2 / textureHeight - f3);
    }


    public void flipFace() {
        this.flipped = true;
        ModelRenderer.PositionTextureVertex[] apositiontexturevertex = new ModelRenderer.PositionTextureVertex[this.vertexPositions.length];

        for (int i = 0; i < this.vertexPositions.length; i++) {
            apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        }

        this.vertexPositions = apositiontexturevertex;
    }


    public void draw(BufferBuilder renderer, float scale, int bright, int color, float alpha) {
        if (bright != -99) {
            renderer.begin(7, RenderingUtils.VERTEXFORMAT_POS_TEX_CO_LM_NO);
        } else {
            renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        }
        Color c = new Color(color);

        int aa = bright;
        int j = aa >> 16 & 0xFFFF;
        int k = aa & 0xFFFF;

        for (int i = 0; i < 4; i++) {

            ModelRenderer.PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            if (bright != -99) {
                renderer
                        .pos(positiontexturevertex.position.getX() * scale, positiontexturevertex.position.getY() * scale, positiontexturevertex.position.getZ() * scale)
                        .tex(positiontexturevertex.textureU, positiontexturevertex.textureV)
                        .color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255.0F))
                        .lightmap(j, k)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
            } else {
                renderer
                        .pos(positiontexturevertex.position.getX() * scale, positiontexturevertex.position.getY() * scale, positiontexturevertex.position.getZ() * scale)
                        .tex(positiontexturevertex.textureU, positiontexturevertex.textureV)
                        .color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255.0F))
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
            }
        }

        Tessellator.getInstance().draw();
    }


    public void draw(BufferBuilder renderer, float scale, int bright, int[] color, float[] alpha) {
        if (bright != -99) {
            renderer.begin(7, RenderingUtils.VERTEXFORMAT_POS_TEX_CO_LM_NO);
        } else {
            renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        }
        int aa = bright;
        int j = aa >> 16 & 0xFFFF;
        int k = aa & 0xFFFF;

        for (int i = 0; i < 4; i++) {

            int idx = this.flipped ? (3 - i) : i;
            Color c = new Color(color[idx]);
            ModelRenderer.PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            if (bright != -99) {
                renderer
                        .pos(positiontexturevertex.position.getX() * scale, positiontexturevertex.position.getY() * scale, positiontexturevertex.position.getZ() * scale)
                        .tex(positiontexturevertex.textureU, positiontexturevertex.textureV)
                        .color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha[idx] * 255.0F))
                        .lightmap(j, k)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
            } else {
                renderer
                        .pos(positiontexturevertex.position.getX() * scale, positiontexturevertex.position.getY() * scale, positiontexturevertex.position.getZ() * scale)
                        .tex(positiontexturevertex.textureU, positiontexturevertex.textureV)
                        .color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha[idx] * 255.0F))
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
            }
        }

        Tessellator.getInstance().draw();
    }

}
