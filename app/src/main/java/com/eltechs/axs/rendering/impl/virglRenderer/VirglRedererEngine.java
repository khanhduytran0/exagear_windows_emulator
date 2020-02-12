package com.eltechs.axs.rendering.impl.virglRenderer;

import android.opengl.EGL14;
import android.opengl.EGLDisplay;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.rendering.RenderingEngine;

public class VirglRedererEngine implements RenderingEngine {
    private final EGLDisplay eglDisplay = EGL14.eglGetDisplay(0);

    public boolean isRenderingAvailable() {
        return this.eglDisplay != null;
    }

    public String getVendor() {
        Assert.isTrue(this.eglDisplay != null);
        return EGL14.eglQueryString(this.eglDisplay, 12371);
    }

    public String getGLXExtensionsList() {
        Assert.isTrue(this.eglDisplay != null);
        return "GLX_ARB_create_context GLX_ARB_create_context_profile GLX_ARB_create_context_robustness GLX_ARB_fbconfig_float GLX_ARB_framebuffer_sRGB GLX_ARB_multisample GLX_EXT_create_context_es_profile GLX_EXT_create_context_es2_profile GLX_EXT_fbconfig_packed_float GLX_EXT_framebuffer_sRGB GLX_EXT_import_context GLX_EXT_texture_from_pixmap GLX_EXT_visual_info GLX_EXT_visual_rating GLX_MESA_copy_sub_buffer GLX_OML_swap_method GLX_SGI_swap_control GLX_SGIS_multisample GLX_SGIX_fbconfig GLX_SGIX_pbuffer GLX_SGIX_visual_select_group GLX_INTEL_swap_event ";
    }
}
