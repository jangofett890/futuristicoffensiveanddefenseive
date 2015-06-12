package futuristicoffensiveanddefenseive.theneonfish.fod.client.render;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class FODTESR extends TileEntitySpecialRenderer{
    
    public FODTESR(){
        // initialize anything that will be needed later by the renderer,
        // like textures and custom 3d models
    }
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        // locationBlocksTexture is a "ResourceLocation" that points to a texture made of many block "icons".
        // It will look very ugly, but creating our own ResourceLocation is beyond the scope of this tutorial.
        this.bindTexture(TextureMap.locationBlocksTexture);

        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y+1, z); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, 1, 0, 0, 1);
        tessellator.addVertexWithUV(1, 1, 0, 1, 1);
        tessellator.addVertexWithUV(1, 0, 0, 1, 0);

        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(1, 0, 0, 1, 0);
        tessellator.addVertexWithUV(1, 1, 0, 1, 1);
        tessellator.addVertexWithUV(0, 1, 0, 0, 1);

        tessellator.draw();
        GL11.glPopMatrix();
    }
}