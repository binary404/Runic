package binary404.mystica.client.core.handler;

import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class ShaderRenderType extends RenderType {

    private final RenderType delegate;
    private final ShaderHandler.MysticaShader shader;

    @Nullable
    private final ShaderCallback cb;

    public ShaderRenderType(ShaderHandler.MysticaShader shader, @Nullable ShaderCallback cb, RenderType delegate) {
        super("mystica:" + delegate.toString() + "_with_" + shader.name(), delegate.getVertexFormat(), delegate.getDrawMode(), delegate.getBufferSize(), delegate.isUseDelegate(), true,
                () -> {
                    delegate.setupRenderState();
                    ShaderHandler.useShader(shader, cb);
                },
                () -> {
                    ShaderHandler.releaseShader();
                    delegate.clearRenderState();
                });
        this.delegate = delegate;
        this.shader = shader;
        this.cb = cb;
    }

    @Override
    public Optional<RenderType> getOutline() {
        return delegate.getOutline();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof ShaderRenderType
                && delegate.equals(((ShaderRenderType) other).delegate)
                && shader == ((ShaderRenderType) other).shader
                && Objects.equals(cb, ((ShaderRenderType) other).cb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate, shader, cb);
    }

}
