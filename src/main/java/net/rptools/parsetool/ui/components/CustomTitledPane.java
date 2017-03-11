package net.rptools.parsetool.ui.components;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import javafx.scene.control.TitledPane;

/**
 * Created by smaudet on 3/11/17.
 */
public class CustomTitledPane extends TitledPane {

  private NGNode peer;

  @Override public NGNode impl_createPeer() {
    return new CustomPGRegion();
  }

  private class CustomPGRegion extends NGRegion {
    @Override
    protected void renderContent(Graphics g) {
      super.renderContent(g);
      BasicStroke oldStroke = g.getStroke();
      BasicStroke stroke = new BasicStroke(3,1,1,1);
      g.setStroke(stroke);
      g.drawEllipse(0,0,100,40);
      g.setStroke(oldStroke);
    }

    @Override
    protected boolean hasOverlappingContents() {
      return super.hasOverlappingContents();
    }
  }
}
