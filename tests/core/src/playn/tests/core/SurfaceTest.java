//
// $Id$

package playn.tests.core;

import playn.core.ImmediateLayer;
import playn.core.Surface;
import playn.core.Pattern;
import static playn.core.PlayN.*;

public class SurfaceTest extends Test {
  @Override
  public String getName() {
    return "SurfaceTest";
  }

  @Override
  public String getDescription() {
    return "Tests various Surface rendering features.";
  }

  @Override
  public void init() {
    int samples = 128; // big enough to force a buffer size increase
    final float[] verts = new float[(samples+1)*4];
    final int[] indices = new int[samples*6];
    tessellateCurve(0, 40*(float)Math.PI, verts, indices, new F() {
      public float apply (float x) { return (float)Math.sin(x/20)*50; }
    });
    final Pattern pattern = graphics().createPattern(assets().getImage("images/tile.png"));

    ImmediateLayer unclipped = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
      public void render (Surface surf) {
        drawLine(surf, 10, 50, 60, 100, 15);
        drawLine(surf, 80, 100, 130, 50, 10);
        drawLine(surf, 10, 120, 130, 180, 10);

        surf.setFillPattern(pattern);
        surf.fillRect(200, 50, 100, 100);

        // use same fill pattern for the triangles
        surf.translate(200, 220);
        surf.fillTriangles(verts, indices);
      }
    });
    graphics().rootLayer().add(unclipped);
  }

  void drawLine(Surface surf, float x1, float y1, float x2, float y2, float width) {
    float xmin = Math.min(x1, x2), xmax = Math.max(x1, x2);
    float ymin = Math.min(y1, y2), ymax = Math.max(y1, y2);

    surf.setFillColor(0xFF0000AA);
    surf.fillRect(xmin, ymin, xmax-xmin, ymax-ymin);

    surf.setFillColor(0xFF99FFCC);
    surf.drawLine(x1, y1, x2, y2, width);

    surf.setFillColor(0xFFFF0000);
    surf.fillRect(x1, y1, 1, 1);
    surf.fillRect(x2, y2, 1, 1);
  }

  private interface F {
    public float apply (float x);
  }

  void tessellateCurve (float minx, float maxx, float[] verts, int[] indices, F f) {
    int slices = (verts.length-1)/4, vv = 0;
    float dx = (maxx-minx)/slices;
    for (float x = minx; x < maxx; x += dx) {
      verts[vv++] = x;
      verts[vv++] = 0;
      verts[vv++] = x;
      verts[vv++] = f.apply(x);
    }
    for (int ss = 0, ii = 0; ss < slices; ss++) {
      int base = ss*2;
      indices[ii++] = base;
      indices[ii++] = base+1;
      indices[ii++] = base+3;
      indices[ii++] = base;
      indices[ii++] = base+3;
      indices[ii++] = base+2;
    }
  }
}
