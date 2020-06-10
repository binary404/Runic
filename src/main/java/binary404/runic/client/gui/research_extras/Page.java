package binary404.runic.client.gui.research_extras;

import java.util.ArrayList;

public class Page {

    public ArrayList contents = new ArrayList();

    public Page() {
    }

    public Page copy() {
        Page p = new Page();
        p.contents.addAll(this.contents);
        return p;
    }

}
