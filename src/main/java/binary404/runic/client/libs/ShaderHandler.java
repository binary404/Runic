package binary404.runic.client.libs;

import binary404.runic.Runic;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.Map;

public class ShaderHandler {
    public enum RunicShader {
        RUNE_MOLDER("shader/passthrough.vert", "shader/rune_molder.frag");

        public final String vertexPath;
        public final String fragmentPath;

        RunicShader(String vertexPath, String fragmentPath) {
            this.vertexPath = vertexPath;
            this.fragmentPath = fragmentPath;
        }

    }

    public static final FloatBuffer FLOAT_BUF = MemoryUtil.memAllocFloat(1);
    private static final Map<RunicShader, ShaderProgram> PROGRAMS = new EnumMap<>(RunicShader.class);

    private static boolean hasIncompatibleMods = false;
    private static boolean checkedIncompatibility = false;

    @SuppressWarnings("deprecation")
    public static void initShaders() {
        if (Minecraft.getInstance() != null
                && Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
                    (IResourceManagerReloadListener) manager -> {
                        PROGRAMS.values().forEach(ShaderLinkHelper::deleteShader);
                        PROGRAMS.clear();
                        loadShaders(manager);
                    });
        }
    }

    private static void loadShaders(IResourceManager manager) {
        if (!useShaders()) {
            return;
        }

        for (RunicShader shader : RunicShader.values()) {
            createProgram(manager, shader);
        }
    }

    public static void useShader(RunicShader shader, @Nullable ShaderCallback callback) {
        if (!useShaders()) {
            return;
        }

        ShaderProgram prog = PROGRAMS.get(shader);
        if (prog == null) {
            return;
        }

        int program = prog.getProgram();
        ShaderLinkHelper.func_227804_a_(program);

        int time = GlStateManager.getUniformLocation(program, "time");
        GlStateManager.uniform1i(time, ClientTickHandler.ticksInGame);

        if (callback != null) {
            callback.call(program);
        }
    }

    public static void useShader(RunicShader shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        ShaderLinkHelper.func_227804_a_(0);
    }

    public static boolean useShaders() {
        return checkIncompatibleMods();
    }

    private static boolean checkIncompatibleMods() {
        if (!checkedIncompatibility) {
            hasIncompatibleMods = ModList.get().isLoaded("optifine");
            checkedIncompatibility = true;
        }

        return !hasIncompatibleMods;
    }

    private static void createProgram(IResourceManager manager, RunicShader shader) {
        try {
            ShaderLoader vert = createShader(manager, shader.vertexPath, ShaderLoader.ShaderType.VERTEX);
            ShaderLoader frag = createShader(manager, shader.fragmentPath, ShaderLoader.ShaderType.FRAGMENT);
            int progId = ShaderLinkHelper.createProgram();
            ShaderProgram prog = new ShaderProgram(progId, vert, frag);
            ShaderLinkHelper.linkProgram(prog);
            PROGRAMS.put(shader, prog);
        } catch (IOException ex) {
            Runic.LOGGER.error("Failed to load program {}", shader.name(), ex);
        }
    }

    private static ShaderLoader createShader(IResourceManager manager, String filename, ShaderLoader.ShaderType shaderType) throws IOException {
        ResourceLocation loc = Runic.key(filename);
        try (InputStream is = new BufferedInputStream(manager.getResource(loc).getInputStream())) {
            return ShaderLoader.func_216534_a(shaderType, loc.toString(), is);
        }
    }

    private static class ShaderProgram implements IShaderManager {
        private final int program;
        private final ShaderLoader vert;
        private final ShaderLoader frag;

        private ShaderProgram(int program, ShaderLoader vert, ShaderLoader frag) {
            this.program = program;
            this.vert = vert;
            this.frag = frag;
        }

        @Override
        public int getProgram() {
            return program;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public ShaderLoader getVertexShaderLoader() {
            return vert;
        }

        @Override
        public ShaderLoader getFragmentShaderLoader() {
            return frag;
        }
    }

}
